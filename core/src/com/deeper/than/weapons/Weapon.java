package com.deeper.than.weapons;

public abstract class Weapon {


	private WeaponParams params;
	private String name;
	private WeaponParams qualityMods;
	
	public Weapon(String name, WeaponParams params){
		this.params = params;
		this.name = name;
		this.params.maker.modifyWeaponParams(params);
		qualityMods = params.quality.getRandomParamMods();
	}

	public String getName() {
		return name;
	}

	public float getAccuracy() {
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
		return params.baseMonetaryCost * qualityMods.baseMonetaryCost;
	}

	public float getPowerCost() {
		return params.powerCost + qualityMods.powerCost;
	}
}
