package com.acitrus.driverrush;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import android.view.View;
import com.engine.acitrus.acActivity;
import com.engine.acitrus.acView;
import com.engine.acitrus.GameObject;
import com.engine.acitrus.NumberDisplay;
import com.engine.acitrus.Room;

/**
 * Game Over room.
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 * 
 */
public class GameOver extends Room {

	// Constants
	private final int AD_FREQ = 6;             // Number of plays between full screen ads.
	private final String bestscore = "BEST";   // Name used for saving the best score
	
	// Data
	private int plays;

	// Game Objects
	private NumberDisplay bestScore;           // Displays the best score
	private GameObject play;                   // The play button
	private GameObject score, best;            // The "Score" and "Best" graphics

	public GameOver(acView container) {
		super(container);
		
		plays = 0;

		/* High score display */
		bestScore = new NumberDisplay(nextInstance(), this);
		bestScore.setAlignment(1);
		bestScore.x = getWidth() / 2;
		bestScore.y = getHeight() / 2;
		bestScore.width = getWidth() / 6;
		bestScore.height = bestScore.width;
		addObject(bestScore);

		/* Play button */
		play = new GameObject(nextInstance(), this);
		play.width = getWidth() / 4;
		play.height = play.width;
		play.x = getWidth() / 2;
		play.y = getHeight() / 4;
		play.setGraphic(GameView.play, 1);
		addObject(play);

		/* "Score" and "Best" graphics */
		score = new GameObject(nextInstance(), this);
		best = new GameObject(nextInstance(), this);
		best.width = score.width = getWidth() / 2;
		best.height = score.height = score.width / 2;
		score.setGraphic(GameView.score, 2);
		best.setGraphic(GameView.score, 2);
		score.x = best.x = getWidth() / 2;

		score.frame = 0;
		score.y = getHeight() * 7 / 8;

		best.frame = 1;
		best.y = getHeight() * 5 / 8;

		addObject(score);
		addObject(best);
		
		/* Number kerning */
		bestScore.setBeforeKerning(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		bestScore.setAfterKerning(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	/**
	 * Set this room to it's default state.
	 */
	public void set() {
		play.x = getWidth() / 2;
		play.y = getHeight() / 4;

		// Best score
		bestScore.x = getWidth() / 2;
		bestScore.setNumber(((acActivity) getActivity()).getSavedInt(bestscore));

		// New best, save it
		if (bestScore.getNumber() < GameView.gameRoom.score) {
			((acActivity) getActivity()).saveInt(bestscore, GameView.gameRoom.score);
			bestScore.setNumber(GameView.gameRoom.score);
		}

		// Show ad
		((MainActivity) getActivity()).showAd();
		
		plays++;
		if (plays % AD_FREQ == 0) {
			((MainActivity) getActivity()).showInterstitial();
		}

		Log.d("test", "frame: " + Integer.toString(best.frame));
	}

	/**
	 * Happens every frame.
	 */
	@Override
	public void step(double dt) {
		GameView.gameRoom.update(dt);
		super.step(dt);
	}

	/**
	 * Handle touches.
	 */
	public void newpress(int index) {
		if (((MainActivity) getActivity()).spinner.getVisibility() == View.GONE) {
			if (getTouch().objectTouched(play)) {             // Play button is touched.
				getView().goToRoom(GameView.startRoom);
				GameView.startRoom.set();
				GameView.startRoom.stage = 1;
			}
		}

		super.newpress(index);
	}

	/**
	 * Draw both this room and the game room underneath it.
	 */
	@Override
	public void draw(GL10 gl) {
		GameView.gameRoom.draw(gl); // Draw game room first.
		super.draw(gl);
	}
}
