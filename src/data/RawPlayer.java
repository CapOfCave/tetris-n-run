package data;

import java.io.Serializable;

import logics.Inventory;

public class RawPlayer implements Serializable{

	private static final long serialVersionUID = 1L;
	private double acc, brake, maxSpeed;
	private int health;
	private Inventory inventory;
	
	
	public RawPlayer(double acc, double brake2, double maxSpeed2, int health, Inventory inventory) {
		
		this.acc = acc;
		this.brake = brake2;
		this.maxSpeed = maxSpeed2;
		this.health = health;
		this.inventory = inventory;
	}

	public void init(){
		inventory.init();
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


	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	public Inventory getInventory() {
		return inventory;
	}


	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	
}
