package com.deeper.than.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.deeper.than.Room;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

public abstract class Weapon extends Group{

	public static final int MAX_POWER_PER_WEAPON = 4;

	private WeaponParams params;
	private String name;
	private WeaponParams qualityMods;
	private int power;
	private boolean wantsPower;
	private ChargeCooldown cooldown;
	private Vector2 fireOrigin;
	
	public Weapon(String name, WeaponParams params){
		this.params = params;
		this.name = name;
		this.params.maker.modifyWeaponParams(params);
		qualityMods = params.quality.getRandomParamMods();
		wantsPower = false;
		cooldown = new ChargeCooldown();
		cooldown.setCooldownLimit(getRechargeSpeed());
		cooldown.startCharging();
		this.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				if(isPowered()){
					cooldown.charge(1);
				}else{
					cooldown.loseCharge();
					cooldown.startCharging();
				}
				return false;
			}
		});
	}
	
	public static void loadWeaponAssets(){
		Laser.loadAssets();
		EMPShot.loadAssets();
		TorpedoLauncher.loadAssets();
	}
	
	public String getParamString(){
		String paramString = "Acc:" + Float.toString(getAccuracy()) + "\n" +
							 "Damage:" + Float.toString(getDamage()) + "\n" +
							 "CritDam:" + Float.toString(getCritDamage()) + "\n" +
							 "CritChan:" + Float.toString(getCritChance()) + "\n" +
							 "Recharge:" + Float.toString(getRechargeSpeed()) + "\n" +
							 "Cost:" + Float.toString(getBaseMonetaryCost()) + "\n" +
							 "Power:" + Float.toString(getPowerCost());
		return paramString;
	}
	
	@Override
	public void act(float delta){
		if(!((GameplayScreen)Screens.GAMEPLAY.getScreen()).isPaused()){
			super.act(delta);
		}
	}

	public String getName() {
		return name;
	}
	
	public float getAccuracy() {
		if(params.maker == WeaponMakers.BOOM_N_ZOOM){
			return 1;
		}
		return params.accuracy * qualityMods.accuracy;
	}

	public int getDamage() {
		return params.baseDamage + qualityMods.baseDamage;
	}

	public float getCritDamage() {
		return params.critDamage * qualityMods.critDamage;
	}

	public float getCritChance() {
		return params.critChance * qualityMods.critChance;
	}

	public float getRechargeSpeed() {
		return params.rechargeSpeed * qualityMods.rechargeSpeed;
	}

	public float getBaseMonetaryCost() {
		return (float)Math.floor(params.baseMonetaryCost * qualityMods.baseMonetaryCost);
	}

	public int getPowerCost() {
		return Math.min(params.powerCost + qualityMods.powerCost, MAX_POWER_PER_WEAPON);
	}
	
	public int getPowered(){
		return power;
	}
	
	public boolean isCharged(){
		return cooldown.isCharged();
	}
	
	public void startCharging(){
		cooldown.startCharging();
	}
	
	public float getCooldownPercent(){

		return cooldown.getCooldownProgress()/cooldown.getCooldownLimit();
	}
	
	public void setFireOrigin(Vector2 fireOrigin){
		this.fireOrigin = fireOrigin;
	}
	
	public Vector2 getFireOrigin(){
		return fireOrigin;
	}
	
	public Projectile createProjectile(){
		return new Projectile(getProjectileImage(), getProjectileWidth(), getProjectileHeight());
	}
	
	public abstract void fire(Room target);
	public abstract Texture getProjectileImage();
	public abstract float getProjectileWidth();
	public abstract float getProjectileHeight();
	
	public abstract void onhit(Room target);
	/**
	 * Sets how much power the weapon has to work with
	 * @param power
	 * @return
	 */
	public int setPower(int power){
		if(power>getPowerCost()){
			power = getPowerCost();
		}else if(power<0){
			power = 0;
		}
		
		this.power = power;
		return power;
	}
	
	public boolean isPowered(){
		return power == getPowerCost();
	}
	
	public boolean doesWantPower(){
		return wantsPower;
	}
	
	public void setWantsPower(boolean want){
		this.wantsPower = want;
	}
	
	public boolean didHit(Room target){
		return Math.random()+target.getShip().getEvade() < this.getAccuracy();
	}
}
