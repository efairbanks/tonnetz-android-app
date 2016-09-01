package com.example.testapp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		setContentView(new InteractiveGrid(this));
		
		Thread audioView = new Thread()
		{
			int sampleRate = 44100;
			boolean isRunning = true;
			
			public void run()
			{
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
				while(isRunning){

					// a beat is 1/4 a second
					if(frames>=(sampleRate/4))
					{
						frames = 0;
						
						int[] minorPentatonic = { 0, 3, 5, 7, 10 };
						
						int scaleIndex = (int)(Math.random()*minorPentatonic.length);
						
						synth.PlayNote(60+minorPentatonic[scaleIndex], 1, 2);
						
						// play chord on every eighth beat
						if(beat%8==0)
						{
							for(int i = 0; i < 3; i++)
							{
								synth.PlayNote(48+minorPentatonic[(scaleIndex+i)%minorPentatonic.length], 1, 3);
							}
						}
						
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
		
		};
		
		audioView.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
