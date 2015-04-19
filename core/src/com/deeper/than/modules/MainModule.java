package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public abstract class MainModule extends Module {

	private int powerLevel;
	
	public MainModule(int id,int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		// TODO Auto-generated constructor stub
	}

	public MainModule(int id,int maxLevel, int level, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		// TODO Auto-generated constructor stub
	}

	public int getPowerLevel() {
		return powerLevel;
	}

	public void setPowerLevel(int powerLevel) {
		this.powerLevel = powerLevel;
		powerChanged();
	}
	
	public abstract void powerChanged();

}
