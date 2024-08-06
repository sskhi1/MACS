package kvraft

import "6.5840/labrpc"
import "crypto/rand"
import "math/big"
import "sync"

type Clerk struct {
	servers []*labrpc.ClientEnd
	// You will have to modify this struct.
	leader int
	mu     sync.Mutex
	Id     int64
	Comid  int64
}

func nrand() int64 {
	max := big.NewInt(int64(1) << 62)
	bigx, _ := rand.Int(rand.Reader, max)
	x := bigx.Int64()
	return x
}

func MakeClerk(servers []*labrpc.ClientEnd) *Clerk {
	ck := new(Clerk)
	ck.servers = servers
	// You'll have to add code here.
	ck.leader = 0
	ck.Id = nrand()
	ck.Comid = 1
	return ck
}

// fetch the current value for a key.
// returns "" if the key does not exist.
// keeps trying forever in the face of all other errors.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.Get", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) Get(key string) string {

	// You will have to modify this function.
	ck.mu.Lock()
	defer ck.mu.Unlock()
	index := ck.leader
	args := GetArgs{ComId: ck.Comid, Key: key, GetId: ck.Id}
	for {
		reply := GetReply{}
		ck.servers[index%len(ck.servers)].Call("KVServer.Get", &args, &reply)
		switch reply.Err {
		case OK:
			ck.leader = index % len(ck.servers)
			ck.Comid += 1
			return reply.Value
		case ErrNoKey:
			ck.leader = index % len(ck.servers)
			return ""
		default:
			index += 1
		}
	}
}

// shared by Put and Append.
//
// you can send an RPC with code like this:
// ok := ck.servers[i].Call("KVServer.PutAppend", &args, &reply)
//
// the types of args and reply (including whether they are pointers)
// must match the declared types of the RPC handler function's
// arguments. and reply must be passed as a pointer.
func (ck *Clerk) PutAppend(key string, value string, op string) {
	// You will have to modify this function.
	ck.mu.Lock()
	defer ck.mu.Unlock()
	index := ck.leader
	args := PutAppendArgs{PutAppendId: ck.Id, Key: key,
		Value: value, Op: op, ComId: ck.Comid}
	for {
		reply := PutAppendReply{}
		ck.servers[index%len(ck.servers)].Call("KVServer.PutAppend", &args, &reply)
		switch reply.Err {
		case OK:
			ck.leader = index % len(ck.servers)
			ck.Comid += 1
			return
		default:
			index += 1
		}
	}
}

func (ck *Clerk) Put(key string, value string) {
	ck.PutAppend(key, value, "Put")
}
func (ck *Clerk) Append(key string, value string) {
	ck.PutAppend(key, value, "Append")
}
