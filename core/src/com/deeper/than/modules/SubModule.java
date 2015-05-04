/**
 * Module for modules that are not variable power
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * Module for non varaible power mods
 * @author zach
 *
 */
public abstract class SubModule extends Module {

	public SubModule(int id,int maxLevel, Room room, Ship ship) {
		super(id, maxLevel,room, ship);
	}

	public SubModule(int id,int maxLevel, int level, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
	}

}
