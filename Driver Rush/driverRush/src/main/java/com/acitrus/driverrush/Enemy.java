package com.acitrus.driverrush;

import java.util.Random;

import com.engine.acitrus.GameObject;
import com.engine.acitrus.Room;
import com.engine.acitrus.SoundPlayer;

/**
 * Enemy object (car)
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class Enemy extends GameObject {

	// Difficulty constants
	final int INIT_LC_SPEED = 15;            // Speed at which enemies change lanes
	final double SWITCH_THRES = 4.0 / 5.0;   // Y position where enemies can no longer change lanes (fraction of the screen, up from the bottom)
	
	final int FRAMES = 3;                    // Number of frames the enemy graphic has (colors)
	
	// Lane constants, for code readability
	final int LEFT = 0;
	final int CENTER = 1;
	final int RIGHT = 2;

	// Sound
	private SoundPlayer sound;               // Sound player for playing sound effect
	private int crash;                       // Crash sound effect id
	
	// Variables
	private int currentPos;
	private double laneChangeSpeed;
	private double speed;
	private boolean go;

	private Random rand;

	protected Enemy(int id, Room containingRoom) {
		super(id, containingRoom);

		rand = new Random();
		sound = new SoundPlayer(this.getActivity().getApplicationContext());
		crash = sound.newSound(R.raw.crash);

		giveCollisionBox(0.1, 0.1, .9, .9);

		setGraphic(GameView.enemy, FRAMES);
	}

	// setup / reset
	public void set() {
		width = getRoom().getWidth() / 8;
		height = width * 2;

		y = -height;

		laneChangeSpeed = INIT_LC_SPEED * getRatioX();
		speed = 4;
		go = false;

		layer = 2;
	}

	/**
	 * Go to top of screen and pick lane.
	 */
	public void go() {
		go = true;
	}

	/**
	 * Happens every frame.
	 */
	@Override
	public void step(double deltaTime) {
		super.step(deltaTime);
		
		if (getRoom().checkCollision(this, ((GameRoom) getRoom()).player)) {  // Collision with player
			speed = 0;
			
			if (!((GameRoom) getRoom()).collision.onScreen()) {
				sound.play(crash);
				
				((GameRoom) getRoom()).gameOver();

				/* Show collision graphic */
				((GameRoom) getRoom()).collision.x = (x + ((GameRoom) getRoom()).player.x) / 2;
				((GameRoom) getRoom()).collision.y = (y + ((GameRoom) getRoom()).player.y) / 2;
				((GameRoom) getRoom()).collision.step(deltaTime);

				/* Go to next room */
				GameView.gameOver.set();
				getView().goToRoom(GameView.gameOver);
			}
		}
		
		if (y > -height) {  // Keep moving
			y -= speed;
		}

		/* Change Lanes */
		if (rand.nextInt(40) == 0 && y > getRoom().getHeight() * SWITCH_THRES) {
			currentPos = rand.nextInt(3);
		}

		angle = 0;

		switch (currentPos) {
		case LEFT:
			if (x > getRoom().getWidth() / 4 + laneChangeSpeed) {
				x -= laneChangeSpeed;
				angle = 20;
			} else {
				x = getRoom().getWidth() / 4;
			}
			break;
		case CENTER:
			if (x > getRoom().getWidth() / 2 + laneChangeSpeed) {
				x -= laneChangeSpeed;
				angle = 20;
			} else if (x < getRoom().getWidth() / 2 - laneChangeSpeed) {
				x += laneChangeSpeed;
				angle = -20;
			} else {
				x = getRoom().getWidth() / 2;
			}
			break;
		case RIGHT:
			if (x < getRoom().getWidth() * 3 / 4 - laneChangeSpeed) {
				x += laneChangeSpeed;
				angle = -20;
			} else {
				x = getRoom().getWidth() * 3 / 4;
			}
			break;
		}
		
		/* Go */
		if (go && y < -height / 2) {
			y = getRoom().getHeight() + height / 2; // Go to top of screen
			frame = rand.nextInt(FRAMES);           // Change color (different frame, different color)

			switch (rand.nextInt(3)) {              // Pick a lane
			case 0:
				currentPos = LEFT;
				x = getRoom().getWidth() / 4;
				break;
			case 1:
				currentPos = CENTER;
				x = getRoom().getWidth() / 2;
				break;
			case 2:
				currentPos = RIGHT;
				x = getRoom().getWidth() * 3 / 4;
				break;
			}
			
			go = false;
		}
	}

	/**
	 * Set the vertical speed of this enemy.
	 * 
	 * @param speed - new vertical speed
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
