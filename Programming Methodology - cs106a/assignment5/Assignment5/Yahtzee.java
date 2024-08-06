
/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while (nPlayers < 1) { // at least 1 player can play.
			nPlayers = dialog.readInt("Enter number of players");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private int[] rolls = new int[N_DICE];
	private int[][] scoreboard; // score in each category for each player.

	private void playGame() {
		/* You fill this in */
		scoreboard = new int[N_CATEGORIES][nPlayers];
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int player = 1; player <= nPlayers; player++) { // the number of total turns is categories * players
																	// (13 * Players).
				display.printMessage(playerNames[player - 1] + "'s turn! Click \"Roll Dice\" to roll again");
				display.waitForPlayerToClickRoll(player);
				for (int dice = 0; dice < N_DICE; dice++) { // 5 dice rolls.
					rolls[dice] = rgen.nextInt(1, 6);
				}
				display.displayDice(rolls);
				otherRolls();
				selectCategory(player); // after 3 rolls player selects category.
				resultAfterTurn(); // After three rolls by a player, program calculates the score (Updates score
				// card).
			}
		}
		findWinner();
	}

	private void selectCategory(int player) {
		display.printMessage("Select a category for this roll.");
		int c = display.waitForPlayerToSelectCategory(); // 'c' is the selected category.
		while (true) {
			if (cIsAvailable(c, player)) {
				addScore(player, c);
				break;
			} else {
				display.printMessage("Please, select available (not selected) category.");
				c = display.waitForPlayerToSelectCategory();
			}
		}
	}

	// Player can't select categories: upper score, lower, score, upper bonus and
	// total.
	// Also, the score in the category the player selects must be 0 (must not be
	// chosen before).
	private boolean cIsAvailable(int c, int player) {
		if (scoreboard[c - 1][player - 1] == 0 && c != UPPER_SCORE && c != LOWER_SCORE && c != UPPER_BONUS
				&& c != TOTAL) {
			return true;
		}
		return false;
	}

	// Add scores for each selected category.
	// 0 points if category condition is not completed.
	private void addScore(int player, int c) {
		int score = 0;
		if (c >= ONES && c <= SIXES) {
			for (int i = 0; i < N_DICE; i++) {
				if (rolls[i] == c) {
					score += c;
				}
			}
		} else if (c == THREE_OF_A_KIND) {
			if (categoryIsAvailable(c)) {
				for (int i = 0; i < N_DICE; i++) {
					score += rolls[i];
				}
			} else {
				score = 0;
			}
		} else if (c == FOUR_OF_A_KIND) {
			if (categoryIsAvailable(c)) {
				for (int i = 0; i < N_DICE; i++) {
					score += rolls[i];
				}
			} else {
				score = 0;
			}
		} else if (c == SMALL_STRAIGHT) {
			if (categoryIsAvailable(c)) {
				score = 30;
			} else {
				score = 0;
			}
		} else if (c == FULL_HOUSE) {
			if (categoryIsAvailable(c)) {
				score = 25;
			} else {
				score = 0;
			}
		} else if (c == LARGE_STRAIGHT) {
			if (categoryIsAvailable(c)) {
				score = 40;
			} else {
				score = 0;
			}
		} else if (c == YAHTZEE) {
			if (categoryIsAvailable(c)) {
				score = 50;
			} else {
				score = 0;
			}
		} else if (c == CHANCE) {
			for (int i = 0; i < N_DICE; i++) {
				score += rolls[i];
			}
		}
		display.updateScorecard(c, player, score);
		scoreboard[c - 1][player - 1] = score;
	}

	// Condition of each category.
	private boolean categoryIsAvailable(int c) {
		ArrayList<Integer> ones = new ArrayList<Integer>();
		ArrayList<Integer> twos = new ArrayList<Integer>();
		ArrayList<Integer> threes = new ArrayList<Integer>();
		ArrayList<Integer> fours = new ArrayList<Integer>();
		ArrayList<Integer> fives = new ArrayList<Integer>();
		ArrayList<Integer> sixes = new ArrayList<Integer>();

		// count number of each dice roll (1, 2, 3, 4, 5, 6).
		for (int i = 0; i < N_DICE; i++) {
			if (rolls[i] == 1) {
				ones.add(1);
			} else if (rolls[i] == 2) {
				twos.add(1);
			} else if (rolls[i] == 3) {
				threes.add(1);
			} else if (rolls[i] == 4) {
				fours.add(1);
			} else if (rolls[i] == 5) {
				fives.add(1);
			} else if (rolls[i] == 6) {
				sixes.add(1);
			}
		}

		if (c == THREE_OF_A_KIND) {
			if (ones.size() >= 3 || twos.size() >= 3 || threes.size() >= 3 || fours.size() >= 3 || fives.size() >= 3
					|| sixes.size() >= 3) {
				return true;
			}
		} else if (c == FOUR_OF_A_KIND) {
			if (ones.size() >= 4 || twos.size() >= 4 || threes.size() >= 4 || fours.size() >= 4 || fives.size() >= 4
					|| sixes.size() >= 4) {
				return true;
			}
		} else if (c == FULL_HOUSE) {
			if ((ones.size() == 3 || twos.size() == 3 || threes.size() == 3 || fours.size() == 3 || fives.size() == 3
					|| sixes.size() == 3)
					&& (ones.size() == 2 || twos.size() == 2 || threes.size() == 2 || fours.size() == 2
							|| fives.size() == 2 || sixes.size() == 2)) {
				return true;
			}
		} else if (c == SMALL_STRAIGHT) { // (1, 2, 3, 4), (2, 3, 4, 5), (3, 4, 5, 6)
			if ((ones.size() >= 1 && twos.size() >= 1 && threes.size() >= 1 && fours.size() >= 1)
					|| (fives.size() >= 1 && twos.size() >= 1 && threes.size() >= 1 && fours.size() >= 1)
					|| (sixes.size() >= 1 && fives.size() >= 1 && threes.size() >= 1 && fours.size() >= 1)) {
				return true;
			}
		} else if (c == LARGE_STRAIGHT) { // (1, 2, 3, 4, 5), (2, 3, 4, 5, 6)
			if ((ones.size() == 1 && twos.size() == 1 && threes.size() == 1 && fours.size() == 1 && fives.size() == 1)
					|| (fives.size() == 1 && twos.size() == 1 && threes.size() == 1 && fours.size() == 1
							&& sixes.size() == 1)) {
				return true;
			}
		} else if (c == YAHTZEE) {
			if (ones.size() == 5 || twos.size() == 5 || threes.size() == 5 || fours.size() == 5 || fives.size() == 5
					|| sixes.size() == 5) {
				return true;
			}
		}

		return false;
	}

	// second and third rolls.
	private void otherRolls() {
		for (int i = 0; i < 2; i++) {
			display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\". ");
			display.waitForPlayerToSelectDice();
			for (int dice = 0; dice < N_DICE; dice++) {
				if (display.isDieSelected(dice)) {
					rolls[dice] = rgen.nextInt(1, 6);
				}
			}
			display.displayDice(rolls);
		}
	}

	// Program counts upper score, lower score and total score after all players,
	// turn.
	private void resultAfterTurn() {
		for (int player = 0; player < nPlayers; player++) {
			int score = 0;
			// It counts upper score (Sum of the scores of first 6 categories).
			for (int c = 0; c < SIXES; c++) {
				score += scoreboard[c][player];
			}

			scoreboard[UPPER_SCORE - 1][player] = score;
			display.updateScorecard(UPPER_SCORE, player + 1, score);

			// It counts lower score (sum of the scores of the categories between upper
			// bonus and chance).
			score = 0;
			for (int c = UPPER_BONUS; c < CHANCE; c++) {
				score += scoreboard[c][player];
			}

			scoreboard[LOWER_SCORE - 1][player] = score;
			display.updateScorecard(LOWER_SCORE, player + 1, score);

			// total score is sum of upper score, lower score and upper bonus.
			scoreboard[TOTAL - 1][player] = scoreboard[UPPER_SCORE - 1][player] + scoreboard[LOWER_SCORE - 1][player]
					+ scoreboard[UPPER_BONUS - 1][player];
			display.updateScorecard(TOTAL, player + 1, scoreboard[TOTAL - 1][player]);

			// If a player collects more than 63 points in first six categories, she gets
			// bonus score (35 points).
			if (scoreboard[UPPER_SCORE - 1][player] > 63) {
				scoreboard[UPPER_BONUS - 1][player] = 35;
				display.updateScorecard(UPPER_BONUS, player + 1, 35);
			} else {
				scoreboard[UPPER_BONUS - 1][player] = 0;
				display.updateScorecard(UPPER_BONUS, player + 1, 0);
			}
		}
	}

	// after the game program finds winner.
	private void findWinner() {
		int winningScore = 0;
		int winner = 0;
		for (int player = 0; player < nPlayers; player++) {
			if (scoreboard[TOTAL - 1][player] > winningScore) {
				winningScore = scoreboard[TOTAL - 1][player];
				winner = player;
			}
		}

		display.printMessage("congratulations, " + playerNames[winner] + ", you are the winner with a total score of "
				+ winningScore + ".");
	}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
