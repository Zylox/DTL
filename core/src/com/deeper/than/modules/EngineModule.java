package com.deeper.than.modules;

import com.deeper.than.Room;
import com.deeper.than.Ship;

public class EngineModule extends MainModule{
	
	private static final int dodgeChances[] = {0,5,10,15,20,25,28,31,35}; 
	private static final float driveChargeModifiers[] = {0,1,1.25f,1.5f,1.75f,2f,2.25f,2.5f,2.75f};
	
	public EngineModule(int id, int level, Room room, Ship ship) {
		super(id, level, room, ship);
	}
	
	public EngineModule(int id, Room room, Ship ship) {
		super(id, 1, room, ship);
	}
	
	public float getDodgeChance(){
		float dodge = 0;
		if(getLevel() <= dodgeChances.length+1 && getLevel() >= 0){
			dodge = dodgeChances[getLevel()];
		}
		return dodge;
	}
	
	public float getDriveChargeModifier(){
		float driveChargeMod = 0;
		if(getLevel() <= driveChargeModifiers.length+1 && getLevel() >= 0){
			driveChargeMod = driveChargeModifiers[getLevel()];
		}
		return driveChargeMod;
	}
	
	public static int[] getDodgeChances(){
		return dodgeChances;
	}
	
	public static float[] getDriveChargeModifiers(){
		return driveChargeModifiers;
	}
}
