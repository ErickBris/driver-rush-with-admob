package com.acitrus.driverrush;

import com.engine.acitrus.GameObject;
import com.engine.acitrus.Room;

/**
 * The Player object.
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class Player extends GameObject {
	
	// Difficulty constants
	final int INIT_LC_SPEED = 20;             // The speed at with the player changes lanes
	
	// Lane constants for code readability
	final int LEFT = 0;
	final int CENTER = 1;
	final int RIGHT = 2;

	// Variables
	private int currentPos;           // The player's current lane
	private double laneChangeSpeed;   // The speed at which the player changes lanes (adjusted to screen size)

	protected Player(int id, Room containingRoom) {
		super(id, containingRoom);

		giveCollisionBox(0.1, 0.1, .9, .9);

		setPreciseGraphic(GameView.player, 0, 0, 1, 1, 1);
	}

	/**
	 * Set to default state.
	 */
	public void set() {
		width = getRoom().getWidth() / 8;
		height = width * 2;

		x = getRoom().getWidth() / 2;
		y = getRoom().getHeight() / 5;

		currentPos = CENTER;
		laneChangeSpeed = INIT_LC_SPEED * getRatioX();
		angle = 0;

		layer = 3;
	}

	/**
	 * Happens every frame.
	 */
	@Override
	public void step(double deltaTime) {
		super.step(deltaTime);

		if (getView().getCurrentRoom() instanceof GameRoom) {
			angle = 0;
			
			switch (currentPos) { // change lanes
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
		}
	}

	/**
	 * Handle touches. Controls when the car switches lanes.
	 */
	@Override
	public void newpress(int index) {
		if (getTouch().areaTouched(0, getRoom().getHeight(), getRoom().getWidth() / 2, 0)) {                           // Left side of the screen is touched
			if (currentPos > 0) {  // Move left
				currentPos--;
			}
		} else if (getTouch().areaTouched(getRoom().getWidth() / 2, getRoom().getHeight(), getRoom().getWidth(), 0)) { // Right side of the screen is touched
			if (currentPos < 2) {  // Move right
				currentPos++;
			}
		}
	}
}
