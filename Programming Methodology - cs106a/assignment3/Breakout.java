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

public class Breakout extends GraphicsProgram {

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
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

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
	
	private double vx, vy;  // X and Y velocity of the ball.
	
	private GObject collider;
	
	private GOval ball;
	
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	
	private int TIME = 15;    //delay
	
	private static int Nlife = NTURNS; 
	
	
	/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		initialText();
		waitForClick();
		removeAll();
		
		drawBricks();
		addPaddle();
		addMouseListeners();
		addBall();
		
		moveBall();
		
	}
	
	//At the beginning this text appears on the screen (It disappears after a click).
	private void initialText() {
		GLabel text = new GLabel("CLICK TO PLAY");
		text.setFont("ITALIC-40");
		text.setLocation(getWidth()/2 - text.getWidth()/2, getHeight()/2);
		text.setColor(Color.BLUE);
		add(text);
	}
	
	//draw bricks
	private double BRICK_X_OFFSET = (WIDTH - NBRICKS_PER_ROW * BRICK_WIDTH - (NBRICKS_PER_ROW-1)*BRICK_SEP)/2;
	private void drawBricks(){
		int counter = 0;  //this int helps us define colors of bricks.
		double X = BRICK_X_OFFSET;
		double Y = BRICK_Y_OFFSET;
		for(int i = 0; i < NBRICK_ROWS; i++) {	//this 'for' is responsible for brick rows.
			for(int j = 0; j < NBRICKS_PER_ROW; j++ ) { //this 'for' is responsible for each brick of 1 row.
				GRect brick = new GRect(X + BRICK_WIDTH * j, Y, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				
				if(counter < 2*NBRICKS_PER_ROW ) {
					brick.setColor(Color.RED);
					brick.setFillColor(Color.RED);
				}else if(counter < 4*NBRICKS_PER_ROW ) {
					brick.setColor(Color.ORANGE);
					brick.setFillColor(Color.orange);
				}else if(counter < 6*NBRICKS_PER_ROW ) {
					brick.setColor(Color.YELLOW);
					brick.setFillColor(Color.YELLOW);
				}else if(counter < 8*NBRICKS_PER_ROW ) {
					brick.setColor(Color.GREEN);
					brick.setFillColor(Color.green);
				}else {
					brick.setColor(Color.CYAN);
					brick.setFillColor(Color.CYAN);
				}
				counter++;
				add(brick);
				X += BRICK_SEP;
			}
			X -= BRICK_SEP * NBRICKS_PER_ROW;
            Y += BRICK_HEIGHT + BRICK_SEP;   //"Jump" on the following row.
		}
	}
	
	//add paddle
	private GRect paddle;
	private void addPaddle() {
		paddle = new GRect(getWidth()/2 - PADDLE_WIDTH/2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.GRAY);
		add(paddle);
	} 
	
	//move paddle
	public void mouseMoved(MouseEvent e) {
		paddle.setLocation(e.getX() - PADDLE_WIDTH/2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT );
		//we need to check if paddle moves out of borders.
		if(e.getX() <= PADDLE_WIDTH/2) {
	        paddle.setLocation(0, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	    }else if(e.getX() >= getWidth() -PADDLE_WIDTH/2) {
	        paddle.setLocation(getWidth() - PADDLE_WIDTH, getHeight() -PADDLE_Y_OFFSET - PADDLE_HEIGHT);
	    }
	}
	
	//add ball 
	private void addBall() {
		ball = new GOval(getWidth()/2 - BALL_RADIUS, getHeight()/2 - BALL_RADIUS, 2*BALL_RADIUS, 2*BALL_RADIUS);
		ball.setFilled(true);
		ball.setColor(Color.black);
		ball.setFillColor(Color.black);
		add(ball);
	}
	
	private void moveBall() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		vy = 5.0;
		waitForClick();  //game begins after 1 click.
		while(true) {
			ball.move(vx, vy);
			checkWalls();
			checkObject();
			pause(TIME);
			if(Nlife == 0) {   //Losing condition
				display_message();
				break;
			}else if(bricks_left == 0) {  //Winning condition.
				display_win_message();
				break;
			}
		}
	}
	
	//ball changes direction if it hits a wall.
	//if it hits bottom "wall" player loses 1 of his/her 3 lives.
	private void checkWalls() {
		if(ball.getX() <= 0) {
			vx = -vx;
		}else if(ball.getX() + 2*BALL_RADIUS >= getWidth()) {
			vx = -vx;
		}else if(ball.getY() <= 0) {
			vy = -vy;
		}else if(ball.getY() + 2*BALL_RADIUS >= getHeight()) {
			Nlife--;
			ball.setLocation(getWidth()/2 - BALL_RADIUS, getHeight()/2 - BALL_RADIUS);	//ball gets placed on the centre of the canvas.
			if(Nlife >= 1) {
			waitForClick();		//if a player has 1 or 2 lives left he/she starts new try after a click.
			}
		}
	}
	
	private void checkObject() {
		GObject collider = getCollidingObject();
		if(collider != null) {
			if(collider == paddle) {
				ball.setLocation(ball.getX(), paddle.getY() - 2*BALL_RADIUS - 3);  //To get rid of the bug (ball getting stuck in the paddle).
				vy = -vy;
				vx = rgen.nextDouble(-3.0, 3.0);
				bounceClip.play();
			}else if(collider != paddle) {
				remove(collider);
				bricks_left--;
				vy = -vy;
				bounceClip.play();
			}
		}
	}
	
	//I checked 8 sides of the ball. This private GObject returns an object which gets hit with the ball.
	//for each case I subtracted/added 2/3 pixels to get rid of bugs.
	private GObject getCollidingObject() {
		if(getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS + 2) != null) {
			collider = getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS + 2);
		}else if(getElementAt(ball.getX() + 2*BALL_RADIUS + 3, ball.getY() + BALL_RADIUS + 2) != null) {
			collider = getElementAt(ball.getX() + 2*BALL_RADIUS + 3, ball.getY() + BALL_RADIUS + 2);
		}else if(getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() - 1) != null) {
			collider = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() - 1);
		}else if(getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + 2*BALL_RADIUS +2) != null) {
			collider = getElementAt(ball.getX() + BALL_RADIUS + 1, ball.getY() + 2*BALL_RADIUS +2);
		}else if(getElementAt(ball.getX() - 1 + BALL_RADIUS/3, ball.getY() - 1 + BALL_RADIUS/3) != null) {
			collider = getElementAt(ball.getX() - 1 + BALL_RADIUS/3, ball.getY() - 1 + BALL_RADIUS/3);
		}else if(getElementAt(ball.getX() - 1 + BALL_RADIUS/3, ball.getY() - 1 + 2*BALL_RADIUS + 3 + BALL_RADIUS/3) != null) {		
			collider = getElementAt(ball.getX() - 1 + BALL_RADIUS/3, ball.getY() - 1 + 2*BALL_RADIUS + 3 + BALL_RADIUS/3);
		}else if(getElementAt(ball.getX() + 2*BALL_RADIUS + 2 - BALL_RADIUS/3, ball.getY() - 1 + BALL_RADIUS/3) != null) {
			collider = getElementAt(ball.getX() + 2*BALL_RADIUS + 2 - BALL_RADIUS/3, ball.getY() - 1 + BALL_RADIUS/3);
		}else if(getElementAt(ball.getX() + 2*BALL_RADIUS + 2 - BALL_RADIUS/3, ball.getY() - 1 + 2*BALL_RADIUS + 3 - BALL_RADIUS/3) != null) {
			collider = getElementAt(ball.getX() + 2*BALL_RADIUS + 2 - BALL_RADIUS/3, ball.getY() - 1 + 2*BALL_RADIUS + 3 - BALL_RADIUS/3);
		
		}else {
			collider = null;
		}
		return collider;
	}
	
	//message "GAME OVER!" appears on the screen if you lose.
	private void display_message() {
		removeAll();
		setBackground(Color.black);
		GLabel lose = new GLabel("GAME OVER!");
		lose.setFont("ITALIC-40");
		lose.setLocation(getWidth()/2 - lose.getWidth()/2, getHeight()/2);
		lose.setColor(Color.DARK_GRAY);
		add(lose);
	}
	
	//message "YOU WIN!!!" appears on the screen if you win.
	private void display_win_message() {
		removeAll();
		setBackground(Color.green);
		GLabel win = new GLabel("YOU WIN!!!");
		win.setFont("ITALIC-40");
		win.setLocation(getWidth()/2 - win.getWidth()/2, getHeight()/2);
		win.setColor(Color.YELLOW);
		add(win);
	}
}
