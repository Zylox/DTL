package com.deeper.than;

import java.util.ArrayList;

public class Room {
	private ArrayList<GridSquare> squares;
	private int id;
	
	public Room(int id){
		this.id = id;
		squares = new ArrayList<GridSquare>();
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
}
