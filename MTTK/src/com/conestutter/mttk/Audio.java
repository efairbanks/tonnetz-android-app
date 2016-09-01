package com.conestutter.mttk;

import java.util.ArrayList;
import java.util.List;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Audio extends Thread {
	SharedAppData sharedAppData;
	private boolean run;
	int sampleRate = 44100;
	boolean isRunning = true;
	List<Integer> midiNotes;
	boolean newNote;
	
	public Audio(SharedAppData sharedAppData) {
		this.sharedAppData = sharedAppData;
		synchronized(sharedAppData) {
			sharedAppData.audioRunning = true;
		}
		this.midiNotes = new ArrayList<Integer>();
		this.newNote = false;
	}
	public void run() {
		setPriority(Thread.MAX_PRIORITY);
		int buffsize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		//create an audiotrack object
		AudioTrack audioTrack = new AudioTrack(	AudioManager.STREAM_MUSIC,
												sampleRate, 
												AudioFormat.CHANNEL_OUT_MONO, 
												AudioFormat.ENCODING_PCM_16BIT, 
												buffsize, 
												AudioTrack.MODE_STREAM);
		short samples[] = new short[buffsize];
		int amp = 10000;
		int frames = 0;
		int beat = 0;
		SimplePolyphonicSynth synth = new SimplePolyphonicSynth(sampleRate,10);
		// start audio
		audioTrack.play();
		// synthesis loop
		while(run){
			this.midiNotes.clear();
			synchronized(sharedAppData) {
				for(Integer midiNote : sharedAppData.midiNotes) {
					this.midiNotes.add(midiNote);
				}
				this.newNote = sharedAppData.newNote;
				sharedAppData.newNote = false;
			}
			// press playnote
			if(this.newNote){
				for(Integer midiNote : midiNotes) {
					Integer playNote = midiNote;
					while(playNote < 48) playNote += 12;
					while(playNote > 60) playNote -= 12; 
					synth.PlayNote(playNote, 4, 2);
				}
			}
			// a beat is 1/4 a second
			if(frames>=(sampleRate/4))
			{
				frames = 0;
				//int[] minorPentatonic = { 0, 3, 5, 7, 10 };
				//int scaleIndex = (int)(Math.random()*minorPentatonic.length);
				//synth.PlayNote(60+minorPentatonic[scaleIndex], 1, 2);
				// play chord on every eighth beat
				/*
				if(beat%8==0)
				{
					for(int i = 0; i < 3; i++)
					{
						synth.PlayNote(48+minorPentatonic[(scaleIndex+i)%minorPentatonic.length], 1, 3);
					}
				}
				*/
				beat++;
			}
		   for(int i=0; i < buffsize; i++){ 
		     samples[i] = (short) (synth.Process()*amp);
		   }
		   frames += buffsize;
		   audioTrack.write(samples, 0, buffsize);
		}
		return;
	}
	public void setRunning(boolean b) { 
	    run = b;
	}
}
