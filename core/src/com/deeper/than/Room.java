package com.deeper.than;

import java.util.ArrayList;

public class Room {
	
	private static final float BASETEMP = 50;
	private static final float BASEPRESSURE = 50;
	private static final float BASEWATERLEVEL= 0;
	
	private static final float WATERTRANSMISSIONRATE = .005f;
	
	private Ship ship;
	
	private ArrayList<GridSquare> squares;
	private int id;
	
	private float temp;
	private float pressure;
	private float waterLevel;
	private float calculatedWaterLevel;
	private float givenWater;
	private float neighBorUpperboundWL;


	private ArrayList<RoomLink> linkedRooms;
	
	public Room(int id, Ship ship){
		this.id = id;
		this.ship = ship;
		squares = new ArrayList<GridSquare>();
		linkedRooms = new ArrayList<RoomLink>();
		temp = BASETEMP;
		pressure = BASEPRESSURE;
		waterLevel = BASEWATERLEVEL;
	}
	
	public void calculateEnv(){
		calculateWaterLevel();
	}
	
	public void updateEnv(){
		setCalculatedWaterLevel(getCalculatedWaterLevel() - getGivenWater(), neighBorUpperboundWL);
		setGivenWater(0);
		setNeighBorUpperboundWL(1000000);
		setWaterLevel(calculatedWaterLevel);
	}
	
	private void calculateWaterLevel(){
		float waterLevelAcc = 0;
		RoomLink link = null;
		Room linkedRoom = null;
		float neighWaterLevel = 0;
		float highestWaterLevel = 100;
		boolean roomClosed = true;

		int i;
		for(i = 0; i<linkedRooms.size(); i++){
			
			link = linkedRooms.get(i);
			if(link.getDoor().isOpen()){
				roomClosed = false;
				linkedRoom = link.getLinkedRoom();
				
				if(linkedRoom == null){
					neighWaterLevel = 100; 
				}else{
					neighWaterLevel = linkedRoom.getWaterLevel();
					
				}
				
				if(neighWaterLevel > this.waterLevel){
					float transmission =DTL.getRatePerFrame((neighWaterLevel * WATERTRANSMISSIONRATE * 60));
					if(waterLevelAcc+transmission+getWaterLevel() > neighWaterLevel-transmission){
						transmission =(neighWaterLevel-(waterLevelAcc+getWaterLevel()))/2;
					}
			
					waterLevelAcc +=  transmission;
					if(linkedRoom != null){
//						if(transmission > .00000000000009f){
							linkedRoom.addGivenWater(transmission);
//						}
					}
					highestWaterLevel = Math.max(neighWaterLevel, highestWaterLevel);
				}
			}
		}

		if(roomClosed){
			waterLevelAcc = DTL.getRatePerFrame(ship.getDrainRate()*60);
		}

		setCalculatedWaterLevel(getWaterLevel() + waterLevelAcc + DTL.getRatePerFrame(ship.getDrainRate()*60) , highestWaterLevel);
			
		
		
	}
	
	public void addLink(Room room, Door door){
		//not zelda
		linkedRooms.add(new RoomLink(room, door));
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
//		if(waterLevel - (int)waterLevel > .99f){
//			waterLevel = (float)Math.ceil(waterLevel);
//		}
		if(waterLevel > neighBorUpperboundWL){
			this.waterLevel = neighBorUpperboundWL;
		}else if(waterLevel < 0){
			this.waterLevel = 0;
		}
	}
	
	public float getCalculatedWaterLevel() {
		return calculatedWaterLevel;
	}
	
	public void setCalculatedWaterLevel(float waterLevel, float upperBound) {
		this.calculatedWaterLevel = waterLevel;
		this.neighBorUpperboundWL= upperBound;
	}
	
	
	
	public float getGivenWater() {
		return givenWater;
	}

	public void setGivenWater(float givenWater) {
		this.givenWater = givenWater;
	}
	
	public void addGivenWater(float add){
		this.givenWater += add;
	}
	
	public float getNeighBorUpperboundWL() {
		return neighBorUpperboundWL;
	}

	public void setNeighBorUpperboundWL(float neighBorUpperboundWL) {
		this.neighBorUpperboundWL = neighBorUpperboundWL;
	}

	private class RoomLink{
		Room linkedRoom;
		Door door;
		
		public RoomLink(Room linkedRoom, Door door){
			this.linkedRoom = linkedRoom;
			this.door = door;
		}

		public Room getLinkedRoom() {
			return linkedRoom;
		}

		public void setLinkedRoom(Room linkedRoom) {
			this.linkedRoom = linkedRoom;
		}

		public Door getDoor() {
			return door;
		}

		public void setDoor(Door door) {
			this.door = door;
		}

		

	}
}
