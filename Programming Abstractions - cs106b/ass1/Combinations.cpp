/*
 * File: Combinations.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Combinations problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "simpio.h"
using namespace std;

// calculate C(n, k) using recursion.
int countCombinations(int n, int k){
	if(k == n || k == 0) return 1;		// base case.
	return countCombinations(n - 1, k) + countCombinations(n - 1, k - 1);
}

int main() {
	int n = getInteger("n = ");
	while(n <= 0){
		cout << "Enter a positive number." << endl;
		n = getInteger("n = ");
	}
	int k = getInteger("k = ");
	while(k <= 0 || k > n){
		cout << "Enter a positive number which is less than (or equal to) n." << endl;
		k = getInteger("k = ");
	}
	int combinations = countCombinations(n, k);
	cout << "C(" << n << ", " << k << ") = " << combinations << endl; 
    return 0;
}
