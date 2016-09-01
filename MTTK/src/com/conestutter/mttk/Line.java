package com.conestutter.mttk;

public class Line extends BlackBox {

	private double _from, _to, _delta, _index;
	
	public Line(int samplerate)
	{
		super(samplerate);
		
		this._index = -1;
		this._from = 0;
		this._to = 0;
		this._delta = 0;
	}
	
	public void Reset(double from, double to, double duration)
	{
		this._from = from;
		this._to = to;
		this._delta = 1/(this._sampleRate*duration);
		this._index = 0;
	}
	
	@Override
	public double Process() {
		
		double out = this._to;
		
		if(this._index >= 1) this._index = -1;
		
		if(this._index > -1)
		{
			out = (this._index*this._to) + ((1-this._index)*this._from);
			
			this._index += this._delta;
		}
		
		return out;
		
	}

}
