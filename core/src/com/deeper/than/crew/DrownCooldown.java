/**
 * 
 */
package com.deeper.than.crew;

import com.deeper.than.modules.Cooldown;

/**
 * @author zach
 *
 */
public class DrownCooldown extends Cooldown {
	private boolean isDrowning;
	private Races race;
	
	public DrownCooldown(Races race){
		this.race = race;
		this.isDrowning = false;
		this.setCooldownLimit(race.getMaxOxygen());
	}
	
	public void advanceDrowning(){
		if(!this.isOnCooldown() && !isDrowning()){
			this.startCooldown();
		}
		this.advanceCooldown(10);
		if(!this.isOnCooldown() && !isDrowning()){
			this.isDrowning = true;
		}
	}
	
	@Override
	public void endCooldown(){
		if(this.isOnCooldown()){
			super.endCooldown();
		}
		this.isDrowning = false;
	}
	
	public boolean isDrowning(){
		return isDrowning;
	}
}
