package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public abstract class MainModule extends Module {

	public MainModule(int id, Room room, Ship ship) {
		super(id, room, ship);
		// TODO Auto-generated constructor stub
	}

	public MainModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
		// TODO Auto-generated constructor stub
	}

}
