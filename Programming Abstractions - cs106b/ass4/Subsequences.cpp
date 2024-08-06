/*
 * File: Subsequences.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Subsequences problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
using namespace std;

/* Given two strings, returns whether the second string is a
 * subsequence of the first string.
 */
bool isSubsequence(string text, string subsequence);

int main() {
	while(true){
		string text = getLine("Text: ");
		if(text.length() == 0) break;
		string subsequence = getLine("Check subsequence: ");
		if(subsequence.length() == 0) break;
		
		if(isSubsequence(text, subsequence)){
			cout << subsequence << " is a subsequence of " << text << "." << endl;
		}else{
			cout << subsequence << " is not a subsequence of " << text << "." << endl;
		}
	}
	cout << "The End" << endl;
    return 0;
}

bool isSubsequence(string text, string subsequence){
	if(subsequence.length() == 0) return true; // empty string is subsequence of every string.
	if(text.length() == 0) return false;	// empty string has no subsequences.
	if(subsequence[0] == text[0]){
		return isSubsequence(text.substr(1), subsequence.substr(1));
	}
	return isSubsequence(text.substr(1), subsequence);
}
