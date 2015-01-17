package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class CloakingModule extends MainModule {

	private boolean isCloaked;
	
	public CloakingModule(int id, Room room, Ship ship) {
		super(id, room, ship);
		isCloaked = false;
	}

	public CloakingModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
		isCloaked = false;
	}
	
	public boolean isCloaked(){
		return isCloaked;
	}
	
	public void setCloaked(boolean isCloaked){
		this.isCloaked = isCloaked;
	}

}
