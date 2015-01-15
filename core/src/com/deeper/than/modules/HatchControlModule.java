package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class HatchControlModule extends SubModule {

	public HatchControlModule(int id, Room room, Ship ship) {
		super(id, 1, room, ship);
	}
	
	public HatchControlModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
	}

	public boolean canControlDoors(){
		if(getLevel() > 0){
			return true;
		}
		return false;
	}
	
	public boolean canEnemiesUseDoors(){
		if(getLevel() >= 3){
			return true;
		}
		return false;
	}

}
