package com.deeper.than.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deeper.than.DTL;
import com.deeper.than.Room;
import com.deeper.than.Ship;

public abstract class Module {
	
	private final String name = "baseModule";
	protected static final String imagePath = "badModule.png";
	private static Sprite icon;
	
	private int id;
	private Ship ship;
	private Room room;
	private int minRoomSize;
	private int level;
	
	public Module(int id, Room room, Ship ship){
		this(id, 1, room, ship);
	}
	
	public Module(int id, int level, Room room, Ship ship){
		this.id = id;
		this.level = level;
		this.room = room;
		this.ship = ship;
	}
	
	public static void loadAssets(String path){
		icon = new Sprite(new Texture(Gdx.files.internal(path)));
	}
	public static void loadAssets(){
		loadAssets("badModule.png");
	}
	
	public void draw(Batch batch){
	
		batch.draw(icon, room.getCenterLoc().x-icon.getWidth()/2, room.getCenterLoc().y-icon.getHeight()/2);
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
