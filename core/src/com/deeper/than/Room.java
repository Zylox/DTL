package com.deeper.than;

import java.util.ArrayList;

public class Room {
	
	private static final float BASETEMP = 50;
	private static final float BASEPRESSURE = 50;
	private static final float BASEWATERLEVEL= 0;
	
	private ArrayList<GridSquare> squares;
	private int id;
	
	private float temp;
	private float pressure;
	private float waterLevel;
	
	public Room(int id){
		this.id = id;
		squares = new ArrayList<GridSquare>();
		temp = BASETEMP;
		pressure = BASEPRESSURE;
		waterLevel = BASEWATERLEVEL;
	}
	
	public void addRoom(GridSquare square){
		squares.add(square);
	}
	
	public int getRoomId(){
		return id;
	}
	
	public void setRoomId(int id){
		this.id = id;
	}
	
	public float getTemp() {
		return temp;
	}
	
	public void setTemp(float temp) {
		this.temp = temp;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	public float getWaterLevel() {
		return waterLevel;
	}
	
	public void setWaterLevel(float waterLevel) {
		this.waterLevel = waterLevel;
	}
}
