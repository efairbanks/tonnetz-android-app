package com.example.testapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;

public class Grid extends Thread {
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	SurfaceHolder sh;
	TonnetzGrid tonnetz;
	private boolean run;
	public int x;
	public int y;
	
	public Grid(SurfaceHolder sh) {
		this.sh = sh;
		this.tonnetz = new TonnetzGrid(18, 7);
		x = 0;
		y = 0;
	}
	public void run() {
	    while (run) {
	      Canvas c = null;
	      try {
	        c = sh.lockCanvas(null);
	        synchronized (sh) {
	          doDraw(c);
	        }
	      } finally {
	        if (c != null) {
	          sh.unlockCanvasAndPost(c);
	        }
	      }
	    }
	  }
	public void setRunning(boolean b) { 
	    run = b;
	}
	private void doDraw(Canvas canvas) {
		int x, y, row, col;
		synchronized(this) {
			x = this.x;
			y = this.y;
		}
		row = tonnetz.xy2row(x, y, canvas.getWidth(), canvas.getHeight());
		col = tonnetz.xy2col(x, y, canvas.getWidth(), canvas.getHeight());
	    //canvas.restore();
	    canvas.drawColor(Color.BLACK);
	    paint.setStyle(Style.STROKE);
	    //canvas.drawCircle(bubbleX, bubbleY, 50, paint);
	    for(int i = 0; i < tonnetz.columns; i++) {
	    	for(int j = 0; j < tonnetz.rows; j++) {
	    		boolean rowMod = (j%2)==1;
    			paint.setColor(Color.rgb(150, 255, 150));
	    		canvas.drawLine(tonnetz.rowcol2x(j, i, canvas.getWidth()),
	    						tonnetz.rowcol2y(j, i, canvas.getHeight()),
	    						tonnetz.rowcol2x(j+1, i, canvas.getWidth()),
	    						tonnetz.rowcol2y(j+1, i, canvas.getHeight()),
	    						paint);
	    		canvas.drawLine(tonnetz.rowcol2x(j, i, canvas.getWidth()),
								tonnetz.rowcol2y(j, i, canvas.getHeight()),
								tonnetz.rowcol2x(j, i+1, canvas.getWidth()),
								tonnetz.rowcol2y(j, i+1, canvas.getHeight()),
								paint);
	    		if(rowMod) {
		    		canvas.drawLine(tonnetz.rowcol2x(j, i, canvas.getWidth()),
							tonnetz.rowcol2y(j, i, canvas.getHeight()),
							tonnetz.rowcol2x(j+1, i+1, canvas.getWidth()),
							tonnetz.rowcol2y(j+1, i+1, canvas.getHeight()),
							paint);
	    		} else {
		    		canvas.drawLine(tonnetz.rowcol2x(j, i+1, canvas.getWidth()),
							tonnetz.rowcol2y(j, i+1, canvas.getHeight()),
							tonnetz.rowcol2x(j+1, i, canvas.getWidth()),
							tonnetz.rowcol2y(j+1, i, canvas.getHeight()),
							paint);

	    		}
	    	}
	    }
	    paint.setStyle(Style.FILL);
	    for(int i = 0; i < tonnetz.columns; i++) {
	    	for(int j = 0; j < tonnetz.rows; j++) {
	    		if(i==col && j==row) {
	    			paint.setColor(Color.rgb(190, 170, 255));
	    			canvas.drawCircle(	tonnetz.rowcol2x(j, i, canvas.getWidth()),
							tonnetz.rowcol2y(j, i, canvas.getHeight()),
							20, paint);
	    		} else {
	    			paint.setColor(Color.rgb(150, 255, 150));
	    			canvas.drawCircle(	tonnetz.rowcol2x(j, i, canvas.getWidth()),
							tonnetz.rowcol2y(j, i, canvas.getHeight()),
							10, paint);
	    		}
	    	}
	    }
	}
}
