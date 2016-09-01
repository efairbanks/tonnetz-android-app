package com.conestutter.mttk;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;

public class MenuPanel {
	SharedAppData sharedAppData;
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	SurfaceHolder surfaceHolder;
	private boolean run;
	
	public MenuPanel() {
		this.sharedAppData = sharedAppData;
		synchronized(sharedAppData) {
			sharedAppData.tonnetzPanelRunning = true;
		}
	}
	public void run() {
		if(this.surfaceHolder==null)
			synchronized(sharedAppData) {
				this.surfaceHolder = sharedAppData.surfaceHolder;
			}
		while (run) {
			Canvas c = null;
			try {
				c = surfaceHolder.lockCanvas(null);
				if(c!=null)
					synchronized (surfaceHolder) {
						doDraw(c);
					}
			} finally {
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
			// --- //
			try {
				Thread.sleep(1000/30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setRunning(boolean b) { 
	    run = b;
	}
	private void doDraw(Canvas canvas) {
	    canvas.drawColor(Color.BLUE);
		int x, y, row, col, width, height;
		width = canvas.getWidth();
		height = canvas.getHeight();
		synchronized(sharedAppData) {
			x = sharedAppData.x;
			y = sharedAppData.y;
		}
	}
}
