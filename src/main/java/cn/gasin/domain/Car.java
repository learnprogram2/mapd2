package cn.gasin.domain;

import cn.gasin.enums.CarType;

public class Car {

	private String make;
	private int numberOfSeats;
	private CarType type;

	//constructor, getters, setters etc.


	public Car(String make, int numberOfSeats, CarType type) {
		this.make = make;
		this.numberOfSeats = numberOfSeats;
		this.type = type;
	}

	public Car() {
	}

	public String getMake() {
		return make;
	}

	public int getNumberOfSeats() {
		return numberOfSeats;
	}

	public CarType getType() {
		return type;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public void setNumberOfSeats(int numberOfSeats) {
		this.numberOfSeats = numberOfSeats;
	}

	public void setType(CarType type) {
		this.type = type;
	}
}