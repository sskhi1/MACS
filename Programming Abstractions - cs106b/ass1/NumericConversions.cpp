/*
 * File: NumericConversions.cpp
 * ---------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Numeric Conversions problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include <string>
#include "simpio.h"
#include "console.h"
using namespace std;

/* Function prototypes */

string intToString(int n);
int stringToInt(string str);

int main() {
	int n = getInteger("int to string: ");
	string str = getLine("string to int: ");
	cout << "hi" << endl;
	cout << intToString(n) << " program works" << endl;
	cout << stringToInt(str) + 9 << " program works" << endl;
    return 0;
}

// converts int to string.
string intToString(int n){
	string answer;
	if(n < 0) answer += "-";

	int value = abs(n);		  // turn the integer positive.
	int lastNum = value % 10; // the last digit of the positive integer (137 % 10 = 7).
	int restNum = value / 10; // integer without the last digit (137 / 10 = 13).
	
	if(value < 10) return answer += char('0' + lastNum); // base case

	return answer += intToString(restNum) + intToString(lastNum);

}

int getSign(string &str){
	int sign;
	if(str[0] == '-'){
		sign = -1;
		str = str.substr(1);
	}else{
		sign = 1;
	}
	return sign;
}


//converts string to int. 
int stringToInt(string str){
	int sign = getSign(str); // clarify if the number is possitive or negative.

	int len = str.length();
	if(len == 0) return 0; // base case

	return  sign * (stringToInt(str.substr(0, len - 1))*10 + (int)(str[len - 1] - '0'));
}

