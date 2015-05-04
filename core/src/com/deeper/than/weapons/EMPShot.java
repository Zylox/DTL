package com.deeper.than.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.deeper.than.Room;
import com.deeper.than.screens.GameplayScreen;

public class EMPShot extends Weapon{

	public EMPShot(String name, WeaponParams params) {
		super(name, params);
		// TODO Auto-generated constructor stub
	}


	public static WeaponParams getBaseParams(){
		WeaponParams params = new WeaponParams();
		params.accuracy = 1;
		params.baseDamage = 1;
		params.critDamage = 1;
		params.critChance = 1;
		params.rechargeSpeed = 5; //%/sec
		params.baseMonetaryCost = 30;
		params.powerCost = 1;
		return params;
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#fire()
	 */
	@Override
	public void fire(Room target) {
		Projectile proj = super.createProjectile();
		Vector2 start = this.stageToLocalCoordinates((getFireOrigin().cpy()));
		proj.setStart(start);
		proj.setDestination(this.stageToLocalCoordinates(target.getCenterLocInStage().cpy()), new WeaponHitAction(this,this.didHit(target), target), 1);
		proj.setColor(Color.YELLOW);
		this.addActor(proj);		
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#onhit()
	 */
	@Override
	public void onhit(Room target) {
		target.takeIonDamage(getDamage());
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#getProjectileImage()
	 */
	@Override
	public Texture getProjectileImage() {
		// TODO Auto-generated method stub
		return GameplayScreen.highlight;
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#getProjectileWidth()
	 */
	@Override
	public float getProjectileWidth() {
		// TODO Auto-generated method stub
		return 15;
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#getProjectileHeight()
	 */
	@Override
	public float getProjectileHeight() {
		// TODO Auto-generated method stub
		return 8;
	}

}
