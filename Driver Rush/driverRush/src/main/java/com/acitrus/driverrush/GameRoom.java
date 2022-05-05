package com.acitrus.driverrush;

import com.engine.acitrus.acView;
import com.engine.acitrus.GameObject;
import com.engine.acitrus.NumberDisplay;
import com.engine.acitrus.Room;

/**
 * Game room.
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class GameRoom extends Room {

	// Difficulty constants
	private final double INIT_SPEED = 40;              // Initial speed of the player.
	private final double DEC = 350;                    // Make this BIGGER to decelerate SLOWER.
	private final double ACC = 2;                      // How quickly the player speeds up after getting a collectable. (BIGGER = FASTER)
	private final double SPEED_THRES = 12;              // Speed at which collectables don't have as much an effect on speed

	private final int NUM_ENEMIES = 4;                 // Number of enemies. Doesn't change how often they appear.
	private final double RELEASE = 0.5;                // When to release next enemy. (0.5 = when last enemy is half way down the screen).

	// GameObjects
	public Player player;                              // The object that represents the player (taxi)
	public Collectable collect;                        // The collectable object (gascan)
	public NumberDisplay number;                       // The score display.
	public GameObject collision;                       // A graphic that shows when the player hits an enemy (smoke)
	public GameObject right;                           // Right arrow graphic
	public GameObject left;                            // Left arrow graphic
	public GameBG bg;                                  // Background (road)
	public Enemy enemies[];                            // Enemies (cars)

	// Room variables
	private double speed;                              // The player's speed. May not be the actual speed. (The player eases up and down to this speed)
	public double dec;                                 // The player's deceleration.
	private double realSpeed;                          // The player's actual speed. Is always approaching speed.
	private int lastEnemy;                             // Position of the last released enemy in the array of enemies
	public int score;                                  // The score (# of collectables collected)

	public GameRoom(acView container) {
		super(container);

		/* Initialize and add the collectable to the room */
		collect = new Collectable(nextInstance(), this);
		addObject(collect);

		/* Initialize and add the background to the room */
		bg = new GameBG(nextInstance(), this);
		addObject(bg);

		/* Initialize and add the enemies to the room */
		enemies = new Enemy[NUM_ENEMIES];
		for (int c = 0; c < NUM_ENEMIES; c++) {
			enemies[c] = new Enemy(nextInstance(), this);
			addObject(enemies[c]);
		}

		/* The player */
		player = new Player(nextInstance(), this);
		addObject(player);

		/* The score display */
		number = new NumberDisplay(nextInstance(), this);
		number.setAlignment(1);
		number.x = getWidth() / 2;
		number.y = getHeight() * 3/4;
		number.width = getWidth() / 6;
		number.height = number.width;
		addObject(number);

		/* Collision graphic initialization */
		collision = new GameObject(nextInstance(), this);
		collision.setGraphic(GameView.collision, 1);
		collision.height = collision.width = getWidth() / 7;
		collision.x = -collision.width;
		collision.layer = 3;
		addObject(collision);

		/* Right and left arrows */
		right = new GameObject(nextInstance(), this);
		right.width = getWidth() / 8;
		right.height = right.width / 2;
		right.x = getWidth() * 7 / 8;
		right.y = -right.height;
		right.setGraphic(GameView.right, 1);
		addObject(right);

		left = new GameObject(nextInstance(), this);
		left.width = getWidth() / 8;
		left.height = left.width / 2;
		left.x = getWidth() / 8;
		left.y = -left.height;
		left.setGraphic(GameView.left, 1);
		addObject(left);

		/* Number kerning */
		number.setBeforeKerning(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		number.setAfterKerning(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public void set() {
		/* Initial values */
		speed = INIT_SPEED * getRatioY();
		dec = speed / DEC * getRatioY();
		realSpeed = speed;
		score = 0;
		lastEnemy = 0;

		for (int c = 0; c < NUM_ENEMIES; c++) {             // Set up the enemies
			enemies[c].set();
		}

		bg.set();                                           // Set up the background
		player.set();                                       // Set up the player
		collect.set();                                      // The collectable
		collision.x = -collision.width;                     // The collision graphic
		left.y = -left.height;                              // The left
		right.y = -right.height;                            // and right arrows
	}

	@Override
	public void step(double deltaTime) {

		if (getView().getCurrentRoom() == this) {         // Current room is game room

			/* Top speed */
			dec = speed / (DEC * getRatioY());
			speed -= dec * getRatioY() * deltaTime;

			/* Smoothly accelerate to top speed */
			if (speed > realSpeed) {
				realSpeed += ACC * getRatioY() * deltaTime;
			} else if (realSpeed > 0) {
				realSpeed -= dec * getRatioY() * deltaTime;
			}

			/* Release collectable */
			if (enemies[lastEnemy].y < getHeight() * 3 / 4 && collect.y < -collect.height / 2) {
				collect.go();
			}

			/* Release next car */
			if (enemies[lastEnemy].y < getHeight() - (getHeight() * RELEASE)) {
				lastEnemy++;

				if (lastEnemy > NUM_ENEMIES - 1) {
					lastEnemy = 0;
				}

				enemies[lastEnemy].go();
			}

			/* Score */
			number.setNumber(score);
			number.x = getWidth() / 2;

			/* Stopped */
			if (speed <= 1) {
				speed = 0;
				GameView.gameOver.set();
				getView().goToRoom(GameView.gameOver);
			}
		} else if (getView().getCurrentRoom() == GameView.startRoom) { // current room is start screen
			number.x = -number.width;  // Move the score off screen
		} else if (getView().getCurrentRoom() == GameView.gameOver) {  // current room is game over
			if (realSpeed > 0) {
				realSpeed -= dec * getRatioY() * deltaTime;
			} else {
				realSpeed = 0;
			}
		}

		/* Synchronize speeds */
		collect.setSpeed(realSpeed);            // Collectable
		bg.setSpeed(realSpeed);                 // Background

		for (int c = 0; c < NUM_ENEMIES; c++) { // Enemies
			enemies[c].setSpeed(realSpeed - 5);
		}

		super.step(deltaTime);
	}

	public void incrementSpeed(int amount) {
		double adjusted;

		if (speed > SPEED_THRES * getRatioY()) {                      // Have hit the threshold, don't speed up too much
			adjusted = amount * (SPEED_THRES * getRatioY() / speed);
		} else {
			adjusted = amount;
		}

		speed += adjusted;
	}

	public void gameOver() {
		dec = 2;
	}
}
