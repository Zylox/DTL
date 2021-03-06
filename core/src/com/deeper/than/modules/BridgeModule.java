/**
 * The module for the ships bridge. Adds evade and the ability to jump.
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */

package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.CrewSkills.CrewSkillsTypes;

/**
 * Bridge module for a ship
 * @author zach
 *
 */
public class BridgeModule extends SubModule {

	private static final float evasionRetentions[] = {0,0,.5f,.8f};
	private static final float EVASION_BASE = 0;
	
	
	public BridgeModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		manable = true;
	}

	public BridgeModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		manable = true;
	}
	
	public float getBaseEvasionRate(){
		float rate = EVASION_BASE;
		if(isManned()){
			rate += this.getManning().getBridgeEvadeRatio();
		}
		return rate;
	}
	
	public float getEvasionRetention(){
		if(isManned()){
			return 1f;
		}
		
		if(getPowerLevel() < evasionRetentions.length+1 && getLevel() >= 0){
			return evasionRetentions[getPowerLevel()];
		}
		return 0;
	}
	
	public void giveEvadeExp(){
		if(this.isManned()){
			this.getManning().gainExp(1, CrewSkillsTypes.BRIDGE);;
		}
	}
	
	public static float[] getEvasionRetentions(){
		return evasionRetentions;
	}
	
	public boolean canJump(){
		return isManned() && getPowerLevel()>0;
	}

}
