/*
 * File: ConsecutiveHeads.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Consecutive Heads problem.
 * [TODO: rewrite the documentation]
 */

#include <iostream>
#include "console.h"
#include "random.h"   // import randomizer.
using namespace std;

const int goal = 3;   // the number of consecutive heads we want.
const double p = 0.5; // probability of getting heads (or tails).

// simulate and count flips until we get as many consecutive heads as we want.
int simulateFlips(int myGoal){
	int flips = 0;
	int counter = 0;  // the number of consecutive heads we have.
	while(true){
		flips++;
		if(randomChance(p)){	// We got heads.
			cout << "heads" << endl;
			counter++;
			if(counter == goal) break;
		}else{					// We got tails.
			cout << "tails" << endl;
			counter = 0;
		}
	}
	return flips;
}

int main() {
	int flips = simulateFlips(goal);
	cout << "It took " << flips << " flips to get " << goal << " consecutive heads." << endl;
    return 0;
}
