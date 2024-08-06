/*
 * File: UniversalHealthCoverage.cpp
 * ----------------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the starter project for the UniversalHealthCoverage problem
 * on Assignment #3.
 * [TODO: extend the documentation]
 */
#include <iostream>
#include <string>
#include "simpio.h"
#include "set.h"
#include "vector.h"
#include "console.h"
using namespace std;

/* Function: canOfferUniversalCoverage(Set<string>& cities,
 *                                     Vector< Set<string> >& locations,
 *                                     int numHospitals,
 *                                     Vector< Set<string> >& result);
 * Usage: if (canOfferUniversalCoverage(cities, locations, 4, result)
 * ==================================================================
 * Given a set of cities, a list of what cities various hospitals can
 * cover, and a number of hospitals, returns whether or not it's
 * possible to provide coverage to all cities with the given number of
 * hospitals.  If so, one specific way to do this is handed back in the
 * result parameter.
 */
const int SENTINEL = 0;
bool canOfferUniversalCoverage(Set<string>& cities,
                               Vector< Set<string> >& locations,
                               int numHospitals,
                               Vector< Set<string> >& result);
bool canOfferUniversalCoverageHelper(Set<string>& cities, 
									 Vector< Set<string> >& locations, 
									 int numHospitals, Vector< Set<string> >& result, 
									 Vector<string>& myLocations);
bool weHaveAll(Set<string>& cities, Vector<string>& myLocations);
void printLocations(Vector< Set<string> >& result);


int main() {
    /* TODO: Add testing code here! */
	Set<string> cities;
	cities.add("A");
	cities.add("B");
	cities.add("C");
	cities.add("D");
	cities.add("E");
	cities.add("F");

	Set<string> L1;
	Set<string> L2;
	Set<string> L3;
	Set<string> L4;

	L1.add("A");
	L1.add("B");
	L1.add("C");

	L2.add("A");
	L2.add("C");
	L2.add("D");

	L3.add("B");
	L3.add("F");

	L4.add("C");
	L4.add("E");
	L4.add("F");

	Vector<Set<string>> locations;
	locations.add(L1);
	locations.add(L2);
	locations.add(L3);
	locations.add(L4);

	int numHospitals;
	
	while(true){
		numHospitals = getInteger("Enter number of hospitals [or 0 to stop]: ");
		if(numHospitals == SENTINEL) break;
		Vector<Set<string>> result;
		if(canOfferUniversalCoverage(cities, locations, numHospitals, result)){
			cout << "It is possible to provide all cities with " << numHospitals << " hospitals." <<endl;
			cout << "Hospitals: ";
			printLocations(result);
		}else{
			cout << "It is impossible to provide all cities with " << numHospitals << " hospitals." <<endl;
		}
		cout << endl;
	}
	
    return 0;
}

bool canOfferUniversalCoverage(Set<string>& cities, 
							   Vector< Set<string> >& locations, 
							   int numHospitals, 
							   Vector< Set<string> >& result){
	Vector<string> myLocations; // locations where we already have built hospitals.
	return canOfferUniversalCoverageHelper(cities, locations, numHospitals, result, myLocations);
}

bool canOfferUniversalCoverageHelper(Set<string>& cities, 
									 Vector< Set<string> >& locations, 
									 int numHospitals, Vector< Set<string> >& result, 
									 Vector<string>& myLocations){
	// base cases.
	if(numHospitals > locations.size() || numHospitals < 0) return false;
	if(numHospitals == 0) return weHaveAll(cities, myLocations); // we have hospitals in each city.				
    
	foreach(Set<string> locs in locations){
		// recursive backtracking.
		result.add(locs);
		foreach(string loc in locs){
			myLocations.add(loc);
		}
		Vector<Set<string>> otherLocations;
		foreach(Set<string> otherLocs in locations){
			if(otherLocs != locs) otherLocations.add(otherLocs);
		}
		if(canOfferUniversalCoverageHelper(cities, otherLocations, numHospitals - 1, result, myLocations)){
			return true;
		}
		result.remove(result.size() - 1);
		for(int i = 0 ; i < locs.size(); i++){
			myLocations.remove(myLocations.size() - 1);
		}
	}	
	return false;
}

bool weHaveAll(Set<string>& cities, Vector<string>& myLocations){
	Set<string> myCities;
	foreach(string s in myLocations){
		myCities.add(s);
	}
	return cities.isSubsetOf(myCities);
}


void printLocations(Vector< Set<string> >& result){
	cout << "{";
	foreach(Set<string> set in result){
		cout << set.toString();
	}
	cout << "}" << endl;
}
