package com.acitrus.driverrush;

import android.content.Intent;

import com.engine.acitrus.SplashActivity;

/**
 * Use this Activity to show splash screens. Splash screens can be defined by
 * using Android XML layouts. See bobEngine/res/layouts/splash.xml for an
 * example.
 *
 * Created by Benjamin on 3/12/2015.
 */
public class SplashScreenActivity extends SplashActivity {
	@Override
	protected void setup() {
		/**
		 * Add your splash screens in the order you want them
		 * to appear.
		 */
		addSplash(R.layout.splash, 2000);
	}

	@Override
	protected void end() {
		Intent main = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(main);
		finish();
	}
}
