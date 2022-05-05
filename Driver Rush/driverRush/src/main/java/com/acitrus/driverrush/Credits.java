package com.acitrus.driverrush;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * This class contains the code the for the About activity. The about activity displays your company logo, credits,
 * and About text that is customizable through strings.xml.
 * 
 * @author Benjamin Blaszczak (Bobby Lou Jo)
 *
 */
public class Credits extends Activity {
	
	private ImageView more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		
		more = (ImageView) findViewById(R.id.imageView2);
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Acitrus")));
			}
			
		});
	}
}
