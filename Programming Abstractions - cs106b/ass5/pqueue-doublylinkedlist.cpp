/*************************************************************
 * File: pqueue-doublylinkedlist.cpp
 *
 * Implementation file for the DoublyLinkedListPriorityQueue
 * class.
 */
 
#include "pqueue-doublylinkedlist.h"
#include "error.h"

DoublyLinkedListPriorityQueue::DoublyLinkedListPriorityQueue() {
	counter = 0;
	curr = NULL;
}

DoublyLinkedListPriorityQueue::~DoublyLinkedListPriorityQueue() {
	while(curr != NULL){
		ListNode *next = curr->next;
		delete curr;
		curr = next;
	}
}

int DoublyLinkedListPriorityQueue::size() {
	return counter;
}

bool DoublyLinkedListPriorityQueue::isEmpty() {
	return size() == 0;
}

void DoublyLinkedListPriorityQueue::enqueue(string value) {
	// initialize new element.
	ListNode *newNode = new ListNode;
	newNode->val = value;
	newNode->prev = NULL;
	newNode->next = NULL;

	if(isEmpty()){
		curr = newNode;	// new element is the only element.
	}else{
		curr->prev = newNode;
		newNode->next = curr;
		curr = newNode;	// new element becomes the first element.
	}
	counter++;
}

string DoublyLinkedListPriorityQueue::peek() {
	if (isEmpty()) {
		error("You cannot form somethingness from nothingness.");
	}
	string result = curr->val;
	for(ListNode *n = curr; n != NULL; n = n->next){
		if(n->val < result){
			result = n->val;
		}
	}
	return result;
}

string DoublyLinkedListPriorityQueue::dequeueMin() {
	if (isEmpty()) {
		error("You cannot form somethingness from nothingness.");
	}
	string result = curr->val;
	// find minimum element node to delete.
	ListNode *resNode = curr;
	for(ListNode *n = curr; n != NULL; n = n->next){
		if(n->val < result){
			resNode = n;
			result = n->val;
		}
	}

	if(resNode->prev == NULL){
		if(resNode->next == NULL){
			curr = NULL;
		}else{
			resNode->next->prev = NULL;
			curr = resNode->next;
		}	
	}else{
		if(resNode->next == NULL){
			resNode->prev->next = NULL;
		}else{
			resNode->next->prev = resNode->prev;
			resNode->prev->next = resNode->next;
		}
	}
	delete resNode;
	counter--;
	return result;
}

