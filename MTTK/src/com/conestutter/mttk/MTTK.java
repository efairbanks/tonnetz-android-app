package com.conestutter.mttk;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class MTTK extends ActionBarActivity {
	public SharedAppData sharedAppData;
	public Screen screen;
	public TonnetzPanel tonnetzPanel;
	public Audio audio;
	public MenuPanel menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// fullscreen
		//supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		//supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		//getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// landscape
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// init shared app data
		sharedAppData = new SharedAppData();
		// init screen
		screen = new Screen(this, sharedAppData);
		setContentView(screen);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	        //Put the code for an action menu from the top here
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onStart() {
		super.onStart();
		startThreads();
	}
	@Override
	protected void onPause() {
		super.onPause();
		stopThreads();
	}
	@Override
	protected void onStop() {
		super.onStop();
		stopThreads();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopThreads();
	}
	private void startThreads() {
		// init tonnetz panel thread
		tonnetzPanel = new TonnetzPanel(sharedAppData);
		tonnetzPanel.setRunning(true);
		tonnetzPanel.start();
		// init audio
		audio = new Audio(sharedAppData);
		audio.setRunning(true);
		audio.start();
	}
	private void stopThreads() {
		tonnetzPanel.setRunning(false);
		audio.setRunning(false);
	}
}
