package com.deeper.than.weapons;

import com.badlogic.gdx.graphics.Color;

public class Laser extends Weapon {	
	private Color beamColor;
	public Laser(String name, WeaponParams params, Color beamColor) {
		super(name, params);
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
		params.rechargeSpeed = 5; //sec
		params.baseMonetaryCost = 30;
		params.powerCost = 1;
		return params;
	}
	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#fire()
	 */
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		
	}
	
}
