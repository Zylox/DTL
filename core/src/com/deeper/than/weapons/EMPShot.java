/**
 * Emp weapon that can sap sheilds and lock down modules but does no damage
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.deeper.than.Room;
import com.deeper.than.screens.GameplayScreen;

/**
 * Emp weapon that can sap sheilds and lock down modules but does no damage
 * @author zach
 *
 */
public class EMPShot extends Weapon{

	private static Sound empShot;
	
	public static void loadAssets(){
		empShot = Gdx.audio.newSound(Gdx.files.internal("sounds/empshot.wav"));
	}
	
	public EMPShot(String name, WeaponParams params) {
		super(name, params);
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


	/**
	 * Fire the emp
	 * @see com.deeper.than.weapons.Weapon#fire()
	 */
	@Override
	public void fire(Room target) {
		Projectile proj = super.createProjectile();
		Vector2 start = this.stageToLocalCoordinates((getFireOrigin().cpy()));
		proj.setStart(start);
		proj.setDestination(this.stageToLocalCoordinates(target.getCenterLocInStage().cpy()), new WeaponHitAction(this,this.didHit(target), target), 1); //1 is seconds till hit
		proj.setColor(Color.YELLOW);
		this.addActor(proj);	
		empShot.play();
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
		return GameplayScreen.highlight;
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#getProjectileWidth()
	 */
	@Override
	public float getProjectileWidth() {
		return 15;
	}


	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#getProjectileHeight()
	 */
	@Override
	public float getProjectileHeight() {
		return 8;
	}

}
