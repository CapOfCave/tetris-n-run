package data;

public class RawSpawner {

	int x;
	int y;
	int loff;
	int toff;
	int roff;
	int boff;
	int max;
	boolean tetroonly;
	double rate;
	
	public RawSpawner(int x, int y, int loff, int toff, int roff, int boff, int max, boolean tetroonly, double rate) {
		super();
		this.x = x;
		this.y = y;
		this.loff = loff;
		this.toff = toff;
		this.roff = roff;
		this.boff = boff;
		this.max = max;
		this.tetroonly = tetroonly;
		this.rate = rate;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getLoff() {
		return loff;
	}

	public int getToff() {
		return toff;
	}

	public int getRoff() {
		return roff;
	}

	public int getBoff() {
		return boff;
	}

	public int getMax() {
		return max;
	}

	public boolean isTetroonly() {
		return tetroonly;
	}

	public double getRate() {
		return rate;
	}
}
