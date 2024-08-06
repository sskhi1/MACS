/*
 * File: FleschKincaid.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Flesch-Kincaid problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <fstream>
#include "simpio.h"
#include "console.h"
#include "tokenscanner.h"
using namespace std;

const double C_0 = -15.59;
const double C_1 = 0.39;
const double C_2 = 11.8;

bool isVowel(char ch);
bool isSentenceEnding(string token);
int countSyllables(string str);
void countAll(TokenScanner scanner, int &words, int &sentences, int &syllables);
double calculateScore(int words, int sentences, int syllables);
void printResult(int words, int sentences, int syllables);


int main() {
	ifstream stream;
	while(true){
		string filename = getLine("Enter file name: ");
		stream.open(filename.c_str());
		if(stream.is_open()) break;
		cout << "Wrong File Name!" << endl;
		stream.clear();
	}

	TokenScanner scanner(stream);
	scanner.ignoreWhitespace();
	scanner.addWordCharacters("'");

	int words = 0;
	int sentences = 0;
	int syllables = 0;

	countAll(scanner, words, sentences, syllables);
	printResult(words, sentences, syllables);
	return 0;
}

bool isVowel(char ch){
	return ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'y';
}

bool isSentenceEnding(string token){
	return token[token.length() - 1] == '.' || token[token.length() - 1] == '!' 
			|| token[token.length() - 1] == '?';
}

// count syllables in the word.
int countSyllables(string token){
	int result = 0;

	string str;
	// make all chars lower case.
	for(int i = 0 ; i < token.length(); i++){
		str += tolower(token[i]);
	}

	for(int i = 0; i < str.length(); i++){
		if(isVowel(str[i])) result++;
	}
	if(str[str.length() - 1] == 'e') result--; // if the word ends with 'e' we don't count it as a syllable.
	for(int i = 1 ; i < str.length(); i++){
		if(isVowel(str[i]) && isVowel(str[i-1])){
			result--;
		}
	}

	if(str.length() >= 2){
		if(isVowel(str[str.length() - 2]) && str[str.length() - 1] == 'e') result++; // e.g. "degree".
	}
	if(result <= 0) result = 1;
	return result;
}

// count words, sentences and syllables in the text.
void countAll(TokenScanner scanner, int &words, int &sentences, int &syllables){

	while(scanner.hasMoreTokens()){
		string token = scanner.nextToken();
		
		TokenType type = scanner.getTokenType(token);
		if(type == WORD && isalpha(token[0])){
			words++;
			syllables += countSyllables(token);
		}
		
		if(isSentenceEnding(token)){
				sentences++;
		}
	}

	// We can't devide by 0.
	if(sentences == 0) sentences = 1;
	if(words == 0) words = 1;
}

// calculate final result.
double calculateScore(int words, int sentences, int syllables){
	double score = C_0 + C_1 * (words * 1.0 / sentences) +
			C_2 * (syllables * 1.0 / words);
	return score;
}

// print result.
void printResult(int words, int sentences, int syllables) {
	cout << words << " words" << endl;
	cout << syllables << " syllables" << endl;
	cout << sentences << " sentences" << endl;
	cout << "score: " << calculateScore(words, sentences, syllables) << endl;
	return;
}