/*************************************************************
 * File: pqueue-vector.cpp
 *
 * Implementation file for the VectorPriorityQueue
 * class.
 */
 
#include "pqueue-vector.h"
#include "error.h"

VectorPriorityQueue::VectorPriorityQueue() {
	Vector<string> queue;
}

VectorPriorityQueue::~VectorPriorityQueue() {}

int VectorPriorityQueue::size() {
	return queue.size();
}

bool VectorPriorityQueue::isEmpty() {
	return size() == 0;
}

void VectorPriorityQueue::enqueue(string value) {
	queue.add(value);
}

string VectorPriorityQueue::peek() {
	if (isEmpty()) {
		error("You cannot form somethingness from nothingness.");
	}
	string res = queue[0];
	for(int i = 1; i < queue.size(); i++){
		if(queue[i] < res){
			res = queue[i];
		}
	}
	return res;
}

string VectorPriorityQueue::dequeueMin() {
	if (isEmpty()) {
		error("You cannot form somethingness from nothingness.");
	}
	string res = queue[0];
	// remove minimum element.
	int idx = 0;
	for(int i = 1; i < queue.size(); i++){
		if(queue[i] < res){
			res = queue[i];
			idx = i;
		}
	}
	queue.remove(idx);
	return res;
}

