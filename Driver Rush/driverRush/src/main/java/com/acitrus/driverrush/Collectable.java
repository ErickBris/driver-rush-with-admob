package com.acitrus.driverrush;

import java.util.Random;

import com.engine.acitrus.GameObject;
import com.engine.acitrus.Room;
import com.engine.acitrus.SoundPlayer;

/**
 * Collectable object (gascan)
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class Collectable extends GameObject {
	
	/* Constants */
	private int SPEED_UP = 20;                  // Amount to increase the player's speed when collected
	
	/* Data */
	private double speed;                       // Vertical speed of this collectable
	private Random rand;                        // Random number generator for selecting a lane
	private SoundPlayer sound;                  // Sound player to play sound effect
	private int pickup;                         // Sound effect id
	
	protected Collectable(int id, Room containingRoom) {
		super(id, containingRoom);
		
		giveCollisionBox(0, 0, 1, 1);           // Give a collision box. This box is (0,0) to (1,1), meaning the entire object.
		
		setGraphic(GameView.collect, 1);        // Choose which graphic to use and how many frames it has.
		
		// Sound effect
		sound = new SoundPlayer(this.getActivity().getApplicationContext());
		pickup = sound.newSound(R.raw.collect);
		
		rand = new Random();                    // Random number generator
	}

	// setup / reset
	public void set() {
		width = getRoom().getWidth() / 8;
		height = width;

		y = -height;
		speed = 4;
		
		layer = 1;
	}
	
	/**
	 * Go to the top of the screen and a random lane.
	 */
	public void go() {
		y = getRoom().getHeight() + height / 2;  // Top of screen
		
		switch (rand.nextInt(3)) {               // Random lane
		case 0:
			x = getRoom().getWidth() / 4;        // Lane 1
			break;
		case 1:
			x = getRoom().getWidth() / 2;        // Lane 2
			break;
		case 2:
			x = getRoom().getWidth() * 3 / 4;    // Lane 3
			break;
		}	
	}
	
	/**
	 * Happens every frame.
	 */
	@Override
	public void step(double dt) {
		y -= speed;
		
		if (getRoom().checkCollision(this, ((GameRoom) getRoom()).player)) { // Collision with the player
			sound.play(pickup);
			y = -height;
			((GameRoom) getRoom()).incrementSpeed((int) (SPEED_UP * getRatioY()));
			((GameRoom) getRoom()).score++;
		}
		
		super.step(dt);
	}
	
	/**
	 * Set the vertical speed of this collectable.
	 * 
	 * @param speed - new vertical speed
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
