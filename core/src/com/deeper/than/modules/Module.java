package com.deeper.than.modules;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.Crew;

public abstract class Module {
	
	private final String name = "baseModule";
	
	private int id;
	private Ship ship;
	private Room room;
	private int minRoomSize;
	private int level;
	private boolean manned;
	private Crew manning;
	
	public Module(int id, Room room, Ship ship){
		this(id, 1, room, ship);
	}
	
	public Module(int id, int level, Room room, Ship ship){
		this.id = id;
		this.level = level;
		this.room = room;
		this.ship = ship;
		manned = false;
		manning = null;
	}
	

	public void draw(Batch batch){
		Sprite icon = Modules.getIcon(this.getClass().getCanonicalName()); 
		batch.draw(icon, room.getCenterLoc().x-icon.getWidth()/2, room.getCenterLoc().y-icon.getHeight()/2);
	}
	
	public boolean isManned() {
		return manned;
	}

	public void setManned(boolean manned) {
		this.manned = manned;
	}

	public Crew getManning() {
		return manning;
	}

	public void setManning(Crew manning) {
		this.manning = manning;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getMinRoomSize() {
		return minRoomSize;
	}
	
	public String getName(){
		return name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
