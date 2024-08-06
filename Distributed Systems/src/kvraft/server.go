package kvraft

import (
	"6.5840/labgob"
	"6.5840/labrpc"
	"6.5840/raft"
	"bytes"
	"log"
	"sync"
	"sync/atomic"
	"time"
)

const Debug = false

func DPrintf(format string, a ...interface{}) (n int, err error) {
	if Debug {
		log.Printf(format, a...)
	}
	return
}

type Op struct {
	// Your definitions here.
	// Field names must start with capital letters,
	// otherwise RPC will break.
	Key     string
	Value   string
	Id      int64
	Command string
	Comid   int64
}

type KVServer struct {
	mu      sync.Mutex
	me      int
	rf      *raft.Raft
	applyCh chan raft.ApplyMsg
	dead    int32 // set by Kill()

	maxraftstate int // snapshot if log grows this big

	// Your definitions here.
	kvdata           map[string]string
	lastIndexApplied map[int]int64
	cond             *sync.Cond
	term             int
	applied          map[int64]bool
	condCh           map[int]chan Op
	requestIds       map[int64]int64
}

func (kv *KVServer) readPersist(snapshot []byte) {
	if snapshot == nil || len(snapshot) < 1 {
		return
	}
	r := bytes.NewBuffer(snapshot)
	d := labgob.NewDecoder(r)

	var sstate Snapshot
	if d.Decode(&sstate) != nil {
		panic("bad persistent state")
	} else {
		kv.kvdata = sstate.Data
		kv.requestIds = sstate.Requests
	}
}

func (kv *KVServer) Snapshot(commandIndex int, snapshot Snapshot) {
	w := new(bytes.Buffer)
	e := labgob.NewEncoder(w)
	err := e.Encode(snapshot)
	if err != nil {
		return
	}
	kv.rf.Snapshot(commandIndex, w.Bytes())
}

func (kv *KVServer) createCondChannel(index int) chan Op {
	kv.mu.Lock()
	defer kv.mu.Unlock()
	if _, ok := kv.condCh[index]; ok {
		return kv.condCh[index]
	}

	kv.condCh[index] = make(chan Op, 1)
	return kv.condCh[index]
}

func (kv *KVServer) Match(id int64, comid int64, op Op) bool {
	return id != op.Id || comid != op.Comid
}

func (kv *KVServer) Get(args *GetArgs, reply *GetReply) {
	// Your code here.
	key := args.Key
	id := args.GetId
	comid := args.ComId
	command := Op{Id: args.GetId, Key: args.Key, Command: "Get", Comid: comid}

	index, _, isLeader := kv.rf.Start(command)
	if kv.CheckGetWrongLeader(isLeader, reply) {
		return
	}
	condCh := kv.createCondChannel(index)

	kv.PerformGetOperation(condCh, reply, id, comid)

	kv.mu.Lock()
	reply.Value = kv.kvdata[key]
	kv.mu.Unlock()
}

func (kv *KVServer) PerformGetOperation(condCh chan Op, reply *GetReply, id int64, comid int64) {
	select {
	case appCommand := <-condCh:
		if kv.Match(id, comid, appCommand) {
			reply.Err = ErrWrongLeader
			return
		}
		reply.Err = OK
	case <-time.After(CommandTimeout * time.Millisecond):
		reply.Err = ErrWrongLeader
	}
}

func (kv *KVServer) CheckGetWrongLeader(isLeader bool, reply *GetReply) bool {
	if !isLeader {
		reply.Err = ErrWrongLeader
		return true
	}

	return false
}

func (kv *KVServer) PutAppend(args *PutAppendArgs, reply *PutAppendReply) {
	// Your code here.
	id := args.PutAppendId
	comid := args.ComId
	command := Op{Id: id, Command: args.Op, Key: args.Key, Value: args.Value, Comid: args.ComId}

	index, _, isLeader := kv.rf.Start(command)
	if kv.CheckPutAppendWrongLeader(isLeader, reply) {
		return
	}
	condCh := kv.createCondChannel(index)

	kv.PerformPutAppendOperation(condCh, reply, id, comid)
}

