/**
 * The container for the grid spot in a ship
 * holds walls and the floor
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.Crew.CrewState;
import com.deeper.than.screens.GameplayScreen;

/**
 * The container for the ship componenets at a spot
 * @author zach
 *
 */
public class GridSquare extends Group{
	
	private FloorTile tile;
	//the walls
	private CellBorder[] borders;
	private ArrayList<Crew> crewOnSquare;
	private Vector2 pos;
	//if this square is the spot to stand for manning or not.
	private boolean isManningStation;
	
	//items used for pathfinding
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
		hValue = 0;
		gValue = 0;
		pathClosed = false;
		onOpenList = false;
		onClosedList = false;
		isManningStation = false;
		setDebug(DTL.GRAPHICALDEBUG);
		pathPointer = this;
	}
	
	public boolean isSameRoom(GridSquare square){
		if(room.getRoomId() == square.getRoomId()){
			return true;
		}
		return false;
	}

	/**
	 * Adds a border to the container. Will overwrite the old one if it exists
	 * @param border
	 */
	public void addBorder(CellBorder border){
		borders[border.orientation] = border;
	}
	
	public CellBorder getBorder(int orientation){
		CellBorder border = borders[orientation];
		return border;
	}
	
	/**
	 * Returns door at orientation if one exists, else null
	 * @param orientation
	 * @return
	 */
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
	
	/**
	 * Sets the floortile and conforms itself to the tiles size
	 * @param tile
	 */
	public void setFloorTile(FloorTile tile){
		this.tile = tile;
		setBounds(tile.pos.x*FloorTile.TILESIZE, tile.pos.y*FloorTile.TILESIZE, FloorTile.TILESIZE, FloorTile.TILESIZE);
		tile.setBounds(0, 0, getWidth(), getHeight());
		addActor(tile);
		pos = tile.pos;
	}
	
	/**
	 * returns the orientation relative to to provided gridsquare
	 * @param adj
	 * @return
	 */
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
	
	/**
	 * Adds door in a reflected orientation
	 * @param doorToReflect
	 */
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

	public Vector2 pathPointerEndPos(){
		return pathPointer.getPos();
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
	
	/**
	 * Gets how many crew are standing on tile, and thus not just moving through it. if this is more than 1 it should be resolved
	 * @return crew standing on tile
	 */
	public int crewStandingCount(){
		int count = 0;
		for(Crew c : crewOnSquare){
			if(c.getState() == CrewState.IDLE || c.getState() == CrewState.MANNING){
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Gets all but the first person who was standing on the square originally
	 * @return
	 */
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
	
	/**
	 * Sets hvalue using the Manhattan heuristic
	 * @param dest
	 */
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
