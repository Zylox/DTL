package com.deeper.than.modules;

import com.deeper.than.DTL;

/**
 * Contains everything needed to manage a cooldown on an object.
 * @author zach
 *
 */
public class Cooldown {

	private float cooldownProgress;
	private float cooldownLimit;
	private boolean isOnCooldown;
	
	/**
	 * Sets the upper bound to the default 100%, and not on cooldown to start
	 */
	public Cooldown(){
		this(100f);
	}
	
	/**
	 *  Sets the upper bound for the cooldown timer and starts it off cooldown.
	 * @param cooldownLimit
	 */
	public Cooldown(float cooldownLimit) {
		this(cooldownLimit, false);
	}
	
	/**
	 * Sets the upper bound for the cooldown timer and if it starts on cooldown or not.
	 * @param cooldownLimit
	 * @param onCooldown
	 */
	public Cooldown(float cooldownLimit, boolean onCooldown){
		this.cooldownLimit = cooldownLimit;
		setState(onCooldown);
	}

	/**
	 * Initilizes values based if cooldown is being set to begin or end
	 * @param isOnCooldown
	 */
	private void setState(boolean isOnCooldown){
		this.isOnCooldown = isOnCooldown;
		if(isOnCooldown){
			cooldownProgress = 0;
		}else{
			cooldownProgress = cooldownLimit;
		}
	}
	
	/**
	 * Starts a cooldown cycle.
	 * Call method advanceCooldown to increment it.
	 */
	public void startCooldown(){
		setState(true);
	}
	
	public float getCooldownProgress(){
		return cooldownProgress;
	}
	
	/**
	 * Given a cooldownRate in rate/second, advances the cooldown timer by that much.
	 * Only advanced timer if it is on cooldown.
	 * Returns if the cooldown has ended or not.
	 * @param coolDownRatePerSecond
	 * @return isOnCoolDown
	 */
	public boolean advanceCooldown(float coolDownRatePerSecond){
		
		if(isOnCooldown())
			cooldownProgress += DTL.getRatePerTimeStep(coolDownRatePerSecond);
		
		if(cooldownProgress >= cooldownLimit){
			setState(false);
		}
		return isOnCooldown();
	}
	
	/**
	 * Returns the state of the cooldown
	 * @return isOnCooldown
	 */
	public boolean isOnCooldown(){
		return isOnCooldown;
	}
	
	/**
	 * Sets the upper bound for the cooldown
	 * @param cooldownLimit
	 */
	public void setCooldownLimit(float cooldownLimit){
		this.cooldownLimit = cooldownLimit;
	}
	
}
