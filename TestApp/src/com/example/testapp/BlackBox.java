package com.example.testapp;

public abstract class BlackBox {
	
	protected int _sampleRate;
	
	public BlackBox(int samplerate)
	{
		this._sampleRate = samplerate;
	}
	
	public abstract double Process();
	
}
