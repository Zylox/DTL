package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class ClimateControlModule extends MainModule {
	
	private static final String name = "ClimateControlModule";
	
	public ClimateControlModule(int id, int maxLevel, Room room,Ship ship) {
		super(id, maxLevel, room, ship);
	}
	
	public ClimateControlModule(int id, int level, int maxLevel, Room room,Ship ship) {
		super(id, level, maxLevel, room, ship);
	}
	
	public float getWaterDrainRate(){
		float waterDrainRate = 0;
		
		if(getPowerLevel() == 1){
			waterDrainRate = -.01f;
		}else if(getPowerLevel() == 2){
			waterDrainRate = -.02f;
		}else if(getPowerLevel() == 3){
			waterDrainRate = -.05f;
		}
		return waterDrainRate;
	}



}
