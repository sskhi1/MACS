
/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

	/** Resets the display so that only the scaffold appears */
	public void reset() {
		/* You fill this in */
		removeAll();
		drawInit();
	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	GLabel display;

	public void displayWord(String word) {
		/* You fill this in */
		if (display != null) {
			remove(display);
		}
		display = new GLabel(word);
		display.setLocation(getWidth() / 2 - BEAM_LENGTH, getHeight() / 2 + SCAFFOLD_HEIGHT / 2 + 30);
		display.setFont("ITALIC-bold-25");
		add(display);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user. Calling
	 * this method causes the next body part to appear on the scaffold and adds the
	 * letter to the list of incorrect guesses that appears at the bottom of the
	 * window.
	 */
	GLabel incorrect;
	private int guesses = 8;
	private String Lguesses = ""; // wrong guesses

	public void noteIncorrectGuess(char letter) {
		/* You fill this in */
		if (incorrect != null) {
			remove(incorrect);
		}
		Lguesses += letter;
		guesses--;
		draw();
		incorrect = new GLabel(Lguesses);
		incorrect.setLocation(getWidth() / 2 - BEAM_LENGTH, getHeight() / 2 + SCAFFOLD_HEIGHT / 2 + 50);
		incorrect.setFont("ITALIC-15");
		add(incorrect);
	}

	private void draw() {
		switch (guesses) {
		case 8:
			break;
		case 7:
			drawHead();
			break;
		case 6:
			drawBody();
			break;
		case 5:
			drawLeftArm();
			break;
		case 4:
			drawRightArm();
			break;
		case 3:
			drawLeftLeg();
			break;
		case 2:
			drawRightLeg();
			break;
		case 1:
			drawLeftFoot();
			break;
		case 0:
			drawRightFoot();
			displayLoseMessage();
			break;
		default:
			break;
		}
	}
	
	public void winMessage() {
		GLabel win = new GLabel("You guessed the word! You win!");
		win.setFont("ITALIC-bold-20");
		win.setLocation(getWidth() / 2 - win.getWidth() / 2, 30);
		add(win);
	}

	private void displayLoseMessage() {
		GLabel lose = new GLabel("You're completely hung. You lose.");
		lose.setFont("ITALIC-bold-20");
		lose.setLocation(getWidth() / 2 - lose.getWidth() / 2, 30);
		add(lose);
	}

	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

	private void drawInit() {
		GLine scaffold = new GLine(getWidth() / 2 - BEAM_LENGTH, getHeight() / 2 - SCAFFOLD_HEIGHT / 2,
				getWidth() / 2 - BEAM_LENGTH, getHeight() / 2 + SCAFFOLD_HEIGHT / 2);
		add(scaffold);
		GLine beam = new GLine(getWidth() / 2 - BEAM_LENGTH, getHeight() / 2 - SCAFFOLD_HEIGHT / 2, getWidth() / 2,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2);
		add(beam);
		GLine rope = new GLine(getWidth() / 2, getHeight() / 2 - SCAFFOLD_HEIGHT / 2, getWidth() / 2,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH);
		add(rope);
	}

	private void drawHead() {
		GOval head = new GOval(getWidth() / 2 - HEAD_RADIUS, getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH,
				2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		add(head);
	}

	private void drawBody() {
		GLine body = new GLine(getWidth() / 2, getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS,
				getWidth() / 2, getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(body);
	}

	private void drawLeftArm() {
		GLine arm = new GLine(getWidth() / 2,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 - UPPER_ARM_LENGTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(arm);
		GLine lowerArm = new GLine(getWidth() / 2 - UPPER_ARM_LENGTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 - UPPER_ARM_LENGTH, getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS
						+ ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		;
		add(lowerArm);

	}

	private void drawRightArm() {
		GLine arm = new GLine(getWidth() / 2,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 + UPPER_ARM_LENGTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD);
		add(arm);
		GLine lowerArm = new GLine(getWidth() / 2 + UPPER_ARM_LENGTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + ARM_OFFSET_FROM_HEAD,
				getWidth() / 2 + UPPER_ARM_LENGTH, getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS
						+ ARM_OFFSET_FROM_HEAD + LOWER_ARM_LENGTH);
		add(lowerArm);
	}

	private void drawLeftLeg() {
		GLine hip = new GLine(getWidth() / 2,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 - HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(hip);
		GLine leg = new GLine(getWidth() / 2 - HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 - HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(leg);
	}

	private void drawRightLeg() {
		GLine hip = new GLine(getWidth() / 2,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 + HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH);
		add(hip);
		GLine leg = new GLine(getWidth() / 2 + HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH,
				getWidth() / 2 + HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(leg);
	}

	private void drawLeftFoot() {
		GLine foot = new GLine(getWidth() / 2 - HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH,
				getWidth() / 2 - HIP_WIDTH - FOOT_LENGTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(foot);
	}

	private void drawRightFoot() {
		GLine foot = new GLine(getWidth() / 2 + HIP_WIDTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH,
				getWidth() / 2 + HIP_WIDTH + FOOT_LENGTH,
				getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH + 2 * HEAD_RADIUS + BODY_LENGTH + LEG_LENGTH);
		add(foot);
	}

}
