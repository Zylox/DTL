package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class CloakingModule extends MainModule {

	private boolean isCloaked;
	
	public CloakingModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		isCloaked = false;
	}

	public CloakingModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		isCloaked = false;
	}
	
	public boolean isCloaked(){
		return isCloaked;
	}
	
	public void setCloaked(boolean isCloaked){
		this.isCloaked = isCloaked;
	}


}
