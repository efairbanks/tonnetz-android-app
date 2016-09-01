package com.conestutter.mttk;

public class SimpleVoice extends BlackBox
{
	private SawOsc _oscillator;
	private Line _attackEnvelope;
	private Line _decayEnvelope;
	
	public SimpleVoice(int samplerate)
	{
		super(samplerate);
		
		this._oscillator = new SawOsc(this._sampleRate);
		this._attackEnvelope = new Line(this._sampleRate);
		this._decayEnvelope = new Line(this._sampleRate);
	}

	public void PlayNote(double midinote, double amplitude, double duration)
	{
		double frequency = Math.pow(2,(midinote+3)/12)*27.5;
	
		this._oscillator.setFrequency(frequency);
		
		this._attackEnvelope.Reset(0, amplitude, 0.005);
		
		this._decayEnvelope.Reset(amplitude, 0, duration);
	}
	
	@Override
	public double Process()
	{
		double out = this._oscillator.Process() * this._attackEnvelope.Process() * this._decayEnvelope.Process();
		
		return out;
	}
}
