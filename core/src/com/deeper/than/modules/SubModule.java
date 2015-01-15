package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public abstract class SubModule extends Module {

	public SubModule(int id, Room room, Ship ship) {
		super(id, room, ship);
		// TODO Auto-generated constructor stub
	}

	public SubModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
		// TODO Auto-generated constructor stub
	}

}
