package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class EngineModule extends MainModule{
	
	private static final float dodgeChances[] = {0,.05f,.1f,.15f,.20f,.25f,.28f,.31f,.35f}; 
	private static final float driveChargeModifiers[] = {0,1,1.25f,1.5f,1.75f,2f,2.25f,2.5f,2.75f};
	
	public EngineModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		manable = true;
	}
	
	
	public EngineModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		manable = true;
	}	

	public float getEvasionChance(){
		float dodge = 0;
		if(getPowerLevel() <= dodgeChances.length+1 && getPowerLevel() >= 0){
			dodge += dodgeChances[getPowerLevel()];
		}
		if(isManned()){
			dodge += getManning().getEngineEvadeRatio();
		}
		return dodge;
	}
	
	public boolean enginesOn(){
		return getPowerLevel() > 0;
	}
	
	public float getDriveChargeModifier(){
		float driveChargeMod = 0;
		if(getPowerLevel() <= driveChargeModifiers.length+1 && getPowerLevel() >= 0){
			driveChargeMod = driveChargeModifiers[getPowerLevel()];
		}
		return driveChargeMod;
	}
	
	public static float[] getDodgeChances(){
		return dodgeChances;
	}
	
	public static float[] getDriveChargeModifiers(){
		return driveChargeModifiers;
	}


}
