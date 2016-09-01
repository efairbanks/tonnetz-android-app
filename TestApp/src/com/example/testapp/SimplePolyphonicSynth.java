package com.example.testapp;

public class SimplePolyphonicSynth extends BlackBox
{
	
	private SimpleVoice[] _voices;
	
	private int _voiceIndex;
	
	public SimplePolyphonicSynth(int samplerate, int voices)
	{
		super(samplerate);
		
		this._voices = new SimpleVoice[voices];
		
		for(int i = 0; i < this._voices.length; i++) this._voices[i] = new SimpleVoice(this._sampleRate);
		
		this._voiceIndex = 0;
	}
	
	public void PlayNote(double midinote, double amplitude, double duration)
	{
		this._voices[this._voiceIndex].PlayNote(midinote, amplitude, duration);
		
		this._voiceIndex = (this._voiceIndex + 1) % this._voices.length;
	}
	
	@Override
	public double Process()
	{
		double out = 0;
		
		for(int i = 0; i < this._voices.length; i++) out += (this._voices[i].Process()/this._voices.length);
		
		return out;
	}

}
