package com.deeper.than.weapons;

public class TorpedoLauncher extends Weapon{
	private float torpedoSpeed;
	
	public TorpedoLauncher(String name, WeaponParams params, float torpedoSpeed) {
		super(name, params);
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
