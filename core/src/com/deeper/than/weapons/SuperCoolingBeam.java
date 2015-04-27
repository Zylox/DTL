package com.deeper.than.weapons;

public class SuperCoolingBeam extends Weapon{

	public SuperCoolingBeam(String name, WeaponParams params) {
		super(name, params);
		// TODO Auto-generated constructor stub
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
