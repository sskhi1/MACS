package mr

import (
	"bufio"
	"bytes"
	"encoding/json"
	"log"
	"os"
	"strconv"
)

func MapData(reply TaskReply, mapf func(string, string) []KeyValue) {
	fileName := reply.TaskFileName
	file, e := os.Open(fileName)
	if e != nil {
		log.Fatalf("error opening file")
	}
	defer file.Close()
	var content string

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		content += scanner.Text() + "\n"
	}

	if err := scanner.Err(); err != nil {
		log.Fatalf("error scanning file")
	}

	kvMap := mapf(fileName, content)
	GenerateMapFiles(reply, kvMap)
}

func GenerateMapFiles(reply TaskReply, kvMap []KeyValue) {
	NReduceTasks := reply.NTasks
	kvStruct := make(map[int]map[string][]string)
	for i := 0; i < NReduceTasks; i++ {
		kvStruct[i] = make(map[string][]string)
	}
	for _, kv := range kvMap {
		key := ihash(kv.Key) % NReduceTasks
		kvStruct[key][kv.Key] = append(kvStruct[key][kv.Key], kv.Value)
	}

	for idx, kvM := range kvStruct {
		var buf bytes.Buffer

		enc := json.NewEncoder(&buf)
		if err := enc.Encode(&kvM); err != nil {
			panic(err)
		}

		fileName := "mr-" + strconv.Itoa(reply.TaskID) + "-" + strconv.Itoa(idx) + ".json"
		file, err := os.Create(fileName)
		if err != nil {
			panic(err)
		}

		if _, err := buf.WriteTo(file); err != nil {
			panic(err)
		}
		file.Close()
	}
}

func ReduceData(reply TaskReply, reducef func(string, []string) string) {
	consolidatedData := make(map[string][]string)

	for i := 0; i < reply.NTasks; i++ {
		filename := "mr-" + strconv.Itoa(i) + "-" + reply.TaskFileName + ".json"
		file, err := os.Open(filename)
		if err != nil {
			panic(err)
		}
		defer file.Close()

		var intermediateData map[string][]string
		dec := json.NewDecoder(file)
		if err := dec.Decode(&intermediateData); err != nil {
			panic(err)
		}

		for key, values := range intermediateData {
			consolidatedData[key] = append(consolidatedData[key], values...)
		}
	}

	GenerateReduceFiles(reply, reducef, consolidatedData)
}

func GenerateReduceFiles(reply TaskReply, reducef func(string, []string) string,
	consolidatedData map[string][]string) {
	fileName := "mr-out-" + reply.TaskFileName + ".txt"
	file, err := os.Create(fileName)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	for key, values := range consolidatedData {
		output := reducef(key, values)
		line := key + " " + output + "\n"

		_, err := file.WriteString(line)
		if err != nil {
			panic(err)
		}
	}
}
