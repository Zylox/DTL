package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;


public class SensorsModule extends SubModule {
	
	public SensorsModule(int id, Room room, Ship ship) {
		super(id, room, ship);
	}

	public SensorsModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
	}
	
	public boolean canSeeOwnShip(){
		if(getLevel() > 0){
			return true;
		}
		return false;
	}
	
	public boolean canSeeEnemyShip(){
		if(getLevel() >= 2 ){
			return true;
		}
		return false;
	}
	
	public boolean canSeeEnemyWeaponCharge(){
		if(getLevel() >= 3){
			return true;
		}
		return false;
	}
	
	public boolean canSeeEnemyPowerUse(){
		if(getLevel() >= 3 && isManned()){
			return true;
		}
		return false;
	}
}
