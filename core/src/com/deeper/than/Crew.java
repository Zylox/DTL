package com.deeper.than;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Crew extends Actor{
	
	private static final String crewImage = "firstDraftHuman.png";

	private Ship ship;
	private Room room;
	private Vector2 tilePos;
	private String name;
	private Sprite crewSprite;
	
	
	public Crew(){
		
	}
	
	public Room getRoom(){
		return room;
	}
	
	public void setRoom(Room room){
		this.room = room;
	}
	
	private String getRandomCrewImage(){
		return crewImage;
	}
	
	public void draw(){
		
	}
}
