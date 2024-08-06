package shardctrler

import (
	"6.5840/raft"
	"sort"
	"time"
)
import "6.5840/labrpc"
import "sync"
import "6.5840/labgob"

type ShardCtrler struct {
	mu      sync.Mutex
	me      int
	rf      *raft.Raft
	applyCh chan raft.ApplyMsg

	// Your data here.

	configs []Config // indexed by config num

	requestIds map[int64]int
	condCh     map[int]chan Op
}

type Op struct {
	// Your data here.
	Id      int64
	Comid   int
	Op      string
	Servers map[int][]string
	GiDs    []int
	Shard   int
	Gid     int
	Num     int
}

func (sc *ShardCtrler) createCondChannel(index int) chan Op {
	sc.mu.Lock()
	defer sc.mu.Unlock()
	if _, ok := sc.condCh[index]; ok {
		return sc.condCh[index]
	}

	sc.condCh[index] = make(chan Op, 1)
	return sc.condCh[index]
}

func (sc *ShardCtrler) Match(id int64, comid int, op Op) bool {
	return id != op.Id || comid != op.Comid
}

func (sc *ShardCtrler) Join(args *JoinArgs, reply *JoinReply) {
	// Your code here.
	id := args.Id
	comid := args.Comid
	command := Op{Id: args.Id, Comid: args.Comid, Op: args.Op, Servers: args.Servers}

	index, _, isLeader := sc.rf.Start(command)
	if sc.CheckJoinWrongLeader(isLeader, reply) {
		return
	}

	condCh := sc.createCondChannel(index)

	sc.PerformJoinOperation(condCh, reply, id, comid)
}

func (sc *ShardCtrler) PerformJoinOperation(condCh chan Op, reply *JoinReply, id int64, comid int) {
	select {
	case appCommand := <-condCh:
		if sc.Match(id, comid, appCommand) {
			reply.WrongLeader = true
			return
		}
		reply.Err = OK
	case <-time.After(CommandTimeout * time.Millisecond):
		reply.WrongLeader = true
	}
}

func (sc *ShardCtrler) CheckJoinWrongLeader(isLeader bool, reply *JoinReply) bool {
	if !isLeader {
		reply.WrongLeader = true
		return true
	}

	return false
}

func (sc *ShardCtrler) Leave(args *LeaveArgs, reply *LeaveReply) {
	// Your code here.
	id := args.Id
	comid := args.Comid
	command := Op{Id: args.Id, Comid: args.Comid, Op: args.Op, GiDs: args.GIDs}

	index, _, isLeader := sc.rf.Start(command)
	if sc.CheckLeaveWrongLeader(isLeader, reply) {
		return
	}

	condCh := sc.createCondChannel(index)

	sc.PerformLeaveOperation(condCh, reply, id, comid)
}

func (sc *ShardCtrler) PerformLeaveOperation(condCh chan Op, reply *LeaveReply, id int64, comid int) {
	select {
	case appCommand := <-condCh:
		if sc.Match(id, comid, appCommand) {
			reply.WrongLeader = true
			return
		}
		reply.Err = OK
	case <-time.After(CommandTimeout * time.Millisecond):
		reply.WrongLeader = true
	}
}

func (sc *ShardCtrler) CheckLeaveWrongLeader(isLeader bool, reply *LeaveReply) bool {
	if !isLeader {
		reply.WrongLeader = true
		return true
	}

	return false
}

func (sc *ShardCtrler) Move(args *MoveArgs, reply *MoveReply) {
	// Your code here.
	id := args.Id
	comid := args.Comid
	command := Op{Id: args.Id, Comid: args.Comid, Op: args.Op, Shard: args.Shard, Gid: args.GID}

	index, _, isLeader := sc.rf.Start(command)
	if sc.CheckMoveWrongLeader(isLeader, reply) {
		return
	}

	condCh := sc.createCondChannel(index)

	sc.PerformMoveOperation(condCh, reply, id, comid)
}

func (sc *ShardCtrler) PerformMoveOperation(condCh chan Op, reply *MoveReply, id int64, comid int) {
	select {
	case appCommand := <-condCh:
		if sc.Match(id, comid, appCommand) {
			reply.WrongLeader = true
			return
		}
		reply.Err = OK
	case <-time.After(CommandTimeout * time.Millisecond):
		reply.WrongLeader = true
	}
}

func (sc *ShardCtrler) CheckMoveWrongLeader(isLeader bool, reply *MoveReply) bool {
	if !isLeader {
		reply.WrongLeader = true
		return true
	}

	return false
}

func (sc *ShardCtrler) Query(args *QueryArgs, reply *QueryReply) {
	// Your code here.
	id := args.Id
	comid := args.Comid
	num := args.Num
	command := Op{Id: args.Id, Comid: args.Comid, Op: args.Op, Num: args.Num}

	index, _, isLeader := sc.rf.Start(command)
	if sc.CheckQueryWrongLeader(isLeader, reply) {
		return
	}

	condCh := sc.createCondChannel(index)

	sc.PerformQueryOperation(condCh, reply, id, num, comid)
}

