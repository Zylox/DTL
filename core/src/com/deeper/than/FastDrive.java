package com.deeper.than;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.Cooldown;
import com.deeper.than.modules.EngineModule;

public class FastDrive extends WidgetGroup{
	private static final float CHARGE_MAX = 30;
	private static final float BASE_CHARGE_RATE = 1;
	private static Image image;
	
	private EngineModule engine;
	private BridgeModule bridge;
	private Cooldown charge;
	
	public FastDrive(EngineModule engine, BridgeModule bridge){
		this.engine = engine;
		this.bridge = bridge;
		charge = new Cooldown(CHARGE_MAX);
	}
	
	public void update(boolean inPeril){
		if(!inPeril && charge.isOnCooldown()){
			charge.endCooldown();
		}
		
		if(engine.enginesOn() && charge.isOnCooldown()){
			charge.advanceCooldown(BASE_CHARGE_RATE*engine.getDriveChargeModifier());
		}
	}
	
	/**
	 * Not used atm, might be used for ai in the future
	 * @return
	 */
	public float getChargePercent(){
		return charge.getCooldownProgress() / charge.getCooldownLimit();
	}
	
	public void resetCharge(){
		charge.startCooldown();
	}

}
