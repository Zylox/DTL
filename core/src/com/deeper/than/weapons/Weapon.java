package com.deeper.than.weapons;

public class Weapon {


	private String name;
	private float accuracy;
	private float baseDamage;
	private float critDamage;
	private float critChance;
	private float rechargeSpeed;
	private float baseMonetaryCost;
	private float powerCost;
	
	public Weapon(String name, float accuracy, float baseDamage, float critDamage, float critChance, float rechargeSpeed, float baseMonetaryCost, float powerCost){
		this.name = name;
		this.accuracy = accuracy;
		this.baseDamage = baseDamage;
		this.critDamage = critDamage;
		this.critChance = critChance;
		this.rechargeSpeed = rechargeSpeed;
		this.baseMonetaryCost = baseMonetaryCost;
		this.powerCost = powerCost;
	}

	public String getName() {
		return name;
	}

	public float getAccuracy() {
		return accuracy;
	}

	public float getBaseDamage() {
		return baseDamage;
	}

	public float getCritDamage() {
		return critDamage;
	}

	public float getCritChance() {
		return critChance;
	}

	public float getRechargeSpeed() {
		return rechargeSpeed;
	}

	public float getBaseMonetaryCost() {
		return baseMonetaryCost;
	}

	public float getPowerCost() {
		return powerCost;
	}
	
	
}
