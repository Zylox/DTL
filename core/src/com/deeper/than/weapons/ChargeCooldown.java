/**
 * 
 */
package com.deeper.than.weapons;

import com.deeper.than.modules.Cooldown;

/**
 * @author zach
 *
 */
public class ChargeCooldown extends Cooldown {
	private boolean charged;
	
	public ChargeCooldown(){
		charged = false;
	}
	
	public void startCharging(){
		charged = false;
		this.startCooldown();
	}
	
	public void charge(int amt){
		if(this.isOnCooldown()){
			this.advanceCooldown(amt);
		}
		
	}
}
