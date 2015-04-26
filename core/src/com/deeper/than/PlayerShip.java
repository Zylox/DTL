package com.deeper.than;

import com.badlogic.gdx.files.FileHandle;

public class PlayerShip extends Ship {

	public PlayerShip(FileHandle filepath, boolean isPlayerShip, DTL game,
			int id) {
		super(filepath, isPlayerShip, game, id);
		// TODO Auto-generated constructor stub
	}

	
	public int getCurrencyAmt(){
		return 100;
	}
	
	public int getFuel(){
		return 17;
	}
	
	public boolean canSeeEnemyShipInterior(){
		if(this.sensors != null){
			return sensors.canSeeEnemyShip();
		}
		return false;
	}
	
	
}
