package com.deeper.than;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class GridSquare extends Group{
	
	private FloorTile tile;
	private CellBorder[] borders;
	private Crew crewMember;
	private Vector2 pos;


	private Room room;
	
	private Ship ship;
	
	public GridSquare(){
		crewMember = null;
		tile = null;
		borders = new CellBorder[4];
		room = null;
		setDebug(DTL.DEBUG);
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
				printWalls();
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
	
	public void printWalls(){
		if(borders[Neighbors.UP] == null){
			System.out.println("Up: null");
		}else{
			System.out.println("Up: " + borders[Neighbors.UP].getClass() + " pos: " +borders[Neighbors.UP].getX() + " " + borders[Neighbors.UP].getY());	
		}
		
		if(borders[Neighbors.RIGHT] == null){
			System.out.println("Right: null");
		}else{
			System.out.println("Right: " + borders[Neighbors.RIGHT].getClass() + " pos: " +borders[Neighbors.RIGHT].getX() + " " + borders[Neighbors.RIGHT].getY());	
		}
		
		if(borders[Neighbors.DOWN] == null){
			System.out.println("Down: null");
		}else{
			System.out.println("Down: " + borders[Neighbors.DOWN].getClass() + " pos: " +borders[Neighbors.DOWN].getX() + " " + borders[Neighbors.DOWN].getY());	
		}
		
		if(borders[Neighbors.LEFT] == null){
			System.out.println("Left: null");
		}else{
			System.out.println("Left: " + borders[Neighbors.LEFT].getClass() + " pos: " +borders[Neighbors.LEFT].getX() + " " + borders[Neighbors.LEFT].getY());	
		}
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
}
