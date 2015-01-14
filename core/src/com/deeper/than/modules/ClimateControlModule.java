package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class ClimateControlModule extends Module {
	
	private static final String name = "ClimateControlModule";
	protected static final String imagePath = "climateControl.png";
	
	public ClimateControlModule(int id, Room room,Ship ship) {
		super(id, room, ship);
	}
	
	public ClimateControlModule(int id, int level, Room room,Ship ship) {
		super(id, level, room, ship);
	}
	
	public float getWaterDrainRate(){
		float waterDrainRate = 0;
		
		if(getLevel() == 1){
			waterDrainRate = -.01f;
		}else if(getLevel() == 2){
			waterDrainRate = -.02f;
		}else if(getLevel() == 3){
			waterDrainRate = -.05f;
		}
		
		return waterDrainRate;
	}


}
