package mr

import (
	"strconv"
	"time"
)

func ReduceTaskProcess(c *Coordinator, reply *TaskReply) (bool, bool) {
	doneReduceTasks := 0
	for idx, task := range c.reduceTasks {
		CheckTimeIntervalReduce(c, &task, idx)

		if task.status == FINISHED {
			doneReduceTasks++
		} else if task.status == NOT_STARTED {
			c.reduceTasks[idx] = Task{
				fileName:  "nil",
				status:    IN_PROGRESS,
				StartTime: time.Now(),
			}
			reply.TaskFileName = strconv.Itoa(idx)
			reply.TaskID = idx
			reply.TaskType = REDUCE
			reply.NTasks = c.NMapTasks
			return false, true
		}
	}

	return doneReduceTasks == len(c.reduceTasks), false
}

func MapTaskProcess(c *Coordinator, reply *TaskReply) (bool, bool) {
	doneMapTasks := 0
	for idx, task := range c.mapTasks {
		fileName := c.mapTasks[idx].fileName
		CheckTimeIntervalMap(c, &task, idx)

		if task.status == FINISHED {
			doneMapTasks++
		} else if task.status == NOT_STARTED {
			c.mapTasks[idx] = Task{fileName, IN_PROGRESS, time.Now()}
			reply.TaskFileName = fileName
			reply.TaskID = idx
			reply.TaskType = MAP
			reply.NTasks = c.NReduceTasks
			return false, true
		}
	}

	return doneMapTasks == len(c.mapTasks), false
}

// CheckTimeIntervalMap check if the process is going more than 10 secs
func CheckTimeIntervalMap(c *Coordinator, task *Task, idx int) {
	fileName := c.mapTasks[idx].fileName
	if task.status == IN_PROGRESS && time.Since(task.StartTime) > 10*time.Second {
		c.mapTasks[idx] = Task{
			fileName:  fileName,
			status:    NOT_STARTED,
			StartTime: time.Now(),
		}
	}
}

// CheckTimeIntervalReduce check if the process is going more than 10 secs
func CheckTimeIntervalReduce(c *Coordinator, task *Task, idx int) {
	if task.status == IN_PROGRESS && time.Since(task.StartTime) > 10*time.Second {
		c.reduceTasks[idx] = Task{
			fileName:  "nil",
			status:    NOT_STARTED,
			StartTime: time.Now(),
		}
	}
}
