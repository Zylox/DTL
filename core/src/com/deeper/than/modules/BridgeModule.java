package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class BridgeModule extends SubModule {

	private static final float evasionRetentions[] = {0,0,.5f,.8f};
	
	public BridgeModule(int id, Room room, Ship ship) {
		super(id, room, ship);
	}

	public BridgeModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
	}
	
	public float getEvasionRetention(){
		if(isManned()){
			return 1f;
		}
		
		if(getLevel() < evasionRetentions.length+1 && getLevel() >= 0){
			return evasionRetentions[getLevel()];
		}
		return 0;
	}
	
	public static float[] getEvasionRetentions(){
		return evasionRetentions;
	}

}
