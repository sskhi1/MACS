/*
 * File: Boggle.cpp
 * ----------------
 * Name: [TODO: enter name here]
 * Section: [TODO: enter section leader here]
 * This file is the main starter file for Assignment #4, Boggle.
 * [TODO: extend the documentation]
 */

#include <iostream>
#include "gboggle.h"
#include "grid.h"
#include "gwindow.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include "strlib.h"
using namespace std;

/* Constants */

const int BOGGLE_WINDOW_WIDTH = 650;
const int BOGGLE_WINDOW_HEIGHT = 350;

const string STANDARD_CUBES[16]  = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};
 
const string BIG_BOGGLE_CUBES[25]  = {
    "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
    "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
    "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
    "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
    "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

/* Function prototypes */

void welcome();
void giveInstructions();
void initializeBoggle();
Grid<char> drawInitBoggleBoard(int size);
Vector<string> getLettersVec(int size);
Vector<string> computerGenerateLetters(int size);
void getRandomLetters(Vector<string> &vec);
void boardAddLetters(Grid<char> &board, Vector<string> &letters);
void displayBoard(Grid<char> &board);
void myTurn(Grid<char> &board, Lexicon &lex, Set<string> &myWords);
void computerTurn(Grid<char> &board, Lexicon &lex, Set<string> &myWords);
bool wordIsOnTheBoard(string word, Grid<char> &board, int row, int col, bool haveFirst, Set<int> &path);
void findRemainingWords(Player &comp, Grid<char> &board, Lexicon &lex, Set<string> &myWords, string soFar, int row, int col, Set<int> computerPath);

/* Main program */

int main() {
    GWindow gw(BOGGLE_WINDOW_WIDTH, BOGGLE_WINDOW_HEIGHT);
    initGBoggle(gw);
    welcome();
    giveInstructions();
	cout << endl;
	initializeBoggle();
	
    return 0;
}

/*
 * Function: welcome
 * Usage: welcome();
 * -----------------
 * Print out a cheery welcome message.
 */

void welcome() {
    cout << "Welcome!  You're about to play an intense game ";
    cout << "of mind-numbing Boggle.  The good news is that ";
    cout << "you might improve your vocabulary a bit.  The ";
    cout << "bad news is that you're probably going to lose ";
    cout << "miserably to this little dictionary-toting hunk ";
    cout << "of silicon.  If only YOU had a gig of RAM..." << endl << endl;
}

/*
 * Function: giveInstructions
 * Usage: giveInstructions();
 * --------------------------
 * Print out the instructions for the user.
 */

void giveInstructions() {
    cout << endl;
    cout << "The boggle board is a grid onto which I ";
    cout << "I will randomly distribute cubes. These ";
    cout << "6-sided cubes have letters rather than ";
    cout << "numbers on the faces, creating a grid of ";
    cout << "letters on which you try to form words. ";
    cout << "You go first, entering all the words you can ";
    cout << "find that are formed by tracing adjoining ";
    cout << "letters. Two letters adjoin if they are next ";
    cout << "to each other horizontally, vertically, or ";
    cout << "diagonally. A letter can only be used once ";
    cout << "in each word. Words must be at least four ";
    cout << "letters long and can be counted only once. ";
    cout << "You score points based on word length: a ";
    cout << "4-letter word is worth 1 point, 5-letters ";
    cout << "earn 2 points, and so on. After your puny ";
    cout << "brain is exhausted, I, the supercomputer, ";
    cout << "will find all the remaining words and double ";
    cout << "or triple your paltry score." << endl << endl;
    cout << "Hit return when you're ready...";
    getLine();
}

void initializeBoggle(){
	Lexicon lex("EnglishWords.dat");
	// Game loop.
	while(true){
		string prompt = getLine("Would you like to play game Boggle? Enter 'No' to stop playing. ");
		if(prompt == "No") break;
		int size;
		while(true){
			size = getInteger("Enter size of the board. 4 or 5: ");
			if(size != 4 && size != 5){
				cout << "Invalid board size. Try again." << endl;
			}else{
				break;
			}
		}
		Grid<char> board = drawInitBoggleBoard(size);
		Set<string> myWords;

		myTurn(board, lex, myWords);
		cout << endl;
		cout << "Computer will wind all the remaining words now." << endl;
		cout << endl;
		computerTurn(board, lex, myWords);
		cout << endl;
	}
	cout << "The End" << endl;
}

Grid<char> drawInitBoggleBoard(int size){
	drawBoard(size, size);
	Vector<string> letters;
	string configuration = getLine("Would you like to enter board letters? enter 'y' for yes, otherwise computer will generate letters. ");
	if(configuration == "y"){
		letters = getLettersVec(size);
	}else{
		letters = computerGenerateLetters(size);
	}
	Grid<char> board(size, size);
	boardAddLetters(board, letters);
	displayBoard(board);
	return board;
}

// player enters letters for the board.
Vector<string> getLettersVec(int size){
	Vector<string> result;
	string myLetters;
	while(true){
		myLetters = getLine("please enter letters for board: ");
		if(myLetters.length() >= size * size){
			break;
		}else{
			cout << "Try again..." << endl;
		}
	}
	string finalLetters = myLetters.substr(0, size * size);
	finalLetters = toUpperCase(finalLetters);
	
	for(int i = 0; i < finalLetters.length(); i++){
		result.add(finalLetters.substr(i, 1));
	}
	return result;
}

// computer puts random letters on the board.
Vector<string> computerGenerateLetters(int size){
	Vector<string> result;
	if(size == 4){
	
		for(int i = 0 ; i < size * size; i++){
			result.add(STANDARD_CUBES[i]);
		}
	
		for(int i = 0; i < result.size(); i++){
			int r = randomInteger(0, result.size() - 1);
			string temp = result[i];
			result[i] = result[r];
			result[r] = temp;
		}
			getRandomLetters(result);
			return result;
	}else{
		for(int i = 0 ; i < size * size; i++){
			result.add(BIG_BOGGLE_CUBES[i]);
		}
	
		for(int i = 0; i < result.size(); i++){
			int r = randomInteger(0, result.size() - 1);
			string temp = result[i];
			result[i] = result[r];
			result[r] = temp;
		}
		getRandomLetters(result);
		return result;
	}

}

// shuffle.
void getRandomLetters(Vector<string> &vec){
	for(int i = 0; i < vec.size(); i++){
		int rand = randomInteger(0, vec[i].length() - 1);
		vec[i] = vec[i][rand];
	}
}

void boardAddLetters(Grid<char> &board, Vector<string> &letters){
	int idx = 0;
	for(int i = 0; i < board.numRows(); i++) {
        for(int j = 0; j < board.numCols(); j++) {
            board[i][j] = letters[idx][0];
			idx++;
        }
    }
}

void displayBoard(Grid<char> &board){
	for(int i = 0; i < board.numRows(); i++){
		for(int j = 0; j < board.numCols(); j++){
			labelCube(i, j, board[i][j]);
		}
	}
}


// human player's turn.
void myTurn(Grid<char> &board, Lexicon &lex, Set<string> &myWords){
	Player me = HUMAN;
	while(true){
		Set<int> path;	// helps us not to use same cubes while finding a word on the board.
		string word = getLine("Enter a word from the board: ");
		if(word.empty()) break;
		word = toUpperCase(word);
		if(word.size() < 4 || !lex.contains(word)){
			cout << "This word doesn't exist or has less than 4 letters." << endl;
		}else if(myWords.contains(word)){
			cout << "You already have this word." << endl;
		}else if(wordIsOnTheBoard(word, board, 0, 0, false, path)){
			myWords.add(word);
			recordWordForPlayer(word, me);
			pause(500);
			// dehighlight cubes after a short time.
			for(int i = 0; i < board.numRows(); i++){
				for(int j = 0; j < board.numCols(); j++){
					highlightCube(i, j, false);
				}
			}
		}else{
			cout << "Word is not on the board." << endl;
		}
	}
}

// check if is it possible to find the word on the board. (if it is, highlight cubes).
bool wordIsOnTheBoard(string word, Grid<char> &board, int row, int col, bool haveFirst, Set<int> &path){
	if(word.length() == 0) return true;
	
	// find first letter.
	if(!haveFirst){
		for(int i = 0; i < board.numRows(); i++){
			for(int j = 0; j < board.numCols(); j++){
				if(board[i][j] == word[0]){
					int coordinate = i * 10 + j;
					path.add(coordinate);
					if(wordIsOnTheBoard(word.substr(1), board, i, j, true, path)){
						highlightCube(i, j, true);
						return true;
					}
					path.remove(coordinate);
				}
			}
		}
	// find other letters than first.
	}else{
		for(int i = row - 1; i < row + 2; i++){
			for(int j = col - 1; j < col + 2; j++){
				if((i != row || j != col) && board.inBounds(i, j) && board[i][j] == word[0]){
					if(!path.contains(i * 10 + j)){
						path.add(i * 10 + j);
						if(wordIsOnTheBoard(word.substr(1), board, i, j, true, path)){
							highlightCube(i, j, true);
							return true;
						}
						path.remove(i * 10 + j);
					}
				}
			}
		}
	}
	return false;
}

// computer finds all words on the board.
void computerTurn(Grid<char> &board, Lexicon &lex, Set<string> &myWords){
	Player comp = COMPUTER;
	Set<int> computerPath; // helps computer not to use same cubes while finding a word on the b
	for (int i = 0; i < board.numRows(); i++){
        for (int j = 0; j < board.numCols(); j++){
			computerPath.add(i * 10 + j);
			findRemainingWords(comp, board, lex, myWords, "", i, j, computerPath);
			computerPath.remove(i * 10 + j);
        }
    }
	cout << "That's all..." << endl;
}

void findRemainingWords(Player &comp, Grid<char> &board, Lexicon &lex, Set<string> &myWords, string soFar, int row, int col, Set<int> computerPath){
	if(!lex.containsPrefix(soFar)) return;
	if(soFar.length() > 3){
		if(lex.contains(soFar) && !myWords.contains(soFar)){
			recordWordForPlayer(soFar, comp);
			myWords.add(soFar);
		}
	}
	for(int i = row - 1; i < row + 2; i++){
		for(int j = col - 1; j < col + 2; j++){
			if(!board.inBounds(i, j) || (i == row && j == col) || computerPath.contains(i * 10 + j)){
					continue;
			}else{
				computerPath.add(i * 10 + j);
				findRemainingWords(comp, board, lex, myWords, soFar + board[i][j], i, j, computerPath);
				computerPath.remove(i * 10 + j);
			}
		}
	}
}



