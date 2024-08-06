/*
 * File: WordLadder.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Word Ladder problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
#include "queue.h"
#include "vector.h"
#include "lexicon.h"
#include "set.h"

using namespace std;

string toLowerCase(string str);
bool wordExists(string str, Lexicon &myLexicon);
Vector<string> find_ladder(string str1, string str2, Lexicon &myLexicon);
void printLadder(Vector<string> &ladder);

int main() {
	Lexicon myLexicon("EnglishWords.dat");
	while(true){
		string str1 = getLine("Enter the starting word (or nothing to quit): ");
		if(str1.empty()) break;
		// You must enter a word which exists...
		if(!wordExists(str1, myLexicon)){
			cout << "Please, enter an English word." << endl;
			continue;
		}
		string str2 = getLine("Enter the ending word (or nothing to quit): ");
		if(str2.empty()) break;
		if(!wordExists(str2, myLexicon)){
			cout << "Please, enter an English word." << endl;
			continue;
		}
		
		cout << "searching..." << endl;
	    Vector<string> ladder = find_ladder(str1, str2, myLexicon);
		if(!ladder.isEmpty()){
			cout << "Found Ladder: ";
			printLadder(ladder);
			cout << endl;
		}else{
			cout << "No word ladder could be found." << endl;
			cout << endl;
		}
	}
	cout << "End of the program." << endl;
    return 0;
}

// returns true, if the word is in lexicon.
bool wordExists(string str, Lexicon &myLexicon){
	return myLexicon.contains(str);
}

Vector<string> find_ladder(string str1, string str2, Lexicon &myLexicon){
	Vector<string> vec;
	if(str1.length() != str2.length()) return vec; // The words must be same size.
	// Just like Pseudocode implementation of the word-ladder algorithm.
	Queue<Vector<string>> ladders;
	Set<string> myWords;  // helps us to know that word has not already been used in a ladder.
	myWords.add(str1);
	Vector<string> list;	// ladder list.
	list.add(str1);
	ladders.enqueue(list);
	while(!ladders.isEmpty()){
		Vector<string> curr = ladders.dequeue();
		string lastStr = curr[curr.size() - 1];
		if(lastStr == str2){ // ladder found
			return curr;
		}else{
		string copy = lastStr;
		// Find each 1 letter different from last word' string.
		for(int i = 0 ; i < lastStr.length(); i++){
			for (char ch = 'a'; ch <= 'z'; ch++){
					copy[i] = ch;
					if(!myWords.contains(copy) && wordExists(copy, myLexicon)){
						Vector<string> v = curr;
						v.add(copy);
						myWords.add(copy);
						ladders.enqueue(v);
					}

			}
			copy = lastStr;
		}

	}
	}
	return vec;	// empty vector.
}

void printLadder(Vector<string> &ladder){
	for(int i = 0 ; i < ladder.size() - 1; i++){
		cout << ladder[i] << "->";
	}
	cout << ladder[ladder.size() - 1];
	cout << endl;
}


