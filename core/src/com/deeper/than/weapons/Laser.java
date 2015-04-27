package com.deeper.than.weapons;

import com.badlogic.gdx.graphics.Color;

public class Laser extends Weapon {	
	private Color beamColor;
	public Laser(String name, float accuracy, float baseDamage,
			float critDamage, float critChance, float rechargeSpeed,
			float baseMonetaryCost, float powerCost, Color beamColor) {
		super(name, accuracy, baseDamage, critDamage, critChance, rechargeSpeed,
				baseMonetaryCost, powerCost);
		this.beamColor = beamColor;
		// TODO Auto-generated constructor stub
	}
	public Color getBeamColor() {
		return beamColor;
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
