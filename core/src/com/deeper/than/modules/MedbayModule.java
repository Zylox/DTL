package com.deeper.than.modules;
import java.util.ArrayList;

import com.deeper.than.DTL;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.Crew;

/**
 * Holds all values relevant to healing crew when in the medbay.
 * @author zach
 *
 */
public class MedbayModule extends MainModule {
	
	/**
	 * Multiplier values for levels 0-3 of the module
	 */
	private static final float healBoost[] = {0,1f,1.5f, 3.0f};
	private static final float baseHeal = 5;
	
	/**
	 * Creates a Medbay module at level 1
	 * @param id module id
	 * @param room room containing the module. Set to null if no room contains it
	 * @param ship ship containing the module. Set to null if no ship owns the module
	 */
	public MedbayModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
	}

	/**
	 * Creates a Medbay module at specified level
	 * @param id module id
	 * @param level level of the module
	 * @param room room containing the module. Set to null if no room contains it
	 * @param ship ship containing the module. Set to null if no ship owns the module
	 */
	public MedbayModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
	}
	
	/**
	 * Gets the heal for the timestep the game is running at
	 * @return heal amount per timestep
	 */
	public float getHealRatePerTimeStep(){
		float healMulitplier = 0;
		if(getPowerLevel() <= healBoost.length+1 && getPowerLevel() >= 0){
			healMulitplier = healBoost[getPowerLevel()];
		}
		return DTL.getRatePerTimeStep(baseHeal*healMulitplier);
	}
	
	@Override
	public void update(){
		super.update();
		ArrayList<Crew> crewInRoom = getRoom().getThisShipsCrewInRoom();
		for(Crew c : crewInRoom){
			c.setHealth(c.getHealth() + getHealRatePerTimeStep());
		}
	}

	
}
