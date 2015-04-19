package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class BridgeModule extends SubModule {

	private static final float evasionRetentions[] = {0,0,.5f,.8f};
	
	public BridgeModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
	}

	public BridgeModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
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
