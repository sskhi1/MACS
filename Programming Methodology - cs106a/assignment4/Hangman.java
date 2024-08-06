
/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.program.*;
import acm.util.*;

public class Hangman extends ConsoleProgram {
	RandomGenerator rgen = RandomGenerator.getInstance();
	private HangmanLexicon words = new HangmanLexicon();

	private int guesses = 8;

	private String SECRET;
	private String HIDDEN = "";

	// add canvas.
	private HangmanCanvas canvas;

	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}

	// Start of the game.
	private void initial() {
		println("Welcome to Hangman!");
		SECRET = randomString();
		HIDDEN = wordLook();
		canvas.displayWord(HIDDEN);
		println("You have " + guesses + " guesses left.");
	}

	// Program chooses secret string randomly from HangmanLexicon.
	private String randomString() {
		int secret_num = rgen.nextInt(0, words.getWordCount() - 1);
		String secret = words.getWord(secret_num);
		return secret;
	}

	// How the player sees hidden word.
	private String wordLook() {
		for (int i = 0; i < SECRET.length(); i++) {
			HIDDEN += "-";
		}
		println("The word now looks like this: " + HIDDEN);
		return HIDDEN;
	}

	public void run() {
		playHangman();
	}

	private void playHangman() {
		canvas.reset();
		canvas.displayWord(HIDDEN);
		initial();
		play();
	}

	// Main part of the game. this part continues until the player loses or wins the
	// game.
	private void play() {
		while (!HIDDEN.equals(SECRET) && guesses > 0) {
			String guess = readLine("Your guess: ");
			if (guess.length() != 1) {
				println("Please, enter a Letter.");
			} else if (!Character.isLetter(guess.charAt(0))) { // firstly, we check if the entered symbol of the player
																// is a letter.
				println("Please, enter a Letter.");
			} else {
				guess = guess.toUpperCase();
				checkguess(guess, SECRET);
				canvas.displayWord(HIDDEN);
				if (HIDDEN.equals(SECRET)) {
					println("You guessed the word: " + SECRET);
					println("You win.");
					canvas.winMessage();
					break;
				} else if (guesses == 0) {
					println("You are completely hung.");
					println("The word was " + SECRET);
					println("You lose.");
					break;
				}
				println("The word now looks like this: " + HIDDEN);
				if (guesses != 1) {
					println("You have " + guesses + " guesses left.");
				} else {
					println("You have only " + guesses + " guess left.");
				}
			}
		}
	}

	// After every entered letter program checks whether the secret word contains
	// that letter or not. If not, player will have 1 less guess left.
	private void checkguess(String entered, String real) {
		if (real.contains(entered)) {
			println("That guess is correct!");
			for (int i = 0; i < real.length(); i++) {
				if (entered.charAt(0) == real.charAt(i)) {
					HIDDEN = HIDDEN.substring(0, i) + entered + HIDDEN.substring(i + 1); // e.g. --- + character + ----
				}
			}
		} else {
			println("There are no " + entered + "'s in the word.");
			guesses--;
			canvas.noteIncorrectGuess(entered.charAt(0)); // Display incorrect guesses on canvas.
		}
	}

}
