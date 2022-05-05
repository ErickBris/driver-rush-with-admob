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

import android.app.Activity;
import android.os.Bundle;


public abstract class acActivity extends Activity {

	// Objects
	private acHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new acHelper(this);
	}

	public int getScreenWidth() {
		return helper.getScreenWidth();
	}


	public int getScreenHeight() {
		return helper.getScreenHeight();
	}

	public void useImmersiveMode() {
		helper.useImmersiveMode();
	}

	public void onResume() {
		super.onResume();
		helper.onResume();
	}

	public void saveInt(String name, int value) {
		helper.saveInt(name, value);
	}

	public void saveBool(String name, boolean value) {
		helper.saveBool(name, value);
	}


	public void saveFloat(String name, float value) {
		helper.saveFloat(name, value);
	}


	public void saveString(String name, String value) {
		helper.saveString(name, value);
	}

	public int getSavedInt(String name) {
		return helper.getSavedInt(name);
	}


	public float getSavedFloat(String name) {
		return helper.getSavedFloat(name);
	}


	public boolean getSavedBool(String name) {
		return helper.getSavedBool(name);
	}

	/**
	 * Get a saved value.
	 *
	 * @param name - name of the saved value.
	 * @return saved value of name. "" if name doesn't exist.
	 */
	public String getSavedString(String name) {
		return helper.getSavedString(name);
	}
}
