package com.conestutter.mttk;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Screen extends SurfaceView implements SurfaceHolder.Callback  {
	private SurfaceHolder surfaceHolder;
	private SharedAppData sharedAppData;
	
	public Screen(Context context, SharedAppData sharedAppData) {
		super(context);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		synchronized(sharedAppData) {
			sharedAppData.surfaceHolder = surfaceHolder;
		}
		this.sharedAppData = sharedAppData;
	}
	public void surfaceCreated(SurfaceHolder holder) {

	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				synchronized(sharedAppData) {
					sharedAppData.x = (int) event.getX();
					sharedAppData.y = (int) event.getY();
					sharedAppData.press = true;
				}
			break;
			default:
			break;
		}
		return true;
	}
}
