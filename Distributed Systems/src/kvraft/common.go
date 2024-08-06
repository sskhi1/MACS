package kvraft

const (
	OK             = "OK"
	ErrNoKey       = "ErrNoKey"
	ErrWrongLeader = "ErrWrongLeader"
)

const CommandTimeout = 1000
const BroadcastTimeout = 100

type Err string

// Put or Append
type PutAppendArgs struct {
	Key   string
	Value string
	Op    string // "Put" or "Append"
	// You'll have to add definitions here.
	// Field names must start with capital letters,
	// otherwise RPC will break.
	PutAppendId int64
	ComId       int64
}

type PutAppendReply struct {
	Err Err
}

type GetArgs struct {
	Key string
	// You'll have to add definitions here.
	GetId int64
	ComId int64
}

type GetReply struct {
	Err   Err
	Value string
}

type Snapshot struct {
	Data     map[string]string
	Requests map[int64]int64
}