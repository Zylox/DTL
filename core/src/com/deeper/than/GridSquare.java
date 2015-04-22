package com.deeper.than;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.Crew.CrewState;

public class GridSquare extends Group{
	
	private FloorTile tile;
	private CellBorder[] borders;
	//private Crew crewMember;
	private ArrayList<Crew> crewOnSquare;
	private Vector2 pos;
	private boolean isManningStation;
	
	private GridSquare pathPointer;
	private float hValue;
	private float gValue;
	private boolean pathClosed;
	private boolean onClosedList;
	private boolean onOpenList;

	private Room room;
	
	private Ship ship;
	
	public GridSquare(){
		//crewMember = null;
		crewOnSquare = new ArrayList<Crew>();
		tile = null;
		borders = new CellBorder[4];
		room = null;
		pathPointer = null;
		hValue = 0;
		gValue = 0;
		pathClosed = false;
		onOpenList = false;
		onClosedList = false;
		isManningStation = false;
		setDebug(DTL.GRAPHICALDEBUG);
	}
	
	public boolean isSameRoom(GridSquare square){
		if(room.getRoomId() == square.getRoomId()){
			return true;
		}
		return false;
	}
	
	public void addBorder(CellBorder border){
		borders[border.orientation] = border;
		//border.setGridSquare(this);
		//addActor(door);
		//border.init();
	}
	
	public CellBorder getBorder(int orientation){
		CellBorder border = borders[orientation];
		return border;
	}
	
	public Door getDoor(int orientation){
		CellBorder border = borders[orientation];
		if(border instanceof Door){
			return (Door)border;
		}
		return null;
	}
	
	public boolean hasDoor(int direction){
		if(borders[direction] != null){
			if(borders[direction] instanceof Door){
				return true;
			}
		}
		return false;
	}
	
	public void setFloorTile(FloorTile tile){
		this.tile = tile;
		setBounds(tile.pos.x*FloorTile.TILESIZE, tile.pos.y*FloorTile.TILESIZE, FloorTile.TILESIZE, FloorTile.TILESIZE);
		tile.setBounds(0, 0, getWidth(), getHeight());
		addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				//printWalls();
				//DTL.printDebug("WaterLevel: " + room.getWaterLevel());
				return false;
		    }
		});
		addActor(tile);
		pos = tile.pos;
	}
	
	public int getRelativeOrientation(GridSquare adj){
		if(pos.y == adj.getPos().y){
			//if in line
			if(pos.x < adj.getPos().x){
				//if left
				return Neighbors.LEFT;
			}else if(pos.x > adj.getPos().x){
				//if right
				return Neighbors.RIGHT;
			}
		}else if(pos.y < adj.pos.y){
			//below
			if(pos.x == adj.pos.x){
				return Neighbors.DOWN;
			}
			
		}else if(pos.y > adj.pos.y){
			//above
			if(pos.x == adj.pos.x){
				return Neighbors.UP;
			}
		}
		
		return -1;
	}
	
	public void setReflectedDoor(Door doorToReflect){
		doorToReflect.getFullFlip();		
		this.addBorder(doorToReflect);
		doorToReflect.getFullFlip();
	}
	
	public void printWalls(){
		if(borders[Neighbors.UP] == null){
			DTL.printDebug("Up: null");
		}else{
			DTL.printDebug("Up: " + borders[Neighbors.UP].getClass() + " pos: " +borders[Neighbors.UP].getX() + " " + borders[Neighbors.UP].getY());	
		}
		
		if(borders[Neighbors.RIGHT] == null){
			DTL.printDebug("Right: null");
		}else{
			DTL.printDebug("Right: " + borders[Neighbors.RIGHT].getClass() + " pos: " +borders[Neighbors.RIGHT].getX() + " " + borders[Neighbors.RIGHT].getY());	
		}
		
		if(borders[Neighbors.DOWN] == null){
			DTL.printDebug("Down: null");
		}else{
			DTL.printDebug("Down: " + borders[Neighbors.DOWN].getClass() + " pos: " +borders[Neighbors.DOWN].getX() + " " + borders[Neighbors.DOWN].getY());	
		}
		
		if(borders[Neighbors.LEFT] == null){
			DTL.printDebug("Left: null");
		}else{
			DTL.printDebug("Left: " + borders[Neighbors.LEFT].getClass() + " pos: " +borders[Neighbors.LEFT].getX() + " " + borders[Neighbors.LEFT].getY());	
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		if(!room.isVisible() && !ship.colorizeRooms()){
			Color color = batch.getColor();
			batch.setColor(new Color(.5f,.5f,.5f,1f));
			batch.draw(ship.getFloorTileImg(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
			batch.setColor(color);
		}
	}
	
	public void addCrewMember(Crew crew){
		crewOnSquare.add(crew);
	}
	
	public void removeCrewMember(Crew crew){
		if(crewOnSquare.contains(crew)){
			crewOnSquare.remove(crew);
		}
	}
	
	public boolean hasCrewMember(){
		if(crewOnSquare.isEmpty()){
			return false;
		}
		return true;
	}
	
	public int crewStandingCount(){
		int count = 0;
		for(Crew c : crewOnSquare){
			if(c.getState() == CrewState.IDLE || c.getState() == CrewState.MANNING){
				count++;
			}
		}
		return count;
	}
	
	public ArrayList<Crew> intrudingStandingCrew(){
		ArrayList<Crew> iIC = new ArrayList<Crew>();
		for(int i = 1; i<crewOnSquare.size(); i++){
			if(crewOnSquare.get(i).getState() != CrewState.WALKING){
				iIC.add(crewOnSquare.get(i));
			}
		}
		return iIC;
	}
	
	public ArrayList<Crew> getCrewOnSquare(){
		return crewOnSquare;
	}
	
	public GridSquare getPathPointer(){
		return pathPointer;
	}
	
	
	public void setPathPointer(GridSquare pathPointer){
		this.pathPointer = pathPointer;
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
	}
	
	public FloorTile getFloorTile(){
		return tile;
	}
	
	public Room getRoom(){
		return room;
	}
	
	public void setRoom(Room room){
		this.room = room;
	}
	
	public int getRoomId(){
		return room.getRoomId();
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public float getHValue() {
		return hValue;
	}

	public void setHValue(float hValue) {
		this.hValue = hValue;
	}
	
	public void setHManh(Vector2 dest){
		float distance = Math.abs(dest.x-pos.x) + Math.abs(dest.y-pos.y);
		setHValue(distance);
	}

	public float getGValue() {
		return gValue;
	}

	public void setGValue(float gValue) {
		this.gValue = gValue;
	}
	
	public float getFValue(){
		return getGValue() + getHValue();
	}

	public boolean isPathClosed() {
		return pathClosed;
	}

	public void setPathClosed(boolean pathClosed) {
		this.pathClosed = pathClosed;
	}

	public boolean isOnClosedList() {
		return onClosedList;
	}

	public void setOnClosedList(boolean onClosedList) {
		this.onClosedList = onClosedList;
	}

	public boolean isOnOpenList() {
		return onOpenList;
	}

	public void setOnOpenList(boolean onOpenList) {
		this.onOpenList = onOpenList;
	}

	public boolean isManningStation() {
		return isManningStation;
	}

	public void setManningStation(boolean isManningStation) {
		this.isManningStation = isManningStation;
	}
	
	
	
	
}
