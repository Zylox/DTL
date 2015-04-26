package com.deeper.than.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.EnemyShip;
import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.SubModule;
import com.deeper.than.screens.GameplayScreen;

public class UIModuleReactorBar extends UIIconReactorBar implements UIModuleSyncable{

	private UIPowerBar mainPower;
	//private int powerWanted;
	private int desiredPowerLevel;
	private Module module;
	private int preLockdownPowerLevel;
	private ClickListener clicky = new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(button == Buttons.LEFT){
					clickPowerUp();
				}else if(button == Buttons.RIGHT){
					clickPowerDown();
				}
				return true;
		    }
		};
	
	public UIModuleReactorBar(int powered, Sprite icon, UIPowerBar mainPower, Module module) {
		super(module.getLevel(), 0, icon);
		this.module = module;
		mainPower.givePower(powered, this);
		module.setPowerLevel(getPowered());
		this.mainPower = mainPower;
		desiredPowerLevel = getPowered();
		
		if(module instanceof MainModule && module.isPlayerModule()){
			this.addListenerToChildren(clicky);
		}
	} 
	
	private void clickPowerUp(){
		if(!module.isOnLockdown()){
			setDesiredPowerLevel(getPowered()+1);
		}
	}
	
	private void clickPowerDown(){
		if(!module.isOnLockdown()){
			setDesiredPowerLevel(getPowered()-1);
		}
	}
	
	public int getDesiredPowerLevel() {
		return desiredPowerLevel;
	}

	public void setDesiredPowerLevel(int desiredPowerLevel) {
		this.desiredPowerLevel = desiredPowerLevel;
		if(desiredPowerLevel > module.getLevel()){
			this.desiredPowerLevel = module.getLevel();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		icon.setColor(Color.WHITE);
		if(module.isManable() && module.isManned() && (module.isPlayerModule() || ((EnemyShip)module.getShip()).canPlayerSeeMyTiles())){
			icon.setColor(Color.GREEN);
		}
		super.draw(batch, parentAlpha);
	}
	
	@Override
	protected void drawCoolDownBar(Batch batch){
		batch.draw(GameplayScreen.highlight, getX(), getTopOfBarY(), getWidth() - module.getIonicCooldownProg() / module.getIonicCooldownMax() * getWidth(), 5 );
	}
	
	public void getPower(int amount){
		amount = getSections()- module.getDamage()-getPowered();
		mainPower.givePower(amount, this);
			
		//updateModulePowerLevel();
	}
	
	public void losePower(int amount){
		this.givePower(amount, mainPower);
		//updateModulePowerLevel();
	}
	
	@Override
	public void updateModulePowerLevel() {
		//int damageReduc = getSections() - module.getDamage();
		if(module.isOnLockdown() && !this.isLockedDown){
			this.setLockedDown(true);
			preLockdownPowerLevel = getPowered();
		}else if(!module.isOnLockdown() && this.isLockedDown){
			this.setLockedDown(false);
			getPower(preLockdownPowerLevel - getPowered());
		}
		
		if(module.isOnLockdown()){
			if(preLockdownPowerLevel - module.getIonicCharges() < getPowered()){
				losePower(getPowered() - (preLockdownPowerLevel - module.getIonicCharges()));
			}
		}
		if(getSections() - module.getDamage() < getPowered()){
			losePower(getPowered() - (getSections() - module.getDamage()));
		}else if(getSections() - module.getDamage() > getPowered() && !this.isLockedDown){
			if(desiredPowerLevel > getPowered()){
				if(mainPower.isEmpty()){
					desiredPowerLevel = getPowered();
				}else{
					getPower(Math.min(desiredPowerLevel, getSections()-module.getDamage()) - getPowered());
				}
			}
		}
		if(desiredPowerLevel < getPowered() && !this.isLockedDown){
			losePower(getPowered() - desiredPowerLevel);
		}

		
		module.setPowerLevel(getPowered());
		updatePowered();
	}
	
	@Override
	protected void updatePowered(){
		for(int i = 0; i < powerChunks.size(); i++){
			if(i < getPowered()){
				powerChunks.get(i).setState(PowerBarState.POWERED);
			}
			else if(i >= getPowered()){
				powerChunks.get(i).setState(PowerBarState.UNPOWERED);
			}
			if(getSections() - i <= module.getDamage()){
				powerChunks.get(i).setState(PowerBarState.DAMAGED);
			}
//			if(getPowered() - i <= module.getIonicCharges()){
//				powerChunks.get(i).setState(PowerBarState.IONIZED);
//			}
		}
	}
	

	public void checkforSectionsChange(){
		if(module.getLevel() != getSections()){
			if(module instanceof SubModule){
				this.adjustSegments(module.getLevel(), module.getLevel());
				setDesiredPowerLevel(getPowered());
				preLockdownPowerLevel = getPowered();
			}else{
				this.adjustSegments(module.getLevel(), this.getPowered());
			}
			addIconToTable();
			if(module instanceof MainModule && module.isPlayerModule()){
				this.addListenerToChildren(clicky);
			}
		}
	}
}
