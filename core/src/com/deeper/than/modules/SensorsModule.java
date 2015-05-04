/**
 * Module for vision based checks
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * Sensor module for ship
 * @author zach
 *
 */
public class SensorsModule extends SubModule {
	
	public SensorsModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		manable = true;
	}

	public SensorsModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		manable = true;
	}
	
	public boolean canSeeOwnShip(){
		if(getPowerLevel() > 0){
			return true;
		}
		return false;
	}
	
	public boolean canSeeEnemyShip(){
		if(!this.isPlayerModule()){
			return false;
		}
		if(getPowerLevel() >= 2 ){
			return true;
		}
		return false;
	}
	
	public boolean canSeeEnemyWeaponCharge(){
		if(getPowerLevel() >= 3){
			return true;
		}
		return false;
	}
	
	public boolean canSeeEnemyPowerUse(){
		if(getPowerLevel() >= 3 && isManned()){
			return true;
		}
		return false;
	}
}
