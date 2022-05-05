/**
 * BobEngine - 2D game engine for Android
 *
 * Copyright (C) 2014, 2015 Benjamin Blaszczak
 *
 * BobEngine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser Public License
 * version 2.1 as published by the free software foundation.
 *
 * BobEngine is provided without warranty; without even the implied
 * warranty of merchantability or fitness for a particular
 * purpose. See the GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with BobEngine; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301 USA
 *
 */

package com.engine.acitrus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


public class acHelper {

	// Constants
	final int VISIBILITY =                                      // The flags for immersive mode
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_FULLSCREEN |
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

	// Data
	private int screenWidth;                                    // Real width of the screen in pixels
	private int screenHeight;                                   // Real height of the screen in pixels
	private boolean useImmersive = false;                       // Flag that determines if Immersive Mode is in use
	private String save = "save";                               // Name of shared preferences file for saving data

	// Objects
	private Activity activity;
	private WindowManager wm;
	private Point size;

	@SuppressLint("NewApi")
	public acHelper(Activity activity) {
		this.activity = activity;

		wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		size = new Point();

		try {                                                   // Get screen dimensions
			wm.getDefaultDisplay().getRealSize(size);           // New method, might not work on old devices
			screenWidth = size.x;
			screenHeight = size.y;
		} catch (NoSuchMethodError er) {                        // If new method didn't work, use depreciated methods
			screenWidth = wm.getDefaultDisplay().getWidth();
			screenHeight = wm.getDefaultDisplay().getHeight();
		}

		ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		Log.i("info", Integer.toString(am.getMemoryClass()) + "MB ram available.");
	}

	public void onResume() {
		if (useImmersive) {
			useImmersiveMode();
		}
	}

	@SuppressLint("NewApi")
	public void useImmersiveMode() {

		try {                                                                  // Immersive mode (Will not work on versions prior to 4.4.2)
			activity.getWindow().getDecorView().setSystemUiVisibility(VISIBILITY);      // Set the flags for immersive mode
			UIChangeListener();                                                // Add a listener to detect if we have lost immersive mode

			wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
			size = new Point();

			wm.getDefaultDisplay().getRealSize(size);                          // Get -REAL- screen size. This excludes the navbar, which isn't visible
			screenWidth = size.x;
			screenHeight = size.y;

			useImmersive = true;
		} catch (NoSuchMethodError e) {                                        // Immersive mode not supported (Android version < 4.4.2)
			// Get KitKat!

			Log.d("BobEngine", "Immersive mode not supported. (Android version < 4.4.2)");
		}
	}


	@SuppressLint("NewApi")
	private void UIChangeListener() {
		final View decorView = activity.getWindow().getDecorView();

		decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
					decorView.setSystemUiVisibility(VISIBILITY);
				}
			}
		});
	}


	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getScreenWidth() {
		try {                                                   // Get screen dimensions
			wm.getDefaultDisplay().getRealSize(size);           // New method, might not work on old devices
			screenWidth = size.x;
		} catch (NoSuchMethodError er) {                        // If new method didn't work, use depreciated methods
			screenWidth = wm.getDefaultDisplay().getWidth();
		}

		return screenWidth;
	}


	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getScreenHeight() {
		try {                                                   // Get screen dimensions
			wm.getDefaultDisplay().getRealSize(size);           // New method, might not work on old devices
			screenHeight = size.y;
		} catch (NoSuchMethodError er) {                        // If new method didn't work, use depreciated methods
			screenHeight = wm.getDefaultDisplay().getHeight();
		}

		return screenHeight;
	}


	public void saveInt(String name, int value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putInt(name, value);
		edit.commit();
	}


	public void saveBool(String name, boolean value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putBoolean(name, value);
		edit.commit();
	}


	public void saveFloat(String name, float value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putFloat(name, value);
		edit.commit();
	}


	public void saveString(String name, String value) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		edit.putString(name, value);
		edit.commit();
	}

	public int getSavedInt(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getInt(name, 0);
	}


	public float getSavedFloat(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getFloat(name, 0f);
	}


	public boolean getSavedBool(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getBoolean(name, false);
	}


	public String getSavedString(String name) {
		SharedPreferences prefs = activity.getSharedPreferences(save, activity.MODE_PRIVATE);

		return prefs.getString(name, "");
	}
}
