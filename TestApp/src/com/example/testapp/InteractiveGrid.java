package com.example.testapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class InteractiveGrid extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder sh;
	private Grid thread;
	
	public InteractiveGrid(Context context) {
		super(context);
		sh = getHolder();
	    sh.addCallback(this);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new Grid(sh);
	    thread.setRunning(true);
	    thread.start();
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				synchronized(thread) {
					thread.x = (int) event.getX();
					thread.y = (int) event.getY();
				}
			break;
			default:
			break;
		}
		return true;
	}
}
