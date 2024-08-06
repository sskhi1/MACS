/*************************************************************
 * File: pqueue-heap.cpp
 *
 * Implementation file for the HeapPriorityQueue
 * class.
 */
 
#include "pqueue-heap.h"
#include "error.h"

HeapPriorityQueue::HeapPriorityQueue() {
	arrSize = INIT_SIZE;
	data = new string[arrSize];
	elems = 0;
}

HeapPriorityQueue::~HeapPriorityQueue() {
	delete[] data;
}

int HeapPriorityQueue::size() {
	return elems;
}

bool HeapPriorityQueue::isEmpty() {
	return size() == 0;
}

void HeapPriorityQueue::enqueue(string value) {
	// check if the size of the array is bigger than the elements. if not, resize array (make it 2 times bigger).
	if(size() == arrSize){
		string *temp = data;
		arrSize *= 2;
		data = new string[arrSize];
		for(int i = 0; i < elems; i++){
			data[i] = temp[i];
		}
		delete[] temp;
	}
	data[elems] = value;
	bubbleUp(elems);
	elems++;
}

void HeapPriorityQueue::bubbleUp(int pos){
	if(pos == 0){
		return;
	}else{
		int parent = (pos+1)/2 - 1;
		if(data[pos] < data[parent]){
			swap(data[pos], data[parent]);
			bubbleUp(parent);
		}
	}
}

string HeapPriorityQueue::peek() {
	if (isEmpty()) {
		error("You cannot form somethingness from nothingness.");
	}
	return data[0];
}

string HeapPriorityQueue::dequeueMin() {
	string result = peek();
	data[0] = data[elems - 1];
	elems--;
	bubbleDown(0);
	return result;
}

void HeapPriorityQueue::bubbleDown(int pos){
	int childPos1 = 2*(pos + 1) - 1;
	int childPos2 = 2*(pos + 1);
	if(childPos1 > elems - 1 && childPos2 > elems - 1) return;


	if(data[childPos1] < data[pos] && data[childPos2] >= data[pos]){
		swap(data[pos], data[childPos1]);
		bubbleDown(childPos1);
	}else if(data[childPos1] >= data[pos] && data[childPos2] < data[pos]){
		swap(data[pos], data[childPos2]);
		bubbleDown(childPos2);
	}else if(data[childPos1] < data[pos] && data[childPos2] < data[pos]){
		if(data[childPos1] < data[childPos2]){
			swap(data[pos], data[childPos1]);
			bubbleDown(childPos1);
		}else{
			swap(data[pos], data[childPos2]);
			bubbleDown(childPos2);
		}
	}else{
		return;
	}
}

