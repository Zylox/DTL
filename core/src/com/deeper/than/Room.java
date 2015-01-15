package com.deeper.than;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.SensorsModule;

public class Room {
	
	private static final float BASETEMP = 50;
	private static final float BASEPRESSURE = 50;
	private static final float BASEWATERLEVEL= 0;
	
	private static final float WATERTRANSMISSIONRATE = .005f;
	
	private Ship ship;
	
	private ArrayList<GridSquare> squares;
	private Module module;
	private int id;
	
	private float temp;
	private float pressure;
	private float waterLevel;
	private float calculatedWaterLevel;
	private float givenWater;
	private float neighBorUpperboundWL;
	private SensorsModule sensors;
	/**
	 * Center location in global coordinates
	 */
	private Vector2 centerLoc;

	private ArrayList<RoomLink> linkedRooms;
	
	public Room(int id, Ship ship){
		this.id = id;
		this.ship = ship;
		this.module = null;
		squares = new ArrayList<GridSquare>();
		linkedRooms = new ArrayList<RoomLink>();
		temp = BASETEMP;
		pressure = BASEPRESSURE;
		waterLevel = BASEWATERLEVEL;
		centerLoc = null;
	}
	
	/**
	 * Get the location in global coords
	 * @return centerLoc
	 */
	public Vector2 getCenterLoc(){
		if(centerLoc == null){
			centerLoc = calculateCenter();
		}
		return centerLoc;
	}
	
	private Vector2 calculateCenter(){
		Vector2 center = new Vector2();
		float minX = 1000000;
		float minY = 1000000;
		float maxX = 0;
		float maxY = 0;
		for(GridSquare square : squares){
			minX = Math.min(minX, square.getX());
			minY = Math.min(minY, square.getY());
			maxX = Math.max(maxX, square.getX() + square.getWidth());
			maxY = Math.max(maxY, square.getY() + square.getHeight());
		}
		center.x = ship.getX() +  minX + (maxX-minX)/2;
		center.y = ship.getY() + minY + (maxY-minY)/2;
		return center;
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
	
	public void addSquare(GridSquare square){
		squares.add(square);
	}
	
	public void setSensorsModule(SensorsModule sensors){
		this.sensors = sensors;
	}
	
	public boolean isVisible(){
		if(sensors == null){
			return false;
		}
		return sensors.canSeeOwnShip() || ship.isCrewInRoom(this);
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
	
	public void setModule(Module module){
		this.module = module;
	}
	
	public Module getModule(){
		return module;
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
