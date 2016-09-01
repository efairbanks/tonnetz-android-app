package com.conestutter.mttk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.SurfaceHolder;

public class TonnetzPanel extends Thread {
	SharedAppData sharedAppData;
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	SurfaceHolder surfaceHolder;
	private boolean run;
	public int rows;
	public int columns;
	public int xoff;
	public int yoff;
	
	public TonnetzPanel(SharedAppData sharedAppData) {
		this.sharedAppData = sharedAppData;
		synchronized(sharedAppData) {
			sharedAppData.tonnetzPanelRunning = true;
		}
		this.rows = 8;
		this.columns = 12;
		this.xoff = 40;
		this.yoff = 90;
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
	    canvas.drawColor(Color.BLACK);
		int x, y, row, col, width, height;
		width = canvas.getWidth();
		height = canvas.getHeight();
		synchronized(sharedAppData) {
			x = sharedAppData.x;
			y = sharedAppData.y;
		}
		row = this.xy2row(x, y, width, height);
		col = this.xy2col(x, y, width, height);
	    List<int[]> coords = xy2tri(x,y,width,height);
		synchronized(sharedAppData) {
			sharedAppData.midiNotes.clear();
			for(int[] coord : coords) {
				sharedAppData.midiNotes.add(rowcol2midi(coord[0],coord[1]));
			}
			if(sharedAppData.press==true) sharedAppData.newNote = sharedAppData.press;
			sharedAppData.press = false;
		}
	    paint.setStyle(Style.STROKE);
	    for(int i = 0; i < this.columns; i++) {
	    	for(int j = 0; j < this.rows; j++) {
	    		boolean rowMod = (j%2)==1;
    			paint.setColor(Color.rgb(150, 255, 150));
	    		canvas.drawLine(this.rowcol2x(j, i, width),
	    						this.rowcol2y(j, i, height),
	    						this.rowcol2x(j+1, i, width),
	    						this.rowcol2y(j+1, i, height),
	    						paint);
	    		canvas.drawLine(this.rowcol2x(j, i, width),
								this.rowcol2y(j, i, height),
								this.rowcol2x(j, i+1, width),
								this.rowcol2y(j, i+1, height),
								paint);
	    		if(rowMod) {
		    		canvas.drawLine(this.rowcol2x(j, i, width),
							this.rowcol2y(j, i, height),
							this.rowcol2x(j+1, i+1, width),
							this.rowcol2y(j+1, i+1, height),
							paint);
	    		} else {
		    		canvas.drawLine(this.rowcol2x(j, i+1, width),
							this.rowcol2y(j, i+1, height),
							this.rowcol2x(j+1, i, width),
							this.rowcol2y(j+1, i, height),
							paint);

	    		}
	    	}
	    }
	    paint.setStyle(Style.FILL);
	    for(int i = 0; i < this.columns; i++) {
	    	for(int j = 0; j < this.rows; j++) {
    			paint.setColor(Color.rgb(150, 255, 150));
    			canvas.drawCircle(	this.rowcol2x(j, i, width),
									this.rowcol2y(j, i, height),
									30, paint);
	    	}
	    }
	    for(int[] coord : coords) {
	    	paint.setColor(Color.rgb(255, 255, 255));
			canvas.drawCircle(	this.rowcol2x(coord[0], coord[1], width),
								this.rowcol2y(coord[0], coord[1], height),
								35, paint);
	    }
	    paint.setStyle(Style.FILL);
	    for(int i = 0; i < this.columns; i++) {
	    	for(int j = 0; j < this.rows; j++) {
				paint.setTextAlign(Align.CENTER);
				paint.setTextSize(30);
				paint.setFakeBoldText(true);
				paint.setColor(Color.rgb(0, 0, 0));
				canvas.drawText(note2string(rowcol2midi(j, i)),
								this.rowcol2x(j, i, width),
								this.rowcol2y(j, i, height)+10,
								paint);
	    	}
	    }
	}
	private String note2string(Integer midiNote) {
		String ret = "";
		switch(midiNote%12) {
		case 0:
			ret = "C";
			break;
		case 1:
			ret = "C#";
			break;
		case 2:
			ret = "D";
			break;
		case 3:
			ret = "D#";
			break;
		case 4:
			ret = "E";
			break;
		case 5:
			ret = "F";
			break;
		case 6:
			ret = "F#";
			break;
		case 7:
			ret = "G";
			break;
		case 8:
			ret = "G#";
			break;
		case 9:
			ret = "A";
			break;
		case 10:
			ret = "A#";
			break;
		case 11:
			ret = "B";
			break;
		}
		return ret;
	}
	private int rowcol2midi(int row, int column) {
		int midiNote = (row/2) + (column*7);
		if(row%2==1) midiNote += 4;
		return midiNote;
	}
	private int rowcol2x(int row, int column, int width) {
		int x = (width*column)/columns;
		if(row%2==1) x += (width/columns)/2;
		return x+this.xoff;
	}
	private int rowcol2y(int row, int column, int height) {
		int y = (height*row)/rows;
		return y+this.yoff;
	}
	private List<int[]> xy2tri(int x, int y, int width, int height) {
		List<float[]> rowColDistance = new ArrayList<float[]>();
		List<int[]> rowCol = new ArrayList<int[]>();
		for(int i = 0; i < this.columns; i++) {
	    	for(int j = 0; j < this.rows; j++) {
	    		int x1 = x;
	    		int y1 = y;
	    		int x2 = this.rowcol2x(j, i, width);
	    		int y2 = this.rowcol2y(j, i, height);
	    		float dist = distance(x1,y1,x2,y2);
	    		float[] listAdd = { (float)j, (float)i, dist};
	    		rowColDistance.add(listAdd);
	    	}
	    }
		Collections.sort(rowColDistance, new Comparator<float[]>() {
			@Override
			public int compare(float[] a, float[] b) {
				return a[2] > b[2] ? 1 : -1;
				//return Math.random() > 0.5 ? 1 : 0;
			}
		});
		for(int i = 0; i < 3 && i < rowColDistance.size(); i++) {
			int[] newRowCol = { (int)rowColDistance.get(i)[0], (int)rowColDistance.get(i)[1] };
			rowCol.add(newRowCol);
		}
		return rowCol;
	}
	private float distance(int x1, int y1, int x2, int y2) {
		return (float)Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2-y1, 2));
	}
	private int xy2row(int x, int y, int width, int height) {
		int row = ((y+((height/rows)/2))*rows)/height;
		return row;
	}
	private int xy2col(int x, int y, int width, int height) {
		int row = xy2row(x,y,width,height);
		int xoffset = ((row%2)*(width/columns))/2;
		int column = (((x+((width/columns)/2))-xoffset)*columns)/width;
		return column;
	}
}
