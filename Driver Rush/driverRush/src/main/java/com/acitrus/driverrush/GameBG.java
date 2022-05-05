package com.acitrus.driverrush;

import com.engine.acitrus.GameObject;
import com.engine.acitrus.Room;

/**
 * Scrolling background. (road)
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class GameBG extends GameObject {

	// Variables
	private double speed;          // Vertical speed
	private int numNeeded;         // Number of vertically tiled images needed to fill the screen.
	private Quad tiles[];          // Additional tiles for filling up the screen

	protected GameBG(int id, Room containingRoom) {
		super(id, containingRoom);

		setGraphic(GameView.background, 1);

		width = getRoom().getWidth();
		height = width;

		numNeeded = (int) (getRoom().getHeight() / height) + 2;
		tiles = new Quad[numNeeded];
		for (int i = 0; i < numNeeded; i++) {
			tiles[i] = new Quad();
			tiles[i].height = height;
			tiles[i].width = width;
			tiles[i].setGraphic(getGraphic());
			addQuad(tiles[i]);
		}
	}

	/**
	 * Set to default state.
	 */
	public void set() {
		y = height / 2;
		x = getRoom().getWidth() / 2;
		layer = 0;

		for (int i = 0; i < numNeeded; i++) {
			tiles[i].x = x;
			tiles[i].y = y + height * (i + 1);
		}
	}
	
	/**
	 * Happens every frame.
	 */
	@Override
	public void step(double deltaTime) {
		y -= speed;
		
		if (y <= -height / 2) {
			y = height / 2 - (-height / 2 - y);
		}

		for (int i = 0; i < numNeeded; i++) {
			tiles[i].y = y + height * (i + 1);
		}
	}

	/**
	 * Change the vertical speed.
	 * 
	 * @param speed - new vertical speed.
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
