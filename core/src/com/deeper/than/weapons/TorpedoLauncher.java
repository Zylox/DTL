package com.deeper.than.weapons;

public class TorpedoLauncher extends Weapon{
	private float torpedoSpeed;
	
	public TorpedoLauncher(String name, float accuracy, float baseDamage,
			float critDamage, float critChance, float rechargeSpeed,
			float baseMonetaryCost, float powerCost, float torpedoSpeed) {
		super(name, accuracy, baseDamage, critDamage, critChance, rechargeSpeed,
				baseMonetaryCost, powerCost);
		// TODO Auto-generated constructor stub
		this.torpedoSpeed = torpedoSpeed;
	}

	public float getTorpedoSpeed() {
		return torpedoSpeed;
	}

	public static WeaponParams getBaseParams(){
		WeaponParams params = new WeaponParams();
		params.accuracy = 1;
		params.baseDamage = 1;
		params.critDamage = 1;
		params.critChance = 1;
		params.rechargeSpeed = 20; //%/sec
		params.baseMonetaryCost = 30;
		params.powerCost = 1;
		return params;
	}
}
