package com.example.testapp;

public class TonnetzGrid {
	public int rows;
	public int columns;
	public TonnetzGrid(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
	}
	public int rowcol2midi(int row, int column) {
		int midiNote = (row/2) + (column*7);
		if(row%2==1) midiNote += 7;
		return midiNote;
	}
	public int rowcol2x(int row, int column, int width) {
		int x = (width*column)/columns;
		if(row%2==1) x += (width/columns)/2;
		return x;
	}
	public int rowcol2y(int row, int column, int height) {
		int y = (height*row)/rows;
		return y;
	}
	public int xy2row(int x, int y, int width, int height) {
		int row = ((y+((height/rows)/2))*rows)/height;
		return row;
	}
	public int xy2col(int x, int y, int width, int height) {
		int row = xy2row(x,y,width,height);
		int xoffset = ((row%2)*(width/columns))/2;
		int column = (((x+((width/columns)/2))-xoffset)*columns)/width;
		return column;
	}
}
