package com.deeper.than.weapons;

import java.util.Random;

public class WeaponGenerator {

	public enum WeaponTypes{
		BEAM_WEAPON,
		CONCUSSION_BOMB,
		EMP_WEAPON,
		LASER,
		RAILGUN,
		SUPERCOOLING_BEAM,
		TORPEDO_LAUNCHER;
	}
	
	private Random ran;
	
	public WeaponGenerator(){
		ran = new Random();
	}
	
	public WeaponGenerator(long seed){
		ran = new Random(seed);
	}
	
	/**
	 * Gets a value within min and max, excluding max
	 * @param min
	 * @param max
	 * @return
	 * @author zach
	 */
	public float getValueInRange(float min, float max){
		float range = max-min;
		float value = ran.nextFloat();
		value *= range;
		value += min;
		return value;
	}
	
	public Weapon generate(WeaponTypes type){
		
		switch(type){
		case BEAM_WEAPON:
			return generateBeamWeapon();
		case CONCUSSION_BOMB:
			return generateConcussionBomb();
		case EMP_WEAPON:
			return generateEmpShot();
		case LASER:
			return generateLaser();
		case RAILGUN:
			return generRailGun();
		case SUPERCOOLING_BEAM:
			return generateSuperCoolingBeam();
		case TORPEDO_LAUNCHER:
			return generateTorpedoLauncher();
		}
		
		return null;
	}
	
	public BeamWeapon generateBeamWeapon(){
		
	}
	
	public ConcussionBomb generateConcussionBomb(){
		
	}
	
	public EMPShot generateEmpShot(){
		
	}
	
	public Laser generateLaser(){
		WeaponParams params = Laser.getBaseParams();
		WeaponMakers.getRandomMaker().modifyWeaponParams(params);
		
	}
	
	public RailGun generRailGun(){
		
	}
	public SuperCoolingBeam generateSuperCoolingBeam(){
		
	}
	
	public TorpedoLauncher generateTorpedoLauncher(){
		
	}
	
}
