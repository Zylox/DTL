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
	

}
