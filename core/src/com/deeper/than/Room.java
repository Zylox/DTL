package com.deeper.than;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.deeper.than.crew.Crew;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.SensorsModule;
import com.deeper.than.screens.GameplayScreen;

/**
 * A room is a collection of gridsquares in a ship that occupy the same "room".
 * @author zach
 *
 */
public class Room {
	
	//start swimming at 50%
	public static final float SWIM_THRESHOLD = .5f;
	
	//Initial constants
	private static final float BASETEMP = 50;
	private static final float BASEPRESSURE = 50;
	private static final float BASEWATERLEVEL= 0;
	
	/**Rate that water flows between doors*/
	private static final float WATERTRANSMISSIONRATE = .005f;
	
	//Ship this room belongs to
	private Ship ship;
	
	/**The squares that make up the rooms.*/
	private ArrayList<GridSquare> squares;
	/**The module in the room if one exists*/
	private Module module;
	private int id;
	
	private float temp;
	private float pressure;
	/**Current water level recognized by the game*/
	private float waterLevel;
	/**Water level that has been calcuated for next frame*/
	private float calculatedWaterLevel;
	/**Amount of water given to adjacent rooms this cylce.*/
	private float givenWater;
	/**The highest waterlevel a neighbor has that is open to this room.
	*The new water level can't be higher than its highest open neighbors.*/
	private float neighBorUpperboundWL;
	/**Sensor module reference to determine visibility.*/
	private SensorsModule sensors;
	
	/**
	 * Center location in global coordinates
	 */
	private Vector2 centerLoc;
	private Vector2 bottomLeft;
	private Vector2 bounds;
	
	/**All rooms linked to this one*/
	private ArrayList<RoomLink> linkedRooms;
	
	private boolean isHoveredOver;
	
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
		bottomLeft = null;
		bounds = null;
		sensors = null;
		
