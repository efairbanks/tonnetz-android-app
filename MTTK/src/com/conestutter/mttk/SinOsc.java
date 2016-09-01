package com.conestutter.mttk;

public class SinOsc extends BlackBox {
	
	private double _index;
	private double _delta;
	private double _amplitude;
	
	public SinOsc(int samplerate)
	{
		super(samplerate);
		
		this._index = 0;
		this._delta = Math.PI*2*440/samplerate;
		this._amplitude = 0.3162; // -10dB
	}
	
	public void setFrequency(double frequency)
	{
		this._delta = Math.PI*2*frequency/this._sampleRate;
	}
	
	public void setAmplitude(double amplitude)
	{
		this._amplitude = amplitude;
	}
	
	@Override
	public double Process() {
		
		double out = Math.sin(this._index)*this._amplitude;

		this._index += this._delta;
		
		return out;

	}

}
