/*************************************************************
 * File: pqueue-linkedlist.cpp
 *
 * Implementation file for the LinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-linkedlist.h"
#include "error.h"

LinkedListPriorityQueue::LinkedListPriorityQueue() {
	counter = 0;
	curr = NULL;
}

LinkedListPriorityQueue::~LinkedListPriorityQueue() {
	while(curr != NULL){
		ListNode *next = curr->next;
		delete curr;
		curr = next;
	}
}

int LinkedListPriorityQueue::size() {
	return counter;
}

bool LinkedListPriorityQueue::isEmpty() {
	return size() == 0;
}

void LinkedListPriorityQueue::enqueue(string value) {
	// initialize new element
	ListNode *newNode = new ListNode;
	newNode->val = value;
	newNode->next = NULL;

	if(isEmpty()){
		curr = newNode;	// new element is the first (and only) element.
	}else if(value < curr->val){ // new element is the first element.
		newNode->next = curr;
		curr = newNode;
	}else{
		for(ListNode *k = curr; k != NULL; k = k->next){
			ListNode *n = k->next;
			// new element is the last.
			if(n == NULL){
				k->next = newNode;
				break;
			}
			// new element is in the middle.
			if(k->val < value && value < n->val){
				newNode->next = n;
				k->next = newNode;
				break;
			}
		}
	}
	counter++;
}

string LinkedListPriorityQueue::peek() {
	if (isEmpty()) {
		error("You cannot form somethingness from nothingness.");
	}
	return curr->val;
}

string LinkedListPriorityQueue::dequeueMin() {
	string res = peek();
	ListNode *n = curr;
	curr = curr->next;
	delete n;
	counter--;
	return res;
}

