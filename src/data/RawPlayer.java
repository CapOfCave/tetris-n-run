package data;

import java.io.Serializable;

public class RawPlayer implements Serializable {

	private static final long serialVersionUID = 1L;
	private double acc, brake, maxSpeed;

	public RawPlayer(double acc, double brake2, double maxSpeed2) {

		this.acc = acc;
		this.brake = brake2;
		this.maxSpeed = maxSpeed2;
	}

	public void init() {
	}

	public double getAcc() {
		return acc;
	}

	public void setAcc(double acc) {
		this.acc = acc;
	}

	public double getBrake() {
		return brake;
	}

	public void setBrake(int brake) {
		this.brake = brake;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}


}
