/**
 * Docking module for teh ship
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * A module that holds values related to being able move crew members between ships.
 * @author zach
 *
 */
public class DockingModule extends MainModule {
	/**
	 * Cooldown values based on levels 0-3;
	 */
	private static final float coolDowns[] = {-1, 20, 15, 10};
	private static final float COOLDOWNLIMIT = 100f;
	
	private Cooldown cooldown;
	
	/**
	 * Creates a DocingModule with the default level 1.
	 * @param id id of the module
	 * @param room room that contains the module. Set to null if it is not in a room
	 * @param ship ship that owns this module. set to null if not owned by a ship
	 */
	public DockingModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		cooldown = new Cooldown(COOLDOWNLIMIT);
	}

	/**
	 * Creates a DocingModule at specified level
	 * @param id id of the module
	 * @param level The level of the module
	 * @param room room that contains the module. Set to null if it is not in a room
	 * @param ship ship that owns this module. set to null if not owned by a ship
	 */
	public DockingModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		cooldown = new Cooldown(COOLDOWNLIMIT);
	}
	
	/**
	 * Sets values neccesary when firing the docking pod;
	 */
	public void fire(){
		cooldown.startCooldown();
	}
	
	/**
	 * Returns if the module is active. This means it must be above level 0.
	 * @return isActive
	 */
	public boolean isActive(){
		if(getLevel() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns cooldown status
	 * @return isOnCooldown
	 */
	public boolean isOnCooldown(){
		return cooldown.isOnCooldown();
	}
	
	/**
	 * Retrieves the cooldown period based on level.
	 * Cooldown period is the amount of time a full cooldown would take in seconds at that level
	 * @return cooldownPeriod
	 */
	private float getCooldownPeriod(){
		float coolDown = -1;
		if(getLevel() <= coolDowns.length+1 && getLevel() >= 0){
			coolDown = coolDowns[getLevel()];
		}
		return coolDown;
	}
	
	/**
	 * Advance the cooldown by the appropriate cooldown period.
	 * Returns the status of the cooldown after the increment.
	 * @return isOnCooldown
	 */
	public boolean advanceCooldown(){
		float coolDownPeriod = getCooldownPeriod();
		if(coolDownPeriod != -1){
			cooldown.advanceCooldown(COOLDOWNLIMIT/getCooldownPeriod());
		}
		return isOnCooldown();
	}

}
