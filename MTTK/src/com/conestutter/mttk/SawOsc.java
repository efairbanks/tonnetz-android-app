package com.conestutter.mttk;

public class SawOsc extends BlackBox {
	
	private double _index;
	private double _delta;
	private double _amplitude;
	
	public SawOsc(int samplerate)
	{
		super(samplerate);
		
		this._index = 0;
		this._delta = 440/samplerate;
		this._amplitude = 0.3162; // -10dB
	}
	
	public void setFrequency(double frequency)
	{
		this._delta = frequency/this._sampleRate;
	}
	
	public void setAmplitude(double amplitude)
	{
		this._amplitude = amplitude;
	}
	
	@Override
	public double Process() {
		
		double out = this._index*this._amplitude;

		this._index += this._delta;
		while(this._index>1) this._index -= 1;
		
		return out;

	}

}

