
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class extension extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	private int bricks_left = NBRICK_ROWS * NBRICKS_PER_ROW;

	private RandomGenerator rgen = RandomGenerator.getInstance();

	private double vx, vy; // X and Y velocity of the ball.

	private GObject collider;

	private GOval ball;

	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	AudioClip geo = MediaTools.loadAudioClip("Ra Drosha mogvca mishama.au");
	AudioClip fr = MediaTools.loadAudioClip("Le-Festin-Camille-Ratatouille-Soundtrack.au");
	AudioClip win = MediaTools.loadAudioClip("Congratulations-Sound-Effect.au");
	AudioClip lose = MediaTools.loadAudioClip("Sad-Music-Sound-Effect-HD-.au");

	private int TIME = 15; // delay

	private static int Nlife = NTURNS;
	
	GImage rickroll = new GImage("rickroll (1).jpg");

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		setBackground(Color.BLACK);
		initialText();
		waitForClick();
		removeAll();
		chooseGameMode();
		addMouseListeners();
		play();

	}

	private void play() {
		waitForClick();
		if (classic_play) {
			playclassic();
		} else if (french_play) {
			playfrench();
		} else if (geo_play) {
			playgeo();
		}
	}

	boolean classic_play;
	boolean geo_play;
	boolean french_play;

	public void mousePressed(MouseEvent e) {
		if (rectclassic.contains(e.getX(), e.getY())) {
			classic_play = true;
		} else if (rectgeo.contains(e.getX(), e.getY())) {
			geo_play = true;
		} else if (rectfrench.contains(e.getX(), e.getY())) {
			french_play = true;
		}
	}

	private GRect rectclassic;
	private GRect rectgeo;
	private GRect rectfrench;

	// player selects game mode.
	private void chooseGameMode() {
		rectclassic = new GRect(0, 0, getWidth() / 2, getHeight() / 2);
		rectclassic.setFilled(true);
		rectclassic.setFillColor(Color.black);
		add(rectclassic);
		rectgeo = new GRect(0, getHeight() / 2, getWidth(), getHeight() / 2);
		rectgeo.setFilled(true);
		rectgeo.setFillColor(Color.red);
		add(rectgeo);
		rectfrench = new GRect(getWidth() / 2, 0, getWidth() / 2, getHeight() / 2);
		rectfrench.setFilled(true);
		rectfrench.setFillColor(Color.BLUE);
		add(rectfrench);
		GLabel classic = new GLabel("CLASSIC");
		classic.setLocation(getWidth() / 4 - classic.getWidth(), getHeight() / 4);
		classic.setFont("ITALIC-20");
		classic.setColor(Color.CYAN);
		add(classic);
		GLabel geo = new GLabel("GEORGIAN");
		geo.setLocation(getWidth() / 2 - geo.getWidth(), 3 * getHeight() / 4);
		geo.setFont("ITALIC-20");
		geo.setColor(Color.white);
		add(geo);
		GLabel french = new GLabel("FRENCH");
		french.setLocation(3 * getWidth() / 4 - french.getWidth(), getHeight() / 4);
		french.setFont("ITALIC-20");
		french.setColor(Color.red);
		add(french);
	}

	GOval life1;
	GOval life2;
	GOval life3;
	GLabel lives;

	// 3 lives.
	private void addLives() {
		lives = new GLabel("LIVES:");
		lives.setColor(Color.white);
		lives.setFont("ITALIC-15");
		lives.setLocation(10, 30);
		add(lives);
		life1 = new GOval(lives.getX() + lives.getWidth() + 2 * BALL_RADIUS - 15, 2 * BALL_RADIUS - 8, 2 * BALL_RADIUS,
				2 * BALL_RADIUS);
		life1.setFilled(true);
		life1.setColor(Color.blue);
		life1.setFillColor(Color.blue);
		add(life1);
		life2 = new GOval(lives.getX() + lives.getWidth() + 2 * BALL_RADIUS - 15 + 2 * BALL_RADIUS + 3,
				2 * BALL_RADIUS - 8, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		life2.setFilled(true);
		life2.setColor(Color.blue);
		life2.setFillColor(Color.blue);
		add(life2);
		life3 = new GOval(lives.getX() + lives.getWidth() + 2 * BALL_RADIUS - 15 + 4 * BALL_RADIUS + 6,
				2 * BALL_RADIUS - 8, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		life3.setFilled(true);
		life3.setColor(Color.blue);
		life3.setFillColor(Color.blue);
		add(life3);
	}

	private void playclassic() {
		removeAll();
		addLives();
		drawBricks();
		addPaddle();
		addMouseListeners();
		addBall();
		addScore();

		moveBall();
	}

	private void playfrench() {
		removeAll();
		addLives();
		drawfrBricks();
		addPaddle();
		addMouseListeners();
		addBall();
		addScore();

		movefrBall();
	}

	private void playgeo() {
		removeAll();
		addLives();
		drawgeoBricks();
		addPaddle();
		addMouseListeners();
		addBall();
		addScore();

		movegeoBall();
	}

	GLabel score;
	private int points = 0;

	private void addScore() {
		score = new GLabel("SCORE: " + points);
		score.setLocation(10, getHeight() - 15);
		score.setColor(Color.white);
		score.setFont("ITALIC-15");
		add(score);
	}

	// draw Georgian mode bricks.
	private void drawgeoBricks() {
		int counter = 0; // this int helps us define colors of bricks.
		double X = BRICK_X_OFFSET;
		double Y = BRICK_Y_OFFSET;
		int bricksperrow = 7;
		int rows = 7;
		double brickwidth = getWidth() / 8;
		double brickheight = BRICK_HEIGHT + 5;
		for (int i = 0; i < rows; i++) { // this 'for' is responsible for brick rows.
			for (int j = 0; j < bricksperrow; j++) { // this 'for' is responsible for each brick of 1 row.
				GRect brick = new GRect(X + BRICK_WIDTH * j, Y, brickwidth, brickheight);
				brick.setFilled(true);

				if (counter < bricksperrow) {
					brick.setColor(Color.white);
					brick.setFillColor(Color.white);
					if (counter == 1 || counter == 3 || counter == 5) {
						brick.setColor(Color.red);
						brick.setFillColor(Color.red);
					}
				} else if (counter < 2 * bricksperrow) {
					brick.setColor(Color.red);
					brick.setFillColor(Color.red);
				} else if (counter < 3 * bricksperrow) {
					brick.setColor(Color.white);
					brick.setFillColor(Color.white);
					if (counter == 15 || counter == 17 || counter == 19) {
						brick.setColor(Color.red);
						brick.setFillColor(Color.red);
					}
				} else if (counter < 4 * bricksperrow) {
					brick.setColor(Color.red);
					brick.setFillColor(Color.red);
				} else if (counter < 5 * bricksperrow) {
					brick.setColor(Color.white);
					brick.setFillColor(Color.white);
					if (counter == 29 || counter == 31 || counter == 33) {
						brick.setColor(Color.red);
						brick.setFillColor(Color.red);
					}
				} else if (counter < 6 * bricksperrow) {
					brick.setColor(Color.red);
					brick.setFillColor(Color.red);
				} else if (counter < 7 * bricksperrow) {
					brick.setColor(Color.white);
					brick.setFillColor(Color.white);
					if (counter == 43 || counter == 45 || counter == 47) {
						brick.setColor(Color.red);
						brick.setFillColor(Color.red);
					}
				}
				counter++;
				add(brick);
				X += 20;
			}
			X -= 20 * bricksperrow;
			Y += brickheight + 4; // "Jump" on the following row.
		} // "Jump" on the following row.
	}

	// draw french mode bricks.
	private void drawfrBricks() {
		int counter = 0;
		double X = BRICK_X_OFFSET + BRICK_WIDTH / 2 + BRICK_SEP / 2;
		double Y = BRICK_Y_OFFSET;
		int bricks = NBRICKS_PER_ROW - 1;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < bricks; j++) {
				GRect brick = new GRect(X, Y + BRICK_HEIGHT * j, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);

				if (counter < 3 * bricks) {
					brick.setColor(Color.blue);
					brick.setFillColor(Color.blue);
				} else if (counter < 6 * bricks) {
					brick.setColor(Color.white);
					brick.setFillColor(Color.white);
				} else if (counter < 9 * bricks) {
					brick.setColor(Color.red);
					brick.setFillColor(Color.red);
				}
				counter++;
				add(brick);
				Y += BRICK_HEIGHT;
			}
			Y -= bricks * BRICK_HEIGHT;
			X += BRICK_WIDTH + BRICK_SEP;
		}
	}

	// At the beginning this text appears on the screen (It disappears after a
	// click).
	private void initialText() {
		GLabel text = new GLabel("CLICK TO PLAY");
		text.setFont("ITALIC-40");
		text.setLocation(getWidth() / 2 - text.getWidth() / 2, getHeight() / 2);
		text.setColor(Color.BLUE);
		add(text);
	}

	// draw bricks
	private double BRICK_X_OFFSET = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / 2;

	private void drawBricks() {
		int counter = 0; // this int helps us define colors of bricks.
		double X = BRICK_X_OFFSET;
		double Y = BRICK_Y_OFFSET;
		for (int i = 0; i < NBRICK_ROWS; i++) { // this 'for' is responsible for brick rows.
			for (int j = 0; j < NBRICKS_PER_ROW; j++) { // this 'for' is responsible for each brick of 1 row.
				GRect brick = new GRect(X + BRICK_WIDTH * j, Y, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);

				if (counter < 2 * NBRICKS_PER_ROW) {
					brick.setColor(Color.RED);
					brick.setFillColor(Color.RED);
				} else if (counter < 4 * NBRICKS_PER_ROW) {
					brick.setColor(Color.ORANGE);
					brick.setFillColor(Color.orange);
				} else if (counter < 6 * NBRICKS_PER_ROW) {
					brick.setColor(Color.YELLOW);
					brick.setFillColor(Color.YELLOW);
				} else if (counter < 8 * NBRICKS_PER_ROW) {
					brick.setColor(Color.GREEN);
					brick.setFillColor(Color.green);
				} else {
					brick.setColor(Color.CYAN);
					brick.setFillColor(Color.CYAN);
				}
				counter++;
				add(brick);
				X += BRICK_SEP;
			}
			X -= BRICK_SEP * NBRICKS_PER_ROW;
			Y += BRICK_HEIGHT + BRICK_SEP; // "Jump" on the following row.
		}
	}

	// add paddle
	private GRect paddle;

	private void addPaddle() {
		paddle = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.GRAY);
		add(paddle);
	}

	// move paddle
	public void mouseMoved(MouseEvent e) {
		if (paddle != null) {
			paddle.setLocation(e.getX() - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
			// we need to check if paddle moves out of borders.
			if (e.getX() <= PADDLE_WIDTH / 2) {
				paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
			} else if (e.getX() >= getWidth() - PADDLE_WIDTH / 2) {
				paddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
			}
		}
	}

	// add ball
	private void addBall() {
		ball = new GOval(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.blue);
		ball.setFillColor(Color.blue);
		add(ball);
	}

	private void moveBall() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		vy = 5.0;
		waitForClick(); // game begins after 1 click.
		while (true) {
			ball.move(vx, vy);
			checkWalls();
			checkObject();
			pause(TIME);

			if (Nlife == 0) { // Losing condition
				display_message();
				addFinalScore();
				lose.play();
				break;
			} else if (bricks_left == 0) { // Winning condition.
				display_win_message();
				addFinalScore();
				win.play();
				break;
			}
		}
	}

	// move ball in FRENCH mode.
	private void movefrBall() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		vy = 5.0;
		waitForClick(); // game begins after 1 click.
		bricks_left = 81;
		fr.play();
		while (true) {
			ball.move(vx, vy);
			checkWalls();
			checkObject();
			pause(TIME);
			if (Nlife == 0) { // Losing condition
				fr.stop();
				display_message();
				addFinalScore();
				lose.play();
				break;
			} else if (bricks_left == 0) { // Winning condition.
				fr.stop();
				display_win_message();
				addFinalScore();
				win.play();
				break;

			}
		}
	}

	// move ball in GEORGIAN mode.
	private void movegeoBall() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		vy = 5.0;
		waitForClick(); // game begins after 1 click.
		bricks_left = 49;
		geo.play();
		while (true) {
			ball.move(vx, vy);
			checkWalls();
			checkObject();
			pause(TIME);
			if (Nlife == 0) { // Losing condition
				display_message();
				addFinalScore();
				geo.stop();
				lose.play();
				break;
			} else if (bricks_left == 0) { // Winning condition.
				geo.stop();
				display_win_message();
				addFinalScore();
				win.play();
				break;
			}
		}
	}

	// ball changes direction if it hits a wall.
	// if it hits bottom "wall" player loses 1 of his/her 3 lives.
	private void checkWalls() {
		if (ball.getX() + 2 <= 0) {
			vx = -vx;
		} else if (ball.getX() + 2 * BALL_RADIUS - 2 >= getWidth()) {
			vx = -vx;
		} else if (ball.getY() + 2 <= 0) {
			vy = -vy;
		} else if (ball.getY() + 2 * BALL_RADIUS  >= getHeight()) {
			Nlife--;
			ball.setLocation(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS); // ball gets placed on the
																							// centre of the canvas.
			if (Nlife == 2) {
				remove(life3);
			} else if (Nlife == 1) {
				remove(life2);
			}
			if (Nlife >= 1) {
				waitForClick(); // if a player has 1 or 2 lives left he/she starts new try after a click.
			}
		}
	}

	private void checkObject() {
		GObject collider = getCollidingObject();
		if (collider != null) {
			if (collider == paddle) {
				ball.setLocation(ball.getX(), paddle.getY() - 2 * BALL_RADIUS - 3); // To get rid of the bug (ball
																					// getting stuck in the paddle).
				vy = -vy;
				vx = rgen.nextDouble(-3.0, 3.0);
				bounceClip.play();
			} else if (collider != paddle && collider != lives && collider != life1 && collider != life2
					&& collider != life3 && collider != score) {
				remove(collider);
				bricks_left--;
				vy = -vy;
				if (bricks_left % 10 == 0) {
					vy += 0.5;
				}
				bounceClip.play();
				if (bricks_left > 70) {
					points++;
					if (score != null) {
						score.setLabel("SCORE: " + points);
					}
				} else if (bricks_left > 35) {
					points += 5;
					if (score != null) {
						score.setLabel("SCORE: " + points);
					}
				} else {
					points += 10;
					if (score != null) {
						score.setLabel("SCORE: " + points);
					}
				}
			}
		}
	}

	// I checked 8 sides of the ball. This private GObject returns an object which
	// gets hit with the ball.
	// for each case I subtracted/added 2/3 pixels to get rid of bugs.
	private GObject getCollidingObject() {
		if (getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS + 2) != null) {
			collider = getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS + 2);
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS + 3, ball.getY() + BALL_RADIUS + 2) != null) {
			collider = getElementAt(ball.getX() + 2 * BALL_RADIUS + 3, ball.getY() + BALL_RADIUS + 2);
		} else if (getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() - 1) != null) {
			collider = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() - 1);
		} else if (getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + 2 * BALL_RADIUS + 2) != null) {
			collider = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + 2 * BALL_RADIUS + 2);
		} else if (getElementAt(ball.getX() - 1 + BALL_RADIUS / 3, ball.getY() - 1 + BALL_RADIUS / 3) != null) {
			collider = getElementAt(ball.getX() - 1 + BALL_RADIUS / 3, ball.getY() - 1 + BALL_RADIUS / 3);
		} else if (getElementAt(ball.getX() - 1 + BALL_RADIUS / 3,
				ball.getY() - 1 + 2 * BALL_RADIUS + 3 + BALL_RADIUS / 3) != null) {
			collider = getElementAt(ball.getX() - 1 + BALL_RADIUS / 3,
					ball.getY() - 1 + 2 * BALL_RADIUS + 3 + BALL_RADIUS / 3);
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS + 2 - BALL_RADIUS / 3,
				ball.getY() - 1 + BALL_RADIUS / 3) != null) {
			collider = getElementAt(ball.getX() + 2 * BALL_RADIUS + 2 - BALL_RADIUS / 3,
					ball.getY() - 1 + BALL_RADIUS / 3);
		} else if (getElementAt(ball.getX() + 2 * BALL_RADIUS + 2 - BALL_RADIUS / 3,
				ball.getY() - 1 + 2 * BALL_RADIUS + 3 - BALL_RADIUS / 3) != null) {
			collider = getElementAt(ball.getX() + 2 * BALL_RADIUS + 2 - BALL_RADIUS / 3,
					ball.getY() - 1 + 2 * BALL_RADIUS + 3 - BALL_RADIUS / 3);

		} else {
			collider = null;
		}
		return collider;
	}

	// message "GAME OVER!" appears on the screen if you lose.
	private void display_message() {
		removeAll();
		setBackground(Color.black);
		GLabel lose = new GLabel("GAME OVER!");
		lose.setFont("ITALIC-40");
		lose.setLocation(getWidth() / 2 - lose.getWidth() / 2, getHeight() / 2);
		lose.setColor(Color.DARK_GRAY);
		add(lose);
	}

	// message "YOU WIN!!!" appears on the screen if you win.
	private void display_win_message() {
		removeAll();
		setBackground(Color.green);
		rickroll.setSize(getWidth(), getHeight());
		add(rickroll, 0, 0);
		GLabel win = new GLabel("YOU WIN!!!");
		win.setFont("ITALIC-40");
		win.setLocation(getWidth() / 2 - win.getWidth() / 2, getHeight() / 2);
		win.setColor(Color.YELLOW);
		add(win);
	}

	// display final score of the game.
	private void addFinalScore() {
		GLabel end = new GLabel("Score: " + points);
		end.setLocation(getWidth() / 2 - end.getWidth() / 2, 3 * getHeight() / 4);
		end.setFont("ITALIC-16");
		end.setColor(Color.BLUE);
		add(end);
	}
}
