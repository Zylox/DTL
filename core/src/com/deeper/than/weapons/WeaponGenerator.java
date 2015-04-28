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
	
	/**
	 * @return the dist
	 */
	public WeaponQualityDistribution getQualityDist() {
		return dist;
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
		return generate(type, dist.getRandomQualityByDist(), WeaponMakers.getRandomMaker());
	}
	
	public Weapon generate(WeaponTypes type, WeaponQualities quality){
		return generate(type, quality, WeaponMakers.getRandomMaker());
	}
	
	public Weapon generate(WeaponTypes type, WeaponMakers make){
		return generate(type, dist.getRandomQualityByDist(), make);
	}
	
	
	
	public Weapon generate(WeaponTypes type, WeaponQualities quality, WeaponMakers make){
		
		switch(type){
		case BEAM_WEAPON:
			return generateBeamWeapon(quality, make);
		case CONCUSSION_BOMB:
			return generateConcussionBomb(quality, make);
		case EMP_WEAPON:
			return generateEmpShot(quality, make);
		case LASER:
			return generateLaser(quality, make);
		case RAILGUN:
			return generRailGun(quality, make);
		case SUPERCOOLING_BEAM:
			return generateSuperCoolingBeam(quality, make);
		case TORPEDO_LAUNCHER:
			return generateTorpedoLauncher(quality, make);
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
	
	public BeamWeapon generateBeamWeapon(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = BeamWeapon.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new BeamWeapon(getPrefix(params.quality, params.maker) + " Beam", params, getRandomColor());
	}
	
	public ConcussionBomb generateConcussionBomb(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = ConcussionBomb.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new ConcussionBomb(getPrefix(params.quality, params.maker) + " Bomb", params);
	}
	
	public EMPShot generateEmpShot(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = EMPShot.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new EMPShot(getPrefix(params.quality, params.maker) + " EMP Shot", params);
	}
	
	public Laser generateLaser(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = Laser.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new Laser(getPrefix(params.quality, params.maker) + " Laser", params, getRandomColor());
	}
	
	public RailGun generRailGun(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = RailGun.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new RailGun(getPrefix(params.quality, params.maker) + " RailGun", params);
	}
	public SuperCoolingBeam generateSuperCoolingBeam(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = SuperCoolingBeam.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new SuperCoolingBeam(getPrefix(params.quality, params.maker) + " SuperCooling Beam", params);
	}
	
	public TorpedoLauncher generateTorpedoLauncher(WeaponQualities quality, WeaponMakers make){
		WeaponParams params = TorpedoLauncher.getBaseParams();
		params.quality = quality;
		params.maker = make;
		params.maker.modifyWeaponParams(params);
		return new TorpedoLauncher(getPrefix(params.quality, params.maker) + " Torpedo", params, getTorpedoSpeed());
	}
	
}
