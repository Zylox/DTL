package com.deeper.than.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.deeper.than.Room;
import com.deeper.than.screens.GameplayScreen;

public class Laser extends Weapon {

	private static Sound laserShot;
	
	public static void loadAssets(){
		laserShot = Gdx.audio.newSound(Gdx.files.internal("sounds/laserShot.wav"));
	}
	
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
	public void fire(Room target) {
		Projectile proj = super.createProjectile();
//		Vector2 start = this.screenToLocalCoordinates(getFireOrigin().cpy());
		Vector2 start = this.stageToLocalCoordinates((getFireOrigin().cpy()));
		proj.setStart(start);
		proj.setDestination(this.stageToLocalCoordinates(target.getCenterLocInStage().cpy()), new WeaponHitAction(this,this.didHit(target), target), 1);
		proj.setColor(beamColor);
		this.addActor(proj);
		laserShot.play();
	}
	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#onhit()
	 */
	@Override
	public void onhit(Room target) {
		target.takeDamage(this.getDamage());
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
		return 20;
	}
	/* (non-Javadoc)
	 * @see com.deeper.than.weapons.Weapon#getProjectileHeight()
	 */
	@Override
	public float getProjectileHeight() {
		return 4;
	}
	
}