func (sc *ShardCtrler) UpdateQueryConfig(num int, reply *QueryReply) {
	sc.mu.Lock()
	defer sc.mu.Unlock()
	if num >= 0 && num < len(sc.configs) {
		reply.Config = sc.configs[num]
	} else {
		reply.Config = sc.configs[len(sc.configs)-1]
	}
}

func (sc *ShardCtrler) PerformQueryOperation(condCh chan Op, reply *QueryReply, id int64, num int, comid int) {
	select {
	case appCommand := <-condCh:
		if sc.Match(id, comid, appCommand) {
			reply.WrongLeader = true
			return
		}
		sc.UpdateQueryConfig(num, reply)
		reply.Err = OK
	case <-time.After(CommandTimeout * time.Millisecond):
		reply.WrongLeader = true
	}
}

func (sc *ShardCtrler) CheckQueryWrongLeader(isLeader bool, reply *QueryReply) bool {
	if !isLeader {
		reply.WrongLeader = true
		return true
	}

	return false
}

func (sc *ShardCtrler) Run() {
	for {
		applyMsg := <-sc.applyCh
		commandValid := applyMsg.CommandValid

		if commandValid {
			sc.ProcessCommand(applyMsg)
		}
	}
}

func (sc *ShardCtrler) ProcessCommand(applyMsg raft.ApplyMsg) {
	sc.mu.Lock()
	defer sc.mu.Unlock()

	Command := applyMsg.Command.(Op)
	commandIndex := applyMsg.CommandIndex
	id := Command.Id
	command := Command.Op
	comid := Command.Comid
	if command != "QUERY" && sc.requestIds[id] < comid {
		config := sc.CreateLastConfig()
		switch command {
		case "JOIN":
			servers := Command.Servers
			for gid, server := range servers {
				config.Groups[gid] = server
			}
			gids := make([]int, 0, len(config.Groups))
			for gid := range config.Groups {
				gids = append(gids, gid)
			}

			if len(gids) > 0 {
				sort.Ints(gids)
				for shard := range config.Shards {
					config.Shards[shard] = gids[shard%len(gids)]
				}
			} else {
				for shard := range config.Shards {
					config.Shards[shard] = 0
				}
			}
		case "LEAVE":
			gids := Command.GiDs
			for _, gid := range gids {
				delete(config.Groups, gid)
			}
			gids = make([]int, 0, len(config.Groups))
			for gid := range config.Groups {
				gids = append(gids, gid)
			}

			if len(gids) > 0 {
				sort.Ints(gids)
				for shard := range config.Shards {
					config.Shards[shard] = gids[shard%len(gids)]
				}
			} else {
				for shard := range config.Shards {
					config.Shards[shard] = 0
				}
			}
		case "MOVE":
			config.Shards[Command.Shard] = Command.Gid
		}
		sc.configs = append(sc.configs, config)
	}
	sc.requestIds[id] = comid

	if condCh, ok := sc.condCh[commandIndex]; ok {
		condCh <- Command
	}
}

func (sc *ShardCtrler) CreateLastConfig() Config {
	conf := sc.configs[len(sc.configs)-1]
	nexti := conf.Num + 1
	config := Config{Num: nexti, Groups: make(map[int][]string)}

	for gid, server := range conf.Groups {
		config.Groups[gid] = append([]string(nil), server...)
	}

	for shard, gid := range conf.Shards {
		config.Shards[shard] = gid
	}

	return config
}

// the tester calls Kill() when a ShardCtrler instance won't
// be needed again. you are not required to do anything
// in Kill(), but it might be convenient to (for example)
// turn off debug output from this instance.
func (sc *ShardCtrler) Kill() {
	sc.rf.Kill()
	// Your code here, if desired.
}

// needed by shardkv tester
func (sc *ShardCtrler) Raft() *raft.Raft {
	return sc.rf
}

// servers[] contains the ports of the set of
// servers that will cooperate via Raft to
// form the fault-tolerant shardctrler service.
// me is the index of the current server in servers[].
func StartServer(servers []*labrpc.ClientEnd, me int, persister *raft.Persister) *ShardCtrler {
	sc := new(ShardCtrler)
	sc.me = me

	sc.configs = make([]Config, 1)
	sc.configs[0].Groups = map[int][]string{}

	labgob.Register(Op{})
	sc.applyCh = make(chan raft.ApplyMsg)
	sc.rf = raft.Make(servers, me, persister, sc.applyCh)

	// Your code here.

	sc.requestIds = make(map[int64]int)
	sc.condCh = make(map[int]chan Op)

	go sc.Run()
	return sc
}
