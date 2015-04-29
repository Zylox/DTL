package com.deeper.than.weapons;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.deeper.than.modules.Cooldown;

public abstract class Weapon extends Actor{

	public static final int MAX_POWER_PER_WEAPON = 4;

	private WeaponParams params;
	private String name;
	private WeaponParams qualityMods;
	private int power;
	private boolean wantsPower;
	private Cooldown cooldown;
	private boolean shot;
	
	public Weapon(String name, WeaponParams params){
		this.params = params;
		this.name = name;
		this.params.maker.modifyWeaponParams(params);
		qualityMods = params.quality.getRandomParamMods();
		wantsPower = false;
		shot = false;
		cooldown = new Cooldown();
		cooldown.setCooldownLimit(getRechargeSpeed());
		this.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				if(shot){
					cooldown.startCooldown();
				}
				cooldown.advanceCooldown(1);
				return false;
			}
		});
	}
	
	public String getParamString(){
		String paramString = "Acc:" + Float.toString(getAccuracy()) + "\n" +
							 "Damage:" + Float.toString(getBaseDamage()) + "\n" +
							 "CritDam:" + Float.toString(getCritDamage()) + "\n" +
							 "CritChan:" + Float.toString(getCritChance()) + "\n" +
							 "Recharge:" + Float.toString(getRechargeSpeed()) + "\n" +
							 "Cost:" + Float.toString(getBaseMonetaryCost()) + "\n" +
							 "Power:" + Float.toString(getPowerCost());
		return paramString;
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

	public float getBaseDamage() {
		return params.baseDamage * qualityMods.baseDamage;
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
	
	public boolean isOnCooldown(){
		return cooldown.isOnCooldown();
	}
	
	public abstract void fire();
	
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
}
