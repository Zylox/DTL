package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public abstract class SubModule extends Module {

	public SubModule(int id,int maxLevel, Room room, Ship ship) {
		super(id, maxLevel,room, ship);
		// TODO Auto-generated constructor stub
	}

	public SubModule(int id,int maxLevel, int level, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		// TODO Auto-generated constructor stub
	}

}
