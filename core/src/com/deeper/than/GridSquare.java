package com.deeper.than;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public class GridSquare extends Group{
	
	private FloorTile tile;
	private Door[] doors;
	private Crew crewMember;
	private Vector2 pos;
	
	private Ship ship;
	
	public GridSquare(){
		crewMember = null;
		tile = null;
		doors = new Door[4];
		setDebug(DTL.DEBUG);
	}
	
	public void addDoor(Door door){
		doors[door.orientation] = door;
		door.setGridSquare(this);
		//addActor(door);
		door.initDoors();
	}
	
	public void setFloorTile(FloorTile tile){
		this.tile = tile;
		setBounds(tile.pos.x*FloorTile.TILESIZE, tile.pos.y*FloorTile.TILESIZE, FloorTile.TILESIZE, FloorTile.TILESIZE);
		tile.setBounds(0, 0, getWidth(), getHeight());
		addActor(tile);
		pos = tile.pos;
	}
	
	public void setCrewMember(Crew crew){
		this.crewMember = crew;
	}
	
	
	public void setShip(Ship ship){
		this.ship = ship;
	}
	
	public FloorTile getFloorTile(){
		return tile;
	}
	
	
}
