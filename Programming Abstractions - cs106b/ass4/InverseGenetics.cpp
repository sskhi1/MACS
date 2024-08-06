/*
 * File: InverseGenetics.cpp
 * --------------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the Inverse Genetics problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include <string>
#include <fstream>
#include "set.h"
#include "map.h"
#include "console.h"
#include "simpio.h"
using namespace std;

/* Function: listAllRNAStrandsFor(string protein,
 *                                Map<char, Set<string> >& codons);
 * Usage: listAllRNAStrandsFor("PARTY", codons);
 * ==================================================================
 * Given a protein and a map from amino acid codes to the codons for
 * that code, lists all possible RNA strands that could generate
 * that protein
 */
void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons);
Set<string> findAllCombinations(string protein, Map<char, Set<string> >& codons);
void printResult(Set<string> &AllCombinations);

/* Function: loadCodonMap();
 * Usage: Map<char, Lexicon> codonMap = loadCodonMap();
 * ==================================================================
 * Loads the codon mapping table from a file.
 */
Map<char, Set<string> > loadCodonMap();

int main() {
    /* Load the codon map. */
    Map<char, Set<string> > codons = loadCodonMap();

	while(true){
		string protein = getLine("Protein order [or nothing to stop]: ");
		if(protein.length() == 0) break;
		listAllRNAStrandsFor(protein, codons);
		cout << endl;
	}
	cout << "The End" << endl;
    return 0;
}

/* You do not need to change this function. */
Map<char, Set<string> > loadCodonMap() {
    ifstream input("codons.txt");
    Map<char, Set<string> > result;

    /* The current codon / protein combination. */
    string codon;
    char protein;

    /* Continuously pull data from the file until all data has been
     * read.
     */
    while (input >> codon >> protein) {
        result[protein] += codon;
    }

    return result;
}

void listAllRNAStrandsFor(string protein, Map<char, Set<string> >& codons){
	Set<string> AllCombinations = findAllCombinations(protein, codons);
	printResult(AllCombinations);
}

Set<string> findAllCombinations(string protein, Map<char, Set<string> >& codons){
	Set<string> AllCombinations;
	// base case
	if(protein.length() == 1){
		return codons[protein[0]];
	}

	Set<string> firstCodon = codons[protein[0]]; // codons of first amino acid (letter) of a protein.
	Set<string> RestCombinations = findAllCombinations(protein.substr(1), codons); // RNA strands without first amino acid of a protein.
	
	foreach(string codon in firstCodon){
		foreach(string combination in RestCombinations){
			string result = codon + combination;
			AllCombinations.add(result);
		}
	}
	return AllCombinations;
}


void printResult(Set<string> &AllCombinations){
	foreach(string s in AllCombinations){
		cout << s << " ";
	}
	cout << endl;
}