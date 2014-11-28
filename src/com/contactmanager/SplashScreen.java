package com.contactmanager;

/**
 * @className CS 6301-022 User Interface Design
 * @author Ankit Tolia (abt140130), Abhishek Bansal (axb146030)
 * @email-id abt140130@utdallas.edu,axb146030@utdallas.edu 
 * @version 1.1
 * @started date 1th Nov 2014
 * 
 * Program of Phonebook for an Android device that allows user to manage contacts using add, edit, delete and view.
 * Class Description: This class displays the startup screen for the application for a few seconds.
 * Purpose : Class Assignment
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000; // set the timeout for the screen

	/*
	 * SplashScreen class is showing a layout for 2 sec at startup, It is
	 * showing an Image and Textview.
	 * 
	 * @created by Ankit Tolia
	 */
	/*
	 * onCreate function is where you initialize your activity. Most
	 * importantly, here you will usually call setContentView(int) with a layout
	 * resource defining your UI, and using findViewById(int) to retrieve the
	 * widgets in that UI that you need to interact with programmatically.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer.
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);

				// close this activity
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

}
