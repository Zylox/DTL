package com.deeper.than;

public enum Neighbors {
	INTERNALWALL,
	EXTERNALWALL,
	DOOR,
	TILE;

	//this should really be an enum, i know, im in too deep though.
	//refactor one day
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int UPPER_RIGHT = 4;
	public static final int UPPER_LEFT = 5;
	public static final int LOWER_RIGHT = 6;
	public static final int LOWER_LEFT = 7;
	
	public static final int UNDEFINED = -1;
	
	public static int reverseOrienation(int orient){
		if(orient == UP){
			return DOWN;
		}
		
		if(orient == RIGHT){
			return LEFT;
		}
		
		if(orient == DOWN){
			return UP;
		}
		
		if(orient == LEFT){
			return RIGHT;
		}
		
		return -1;
	}
	
}
