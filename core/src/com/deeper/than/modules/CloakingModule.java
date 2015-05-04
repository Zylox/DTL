/**
 * Lets a ship cloak, rasing evade
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * Cloaking modlue for a ship
 * @author zach
 *
 */
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

	public void cloak(){
		
	}

}
