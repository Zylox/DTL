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

	


}
