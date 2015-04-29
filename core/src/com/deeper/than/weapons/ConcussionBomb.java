package com.deeper.than.weapons;

public class ConcussionBomb extends Weapon{

	public ConcussionBomb(String name, WeaponParams params) {
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

	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#fire()
	 */
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		
	}

}
