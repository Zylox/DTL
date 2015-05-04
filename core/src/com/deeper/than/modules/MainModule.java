/**
 * Implementation of modules that have, primarily, variable power set by the player
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * Abstract class for modules that are main moudles
 * @author zach
 *
 */
public abstract class MainModule extends Module {

	
	public MainModule(int id,int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		// TODO Auto-generated constructor stub
	}

	public MainModule(int id,int maxLevel, int level, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		// TODO Auto-generated constructor stub
	}

	

}
