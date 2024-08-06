package mr

import "log"
import "net"
import "os"
import "net/rpc"
import "net/http"
import "sync"
import "time"

type Task struct {
	fileName  string
	status    int
	StartTime time.Time
}

type Coordinator struct {
	// Your definitions here.
	NReduceTasks int
	NMapTasks    int
	mapTasks     []Task
	reduceTasks  []Task
	lock         sync.Mutex
}

// Your code here -- RPC handlers for the worker to call.
func (c *Coordinator) GetNextTask(_ *TaskArgs, reply *TaskReply) error {
	c.lock.Lock()
	defer c.lock.Unlock()

	result := CheckMapReduce(c, reply)
	if result {
		reply.TaskType = DONE
	}
	return nil
}

func CheckMapReduce(c *Coordinator, reply *TaskReply) bool {
	mapDone, mapNotStarted := MapTaskProcess(c, reply)
	if mapNotStarted {
		return false
	}
	if !mapDone {
		reply.TaskType = STOP
		return false
	}

	reduceDone, reduceNotStarted := ReduceTaskProcess(c, reply)
	if reduceNotStarted {
		return false
	}
	if !reduceDone {
		reply.TaskType = STOP
		return false
	}
	return true
}

func (c *Coordinator) TaskDone(args *TaskReply, _ *TaskReply) error {
	c.lock.Lock()
	defer c.lock.Unlock()
	ProcessFinish(c, args)
	return nil
}

func ProcessFinish(c *Coordinator, a *TaskReply) {
	if a.TaskType == MAP {
		c.mapTasks[a.TaskID].status = FINISHED
	} else if a.TaskType == REDUCE {
		c.reduceTasks[a.TaskID].status = FINISHED
	}
}

// an example RPC handler.
//
// the RPC argument and reply types are defined in rpc.go.
func (c *Coordinator) Example(args *ExampleArgs, reply *ExampleReply) error {
	reply.Y = args.X + 1
	return nil
}

// start a thread that listens for RPCs from worker.go
func (c *Coordinator) server() {
	rpc.Register(c)
	rpc.HandleHTTP()
	//l, e := net.Listen("tcp", ":1234")
	sockname := coordinatorSock()
	os.Remove(sockname)
	l, e := net.Listen("unix", sockname)
	if e != nil {
		log.Fatal("listen error:", e)
	}
	go http.Serve(l, nil)
}

// main/mrcoordinator.go calls Done() periodically to find out
// if the entire job has finished.
func (c *Coordinator) Done() bool {
	// Your code here.
	for _, task := range c.reduceTasks {
		if task.status != FINISHED {
			return false
		}
	}
	return true
}

// create a Coordinator.
// main/mrcoordinator.go calls this function.
// nReduce is the number of reduce tasks to use.
func MakeCoordinator(files []string, nReduce int) *Coordinator {
	c := Coordinator{}

	// Your code here.
	if nReduce < 1 {
		return nil
	}
	c.NReduceTasks = nReduce
	c.NMapTasks = len(files)

	c.mapTasks = make([]Task, c.NMapTasks)
	c.reduceTasks = make([]Task, nReduce)

	for i, filename := range files {
		c.mapTasks[i] = Task{filename, NOT_STARTED, time.Time{}}
	}

	for i := 0; i < nReduce; i++ {
		c.reduceTasks[i] = Task{"nil", NOT_STARTED, time.Time{}}
	}
	c.server()
	return &c
}
