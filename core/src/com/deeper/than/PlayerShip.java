/**
 * Player ship implementation that holds things that only the player ship will have like currency and feul
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than;


import com.badlogic.gdx.files.FileHandle;
import com.deeper.than.weapons.WeaponGenerator;

/**
 * Player ship instance
 * @author zach
 *
 */
public class PlayerShip extends Ship {

	private int currency;
	private int fuel;
	
	public PlayerShip(FileHandle filepath, boolean isPlayerShip, DTL game,
			int id, WeaponGenerator weaponGen) throws ShipLoadException {
		super(filepath, isPlayerShip, game, id, weaponGen);
	}
	
	
	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public boolean canSeeEnemyShipInterior(){
		if(this.sensors != null){
			return sensors.canSeeEnemyShip();
		}
		return false;
	}
	
	
}
