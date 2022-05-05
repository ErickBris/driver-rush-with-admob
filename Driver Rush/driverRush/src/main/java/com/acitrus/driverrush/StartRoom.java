package com.acitrus.driverrush;

import javax.microedition.khronos.opengles.GL10;

import android.view.View;
import com.engine.acitrus.acView;
import com.engine.acitrus.GameObject;
import com.engine.acitrus.Room;

/**
 * The first room the player sees.
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 * 
 */
public class StartRoom extends Room {

	// Game Objects
	private GameObject title;       // The title graphic
	private GameObject play;        // Play button
	private GameObject ready;       // Instructions
	private GameObject about;       // "?" button that starts the About activity

	// Data
	public int stage;               // Current stage of the game
						// 0 = showing title
						// 1 = showing instructions
						// 2 = in game

	public StartRoom(acView container) {
		super(container);

		/* Title graphic initialization */
		title = new GameObject(nextInstance(), this);
		title.x = getWidth() / 2;
		title.y = getHeight() * 2 / 3;
		title.width = getWidth() / 2;
		title.height = title.width / 2;
		title.setGraphic(GameView.title, 1);
		addObject(title);

		/* Instruction graphic initialization */
		ready = new GameObject(nextInstance(), this);
		ready.width = getWidth() * 3 / 4;
		ready.height = ready.width * 3 / 2;
		ready.x = getWidth() / 2;
		ready.y = -ready.height;
		ready.setGraphic(GameView.ready, 1);
		addObject(ready);

		/* About button initialization */
		about = new GameObject(nextInstance(), this);
		about.width = getWidth() / 12;
		about.height = about.width;
		about.x = getWidth() / 8;
		about.y = about.x;
		about.setGraphic(GameView.about, 1);
		addObject(about);

		/* Play button initialization */
		play = new GameObject(nextInstance(), this);
		play.width = getWidth() / 4;
		play.height = play.width;
		play.x = getWidth() / 2;
		play.y = getHeight() / 4;
		play.setGraphic(GameView.play, 1);
		addObject(play);

		/* Show the interstitial ad */
		((MainActivity) getActivity()).showInterstitial();
	}

	/**
	 * Set or reset the room to it's default state.
	 */
	public void set() {
		GameView.gameRoom.set();
		play.x = getWidth() / 2;
		play.y = getHeight() / 4;
		stage = 0;
	}

	/**
	 * Happens every frame.
	 */
	@Override
	public void step(double deltaTime) {
		GameView.gameRoom.update(deltaTime);

		if (stage == 1) {                                  // Show instructions
			ready.y = getHeight() / 2;
			title.y = -title.height;
			play.y = -play.height;
			about.y = -about.height;
		} else if (stage == 2) {                           // Go to game
			GameView.gameRoom.right.y = getHeight() / 8;   // Show right
			GameView.gameRoom.left.y = getHeight() / 8;    // and left arrows
			((MainActivity) getActivity()).removeAd();
			getView().goToRoom(GameView.gameRoom);
		}

		super.step(deltaTime);
	}

	/**
	 * Output graphics.
	 */
	@Override
	public void draw(GL10 gl) {
		GameView.gameRoom.draw(gl); // Draw the game room underneath this room.
		super.draw(gl);             // Draw this room.
	}

	/**
	 * Handle touches.
	 */
	@Override
	public void newpress(int index) {
		if (((MainActivity) getActivity()).spinner.getVisibility() == View.GONE) { // Ad is loading
			if (getTouch().objectTouched(about)) {              // About button is touched
				((MainActivity) getActivity()).showAbout();       // Show About activity
			} else if (getTouch().objectTouched(play)) {        // Play Button is touched
				stage++;                                          // Show instructions
			} else if (stage == 1) {                            // Instructions already shown
				stage++;                                          // Go to game!
			}
		}
	}
}
