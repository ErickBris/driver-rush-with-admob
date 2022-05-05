package com.acitrus.driverrush;

import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.util.AttributeSet;
import com.engine.acitrus.acView;
import com.engine.acitrus.Graphic;

/**
 * The custom Android view that displays all of the game objects. Game objects are organized into
 * rooms, the rooms are organized here. The view displays one room at a time, though a room may
 * also display another room underneath it (StartRoom and GameOver do this: they display GameRoom
 * underneath themselves).
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class GameView extends acView {

	// Graphics
	
	// Title screen
	public static Graphic title;
	public static Graphic play;
	public static Graphic ready;
	
	// Game
	public static Graphic player;
	public static Graphic enemy;
	public static Graphic background;
	public static Graphic collect;
	public static Graphic number;
	public static Graphic collision;
	public static Graphic right;
	public static Graphic left;
	public static Graphic about;
	
	// Game Over
	public static Graphic score;

	// Rooms
	public static StartRoom startRoom;
	public static GameRoom gameRoom;
	public static GameOver gameOver;

	public GameView(Context context) {
		super(context);
	}
	
	public GameView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	@Override
	public void onCreateRooms() {
		// Initialize the rooms
		startRoom = new StartRoom(this);
		gameRoom = new GameRoom(this);
		gameOver = new GameOver(this);

		// Start in the start room!
		startRoom.set();
		goToRoom(startRoom);
	}

	@Override
	public void onCreateGraphics() {
		/**
		 * "Load" the graphics. (They actually aren't loaded right away, but when onSurfaceCreated is called in acRenderer.java).
		 */
		
		getGraphicsHelper().setParameters(true, GL11.GL_NEAREST_MIPMAP_NEAREST, GL11.GL_NEAREST);
		
		// Start screen
		title = getGraphicsHelper().addGraphic(R.drawable.title);
		play = getGraphicsHelper().addGraphic(R.drawable.play);
		ready = getGraphicsHelper().addGraphic(R.drawable.touchtoplay);
		about = getGraphicsHelper().addGraphic(R.drawable.about);
		
		// Game
		player = getGraphicsHelper().addGraphic(R.drawable.player);
		enemy = getGraphicsHelper().addGraphic(R.drawable.enemy);
		background = getGraphicsHelper().addGraphic(R.drawable.bg);
		collect = getGraphicsHelper().addGraphic(R.drawable.collectable);
		number = getGraphicsHelper().addGraphic(R.drawable.numbers);
		collision = getGraphicsHelper().addGraphic(R.drawable.crashgraphic);
		right = getGraphicsHelper().addGraphic(R.drawable.right);
		left = getGraphicsHelper().addGraphic(R.drawable.left);
		
		// Game Over
		score = getGraphicsHelper().addGraphic(R.drawable.score);
	}

}
