package com.deeper.than.weapons;

public abstract class Weapon {

	public static final int MAX_POWER_PER_WEAPON = 4;

	private WeaponParams params;
	private String name;
	private WeaponParams qualityMods;
	
	public Weapon(String name, WeaponParams params){
		this.params = params;
		this.name = name;
		this.params.maker.modifyWeaponParams(params);
		qualityMods = params.quality.getRandomParamMods();
	}
	
	public String getParamString(){
		String paramString = "Acc:" + Float.toString(getAccuracy()) + "\n" +
							 "Damage:" + Float.toString(getBaseDamage()) + "\n" +
							 "CritDam:" + Float.toString(getCritDamage()) + "\n" +
							 "CritChan:" + Float.toString(getCritChance()) + "\n" +
							 "Recharge:" + Float.toString(getRechargeSpeed()) + "\n" +
							 "Cost:" + Float.toString(getBaseMonetaryCost()) + "\n" +
							 "Power:" + Float.toString(getPowerCost());
		return paramString;
	}

	public String getName() {
		return name;
	}
	
	public float getAccuracy() {
		if(params.maker == WeaponMakers.BOOM_N_ZOOM){
			return 1;
		}
		return params.accuracy * qualityMods.accuracy;
	}

	public float getBaseDamage() {
		return params.baseDamage * qualityMods.baseDamage;
	}

	public float getCritDamage() {
		return params.critDamage * qualityMods.critDamage;
	}

	public float getCritChance() {
		return params.critChance * qualityMods.critChance;
	}

	public float getRechargeSpeed() {
		return params.rechargeSpeed * qualityMods.rechargeSpeed;
	}

	public float getBaseMonetaryCost() {
		return (float)Math.floor(params.baseMonetaryCost * qualityMods.baseMonetaryCost);
	}

	public float getPowerCost() {
		return Math.min(params.powerCost + qualityMods.powerCost, MAX_POWER_PER_WEAPON);
	}
}