		isHoveredOver = false;
	}
	
	public ArrayList<Crew> getRepairCandidates(){
		ArrayList<Crew> cands = new ArrayList<Crew>();
		for(GridSquare gs : squares){
			cands.addAll(gs.getCrewOnSquare());
		}
		return cands;
	}
	
	public GridSquare selectTileToWalkTo(){
		for(GridSquare g : squares){
			if(!g.hasCrewMember()){
				return g;
			}
		}
		return squares.get(0);
	}
	
	public ArrayList<Crew> getCrewInRoom(){
		ArrayList<Crew> crewInRoom = new ArrayList<Crew>();
		for(GridSquare gs : squares){
			crewInRoom.addAll(gs.getCrewOnSquare());
		}
		return crewInRoom;
	}
	
	public ArrayList<Crew> getThisShipsCrewInRoom(){
		ArrayList<Crew> thisShipsCrewInRoom = new ArrayList<Crew>();
		for(GridSquare gs : squares){
			for(Crew c: gs.getCrewOnSquare()){
				if(c.getOwnerShip() == this.ship){
					thisShipsCrewInRoom.add(c);
				}
			}
		}
		return thisShipsCrewInRoom;
	}
	
	/**
	 * Get the location in global coords
	 * @return centerLoc
	 */
	public Vector2 getCenterLoc(){
		if(centerLoc == null){
			calculateBounds();
		}
		return centerLoc;
	}
	
	/**
	 * Calculates the center in global coords.
	 * @return Global coord center location.
	 */
	private void calculateBounds(){
		//"infinite"
		float minX = 1000000;
		float minY = 1000000;
		float maxX = 0;
		float maxY = 0;
		//Find the botom left and top right corner essentially.
		//This means rroms have to be square as far as modules are concerned.
		for(GridSquare square : squares){
			minX = Math.min(minX, square.getX());
			minY = Math.min(minY, square.getY());
			maxX = Math.max(maxX, square.getX() + square.getWidth());
			maxY = Math.max(maxY, square.getY() + square.getHeight());
		}

		centerLoc = new Vector2();
		bottomLeft= new Vector2();
		bounds = new Vector2();
		centerLoc.x = ship.getX() +  minX + (maxX-minX)/2;
		centerLoc.y = ship.getY() + minY + (maxY-minY)/2;
		bottomLeft.x = ship.getX() + minX;
		bottomLeft.y = ship.getY() + minY;
		bounds.x = maxX-minX;
		bounds.y = maxY-minY;
	}
	
	public void drawHover(Batch batch){
		if(bottomLeft == null){
			calculateBounds();
		}
		Texture wall = GameplayScreen.highlight;
		Color color = batch.getColor();
		batch.setColor(Color.YELLOW);
		//wall.setColor(Color.CYAN);
		float offset = Wall.INTERIORWALLSHORTSIDE/2f;
		batch.draw(wall, bottomLeft.x+offset, bottomLeft.y + offset, Wall.INTERIORWALLSHORTSIDE, bounds.y - offset * 2);
		batch.draw(wall, bottomLeft.x + bounds.x-Wall.INTERIORWALLSHORTSIDE-offset, bottomLeft.y + offset, Wall.INTERIORWALLSHORTSIDE, bounds.y - offset * 2);
		batch.draw(wall, bottomLeft.x+offset, bottomLeft.y+offset, bounds.x - offset * 2, Wall.INTERIORWALLSHORTSIDE);
		batch.draw(wall, bottomLeft.x+offset, bottomLeft.y + bounds.y - Wall.INTERIORWALLSHORTSIDE-offset, bounds.x - offset * 2, Wall.INTERIORWALLSHORTSIDE);
		batch.setColor(color);
	}

	/**
	 * Calculates Enviroment conditions for the next cycle.
	 * Currently this means water level
	 */
	public void calculateEnv(){
		calculateWaterLevel();
	}
	
	/**
	 * Set the values taht were previously calculated.
	 */
	public void updateEnv(){
		//Update calculated level accounting for the given water amount.
		setCalculatedWaterLevel(getCalculatedWaterLevel() - getGivenWater(), neighBorUpperboundWL);
		//Reset values
		setGivenWater(0);
		setNeighBorUpperboundWL(1000000);
		//Ayy Lmao - Nick Ferguson, 2014
		setWaterLevel(calculatedWaterLevel);
	}
	
	private void calculateWaterLevel(){
		float waterLevelAcc = 0;
		RoomLink link = null;
		Room linkedRoom = null;
		float neighWaterLevel = 0;
		float highestWaterLevel = 1000000000;
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
					float transmission =DTL.getRatePerTimeStep((neighWaterLevel * WATERTRANSMISSIONRATE * 60));
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
			waterLevelAcc = DTL.getRatePerTimeStep(ship.getDrainRate()*60);
		}

		setCalculatedWaterLevel(getWaterLevel() + waterLevelAcc + DTL.getRatePerTimeStep(ship.getDrainRate()*60) , highestWaterLevel);
			
		
		
	}
	
	public Vector2 getManningLocation(){
		return squares.get(0).getPos();
	}
	
	public boolean isWaterSwimHeight(){
		if(getWaterLevel()/100 > SWIM_THRESHOLD){
			return true;
		}
		return false;
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
		if(ship instanceof EnemyShip){
			if(sensors != null){
				if(sensors.canSeeEnemyShip()){
					return true;
				}
			}
		}
		if(sensors == null){
			return isPlayerCrewInRoom();
		}
		return sensors.canSeeOwnShip() || isPlayerCrewInRoom();
	}
	
	public boolean isPlayerCrewInRoom(){
		for(Crew c : getCrewInRoom()){
			if(c.getOwnerShip() instanceof PlayerShip){
				return true;
			}
		}
		return false;
	}
	
	public int getSize(){
		return squares.size();
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
	
	public int getId(){
		return id;
	}

	public boolean isHoveredOver() {
		return isHoveredOver;
	}

	public void setHoveredOver(boolean isHoveredOver) {
		this.isHoveredOver = isHoveredOver;
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
