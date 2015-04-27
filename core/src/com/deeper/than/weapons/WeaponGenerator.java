package com.deeper.than.weapons;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

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
	
	private static Color beamColors[] = {Color.GREEN, Color.BLUE, Color.RED, Color.CYAN, Color.PURPLE, Color.PINK, Color.WHITE, Color.YELLOW};
	
	private Random ran;
	private WeaponQualityDistribution dist;
	
	public WeaponGenerator(){
		ran = new Random();
		init();
	}
	
	public WeaponGenerator(long seed){
		ran = new Random(seed);
		init();
	}
	
	private void init(){
		dist = new WeaponQualityDistribution();
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
	
	private String getPrefix(WeaponQualities quality, WeaponMakers maker){
		return quality.getPrefix() + " " + maker.getPrefix();
	}
	
	private Color getRandomColor(){
		return beamColors[ran.nextInt(beamColors.length)];
	}
	
	private float getTorpedoSpeed(){
		float min = 1;
		float max = 4;
		return getValueInRange(min, max);
	}
	
	public BeamWeapon generateBeamWeapon(){
		WeaponParams params = BeamWeapon.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new BeamWeapon(getPrefix(params.quality, params.maker) + " Beam", params, getRandomColor());
	}
	
	public ConcussionBomb generateConcussionBomb(){
		WeaponParams params = ConcussionBomb.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new ConcussionBomb(getPrefix(params.quality, params.maker) + " Bomb", params);
	}
	
	public EMPShot generateEmpShot(){
		WeaponParams params = EMPShot.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new EMPShot(getPrefix(params.quality, params.maker) + " EMP Shot", params);
	}
	
	public Laser generateLaser(){
		WeaponParams params = Laser.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new Laser(getPrefix(params.quality, params.maker) + " Laser", params, getRandomColor());
	}
	
	public RailGun generRailGun(){
		WeaponParams params = RailGun.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new RailGun(getPrefix(params.quality, params.maker) + " RailGun", params);
	}
	public SuperCoolingBeam generateSuperCoolingBeam(){
		WeaponParams params = SuperCoolingBeam.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new SuperCoolingBeam(getPrefix(params.quality, params.maker) + " SuperCooling Beam", params);
	}
	
	public TorpedoLauncher generateTorpedoLauncher(){
		WeaponParams params = TorpedoLauncher.getBaseParams();
		params.quality = dist.getRandomQualityByDist();
		params.maker = WeaponMakers.getRandomMaker();
		params.maker.modifyWeaponParams(params);
		return new TorpedoLauncher(getPrefix(params.quality, params.maker) + " Beam", params, getTorpedoSpeed());
	}
	
}
