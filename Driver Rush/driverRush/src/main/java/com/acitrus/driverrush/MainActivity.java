package com.acitrus.driverrush;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.engine.acitrus.acActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Crazy Taxi Driver Android game Bobby Lou Jo (2014)
 * 
 * Crazy Taxi Driver is a game created by Bobby Lou Jo. It is available as a
 * template on Chupamobile. Those who have purchased a license to use this
 * source from Chupamobile may reskin and make other changes to the game then
 * use it for commercial or non-commercial purposes in accordance to the
 * licensing terms on Chupamobile.
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 * 
 */
public class MainActivity extends acActivity {

	// AdMob
	private static final String BANNER_AD_UNIT_ID = "YOUR BANNER AD ID";                             // "Top banner on Game Over" ad unit ID
	private static final String INTER_AD_UNIT_ID = "YOUR INTERSTITIAL AD ID";                        // "Interstitial ad" ad unit ID

	public ProgressBar spinner;                                         // Loading circle to show while loading interstitial ad
	private AdRequest adRequest;                                        // AdMob ad request
	private AdView adView;                                              // The banner
	private InterstitialAd inter;                                       // Interstitial (full screen) ad.
	private boolean show;                                               // Should the interstitial be show when it is loaded?

	// Music
	private MediaPlayer backgroundMusic;       // MediaPlayer for playing background music

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		useImmersiveMode(); // Use immersive mode. Removes navbar/notification bar/etc.

		setContentView(R.layout.activity_main);

		/* ADVERTISING (ADMOB) */
		// Get the spinner
		spinner = (ProgressBar) findViewById(R.id.adspinner);
		spinner.setVisibility(View.GONE);

		// Create an ad (banner).
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(BANNER_AD_UNIT_ID);

		// Create interstitial ad
		inter = new InterstitialAd(this);
		inter.setAdUnitId(INTER_AD_UNIT_ID);

		inter.setAdListener(new AdListener() {

			@Override
			public void onAdLoaded() {
				if (inter.getAdUnitId() != null && show) inter.show(); // The ad should be shown when it is loaded
				show = false;
				
				spinner.setVisibility(View.GONE);     // Get rid of spinner
			}

			@Override
			public void onAdFailedToLoad(int error) { // Ad didn't load
				spinner.setVisibility(View.GONE);     // Get rid of the spinner
			}
			
			@Override
			public void onAdClosed() {              // Current ad is closed
				inter.loadAd(adRequest);            // Load a new ad for next time
			}
		});

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
		inter.loadAd(adRequest);
		show = false;

		// Configure adView
		adView.loadAd(adRequest);
		//adView.setVisibility(View.GONE);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		adView.setLayoutParams(adParams);
		((RelativeLayout) findViewById(R.id.mainlayout)).addView(adView);

		/* Play background music */
		backgroundMusic = MediaPlayer.create(this, R.raw.background);
		backgroundMusic.start();
		backgroundMusic.setLooping(true);
		backgroundMusic.setVolume(.5f, .5f);
	}

	@Override
	public void onPause() {
		super.onPause();
		backgroundMusic.pause(); // Pause music
	}

	@Override
	public void onResume() {
		super.onResume();
		backgroundMusic.start(); // Resume music
	}

	/**
	 * Show the About activity.
	 */
	public void showAbout() {
		Intent credits = new Intent(this, Credits.class);
		this.startActivity(credits);
	}

	/* ADVERTISEMENTS */

	/**
	 * This method shows the banner ad.
	 */
	public void showAd() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (adView != null) {
					adView.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * This method removes the banner ad.
	 */
	public void removeAd() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (adView != null) {
					adView.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * This method shows the interstitial ad.
	 */
	public void showInterstitial() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (inter != null && isOnline()) {
					if (inter.isLoaded()) {                   // ad is already loaded
						inter.show();                         // show it.
					} else {                                  // ad is NOT loaded
						spinner.setVisibility(View.VISIBLE);  // Show the loading spinner
						inter.loadAd(adRequest);              // Load the ad
						show = true;                          // Show the ad as soon as it is loaded.
					}
				}
			}

		});
	}

	/**
	 * Check if we are connected to the internet.
	 * 
	 * @return true if connected or connecting, false otherwise
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) { return true; }
		return false;
	}
}
