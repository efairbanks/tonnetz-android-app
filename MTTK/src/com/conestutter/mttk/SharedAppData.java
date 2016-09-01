package com.conestutter.mttk;

import java.util.ArrayList;
import java.util.List;

import android.view.SurfaceHolder;

public class SharedAppData {
	public SurfaceHolder surfaceHolder;
	public boolean tonnetzPanelRunning;
	public boolean audioRunning;
	public int x;
	public int y;
	public boolean press;
	public boolean newNote;
	public List<Integer> midiNotes;
	
	public SharedAppData() {
		surfaceHolder = null;
		tonnetzPanelRunning = false;
		audioRunning = false;
		x = 0;
		y = 0;
		midiNotes = new ArrayList<Integer>();
		press = false;
		newNote = false;
	}
}
