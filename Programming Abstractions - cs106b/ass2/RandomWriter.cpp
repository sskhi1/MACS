/*
 * File: RandomWriter.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Random Writer problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
#include "map.h"
#include "vector.h"
#include "random.h"
#include <fstream>
using namespace std;

const int symbols = 2000;	// Number of symbols we want to write.

void initialize(ifstream &stream, int &order);
void makeMap(Map<string, Vector<char>> &text_info, ifstream &stream, int order);
string getFirst(ifstream &stream, int order);
string getMostOrder(Map<string, Vector<char>> &text_info);
void printRandomText(string mostOrder, Map<string, Vector<char>> &text_info);

int main() {
    ifstream stream;
	int order;
	initialize(stream, order);


	Map<string, Vector<char>> text_info;
	makeMap(text_info, stream, order);

	string mostOrder = getMostOrder(text_info);
	printRandomText(mostOrder, text_info);
    return 0;
}


void initialize(ifstream &stream, int &order){
	// Ask for file unless its real.
	while(true){
		string filename = getLine("Enter the source text: ");
		stream.open(filename.c_str());
		if(stream.is_open()) break;
		cout << "Wrong File Name!" << endl;
		stream.clear();
	}
	// Ask for Markov order unless it's from 1 to 10.
	while(true){
		order = getInteger("Enter the Markov order [1-10]: ");
		if(order > 0 && order < 11) break;
	}
	cout << "Processing file..." << endl;
}

// key - 'order' size string, value - list of chars which come after the key.
void makeMap(Map<string, Vector<char>> &text_info, ifstream &stream, int order){
	string firstOrder = getFirst(stream, order);
	char ch;
	while(stream.get(ch)){
		text_info[firstOrder].add(ch);
		firstOrder = firstOrder.substr(1) + ch;
	}
}

// get first 'order' size string from the file.
string getFirst(ifstream &stream, int order){
	string ans;
	char ch;
	for(int i = 0 ; i < order; i++){
		stream.get(ch);
		ans += ch;
	}
	return ans;
}
 
// find the string in the map, which has the longest vector as a value.
string getMostOrder(Map<string, Vector<char>> &text_info){
	int maxSize = 0;
	string mostOrder;
	foreach(string order in text_info){
		if(text_info[order].size() > maxSize){
			maxSize = text_info[order].size();
			mostOrder = order;
		}
	}
	return mostOrder;
}

void printRandomText(string mostOrder, Map<string, Vector<char>> &text_info){
	cout << mostOrder;
	string nextOrder = mostOrder;
	for(int i = 0 ; i < symbols; i++){
		Vector<char> nextPossibleChars = text_info[nextOrder];
		int rand = randomInteger(0, nextPossibleChars.size() - 1);
		char ch = nextPossibleChars[rand];
		cout << ch;
		nextOrder = nextOrder.substr(1) + ch;
	}
	cout << endl;
}