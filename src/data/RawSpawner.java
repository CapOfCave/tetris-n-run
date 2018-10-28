package data;

public class RawSpawner {

	private int x;
	private int y;
	private int loff;
	private int toff;
	private int roff;
	private int boff;
	private int max;
	private boolean tetroonly;
	private double rate;
	private boolean start;

	public RawSpawner(int x, int y, int loff, int toff, int roff, int boff, int max, boolean tetroonly, double rate,
			boolean start) {
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
		this.start = start;
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
	
	public boolean getStart() {
		return start;
	}
}