func (kv *KVServer) PerformPutAppendOperation(condCh chan Op, reply *PutAppendReply, id int64, comid int64) {
	select {
	case appCommand := <-condCh:
		if kv.Match(id, comid, appCommand) {
			reply.Err = ErrWrongLeader
			return
		}
		reply.Err = OK
	case <-time.After(CommandTimeout * time.Millisecond):
		reply.Err = ErrWrongLeader
	}
}

func (kv *KVServer) CheckPutAppendWrongLeader(isLeader bool, reply *PutAppendReply) bool {
	if !isLeader {
		reply.Err = ErrWrongLeader
		return true
	}

	return false
}

func (kv *KVServer) Run() {
	for {
		applyMsg := <-kv.applyCh
		commandValid := applyMsg.CommandValid
		commandIndex := applyMsg.CommandIndex

		if commandValid {
			kv.ProcessCommand(applyMsg)
			if kv.maxraftstate != -1 {
				snapshot := Snapshot{Data: kv.kvdata, Requests: kv.requestIds}
				kv.Snapshot(commandIndex, snapshot)
			}
		}

		if applyMsg.SnapshotValid {
			kv.readPersist(applyMsg.Snapshot)
		}
	}
}

func (kv *KVServer) ProcessCommand(applyMsg raft.ApplyMsg) {
	kv.mu.Lock()
	defer kv.mu.Unlock()
	Command := applyMsg.Command.(Op)
	commandIndex := applyMsg.CommandIndex
	id := Command.Id
	command := Command.Command
	key := Command.Key
	value := Command.Value
	comid := Command.Comid
	if kv.requestIds[id] < comid {
		switch command {
		case "Put":
			kv.kvdata[key] = value
		case "Append":
			kv.kvdata[key] += value
		default:
			_, err := DPrintf("get? why?")
			if err != nil {
				return
			}
		}
		kv.requestIds[id] = comid
	}

	if condCh, ok := kv.condCh[commandIndex]; ok {
		condCh <- Command
	}
}

// the tester calls Kill() when a KVServer instance won't
// be needed again. for your convenience, we supply
// code to set rf.dead (without needing a lock),
// and a killed() method to test rf.dead in
// long-running loops. you can also add your own
// code to Kill(). you're not required to do anything
// about this, but it may be convenient (for example)
// to suppress debug output from a Kill()ed instance.
func (kv *KVServer) Kill() {
	atomic.StoreInt32(&kv.dead, 1)
	kv.rf.Kill()
	// Your code here, if desired.
}

func (kv *KVServer) killed() bool {
	z := atomic.LoadInt32(&kv.dead)
	return z == 1
}

// servers[] contains the ports of the set of
// servers that will cooperate via Raft to
// form the fault-tolerant key/value service.
// me is the index of the current server in servers[].
// the k/v server should store snapshots through the underlying Raft
// implementation, which should call persister.SaveStateAndSnapshot() to
// atomically save the Raft state along with the snapshot.
// the k/v server should snapshot when Raft's saved state exceeds maxraftstate bytes,
// in order to allow Raft to garbage-collect its log. if maxraftstate is -1,
// you don't need to snapshot.
// StartKVServer() must return quickly, so it should start goroutines
// for any long-running work.
func StartKVServer(servers []*labrpc.ClientEnd, me int, persister *raft.Persister, maxraftstate int) *KVServer {
	// call labgob.Register on structures you want
	// Go's RPC library to marshall/unmarshall.
	labgob.Register(Op{})
	labgob.Register(Snapshot{})

	kv := new(KVServer)
	kv.me = me
	kv.maxraftstate = maxraftstate

	// You may need initialization code here.

	kv.applyCh = make(chan raft.ApplyMsg)
	kv.rf = raft.Make(servers, me, persister, kv.applyCh)
	kv.kvdata = make(map[string]string)
	kv.lastIndexApplied = make(map[int]int64)
	kv.applied = make(map[int64]bool)
	kv.cond = sync.NewCond(&kv.mu)
	kv.term = 1
	kv.requestIds = make(map[int64]int64)
	kv.condCh = make(map[int]chan Op)

	// You may need initialization code here.

	go kv.Run()

	go func() {
		for !kv.killed() {
			kv.cond.Broadcast()
			time.Sleep(BroadcastTimeout * time.Millisecond)
		}
	}()
	return kv
}
