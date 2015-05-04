/**
 * Reactor for a module
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
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

/**
 * UI element for a moudule
 * @author zach
 *
 */
public class UIModuleReactorBar extends UIIconReactorBar implements UIModuleSyncable{

	private UIPowerBar mainPower;
	//powerlevel the module wants
	private int desiredPowerLevel;
	private Module module;
	private int preLockdownPowerLevel;
	//callback for clicks
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
		
		//only main modules should be clickable
		if(module instanceof MainModule && module.isPlayerModule()){
			this.addListenerToChildren(clicky);
		}
	} 
	
	protected void clickPowerUp(){
		clickPowerUp(1);
	}
	protected void clickPowerUp(int amt){
		if(!module.isOnLockdown()){
			setDesiredPowerLevel(getPowered()+amt);
		}
	}
	
	protected void clickPowerDown(){
		clickPowerDown(1);
	}
	
	protected void clickPowerDown(int amt){
		if(!module.isOnLockdown()){
			setDesiredPowerLevel(getPowered()-amt);
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
		//color if manned
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
			
	}
	
	public void losePower(int amount){
		this.givePower(amount, mainPower);
	}
	
	@Override
	public void updateModulePowerLevel() {
		//modify visibility based on new conditions
		if(!this.isPowerVisible && module.getShip() instanceof EnemyShip && ((EnemyShip)module.getShip()).canPlayerSeeMyPower()){
			this.setPowerVisible(true);
		}else if(this.isPowerVisible && module.getShip() instanceof EnemyShip && !((EnemyShip)module.getShip()).canPlayerSeeMyPower()){
			this.setPowerVisible(false);
		}
		
		//if the module is on lockdown, effect power accordingly
		if(module.isOnLockdown() && !this.isLockedDown){
			this.setLockedDown(true);
			preLockdownPowerLevel = getPowered();
		}else if(!module.isOnLockdown() && this.isLockedDown){
			this.setLockedDown(false);
			getPower(preLockdownPowerLevel - getPowered());
		}
		//take power in case of lockdown
		if(module.isOnLockdown()){
			if(preLockdownPowerLevel - module.getIonicCharges() < getPowered()){
				losePower(getPowered() - (preLockdownPowerLevel - module.getIonicCharges()));
			}
		}
		//if damage is too much, take some power
		if(getSections() - module.getDamage() < getPowered()){
			losePower(getPowered() - (getSections() - module.getDamage()));
		}else if(getSections() - module.getDamage() > getPowered() && !this.isLockedDown){
			//if there is room for power, and more is wanted, get it
			if(desiredPowerLevel > getPowered()){
				if(mainPower.isEmpty()){
					desiredPowerLevel = getPowered();
				}else{
					getPower(Math.min(desiredPowerLevel, getSections()-module.getDamage()) - getPowered());
				}
			}
		}
		//if the bar wants to lose some power, take it
		if(desiredPowerLevel < getPowered() && !this.isLockedDown){
			losePower(getPowered() - desiredPowerLevel);
		}
		//sync module
		module.setPowerLevel(getPowered());
		updatePowered();
	}
	
	/**
	 * Set power chunks to appropriate states for drawing
	 */
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
		}
	}
	

	//Check to see if module has updated itself to a new level and react accordingly
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

	public Module getModule() {
		return module;
	}
	
	
}
