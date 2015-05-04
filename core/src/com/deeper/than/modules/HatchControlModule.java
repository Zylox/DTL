/**
 * Controls doors
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * Hatch control module for ships
 * @author zach
 *
 */
public class HatchControlModule extends SubModule {

	public HatchControlModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		manable = true;
	}
	
	public HatchControlModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		manable = true;
	}

	public boolean canControlDoors(){
		if(getPowerLevel() > 0){
			return true;
		}
		return false;
	}
	
	public boolean canEnemiesUseDoors(){
		if(getPowerLevel() >= 3){
			return true;
		}
		return false;
	}

}
