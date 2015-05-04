/**
 * Recator bar for a specific weapon
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * Recator bar for a specific weapon
 * @author zach
 *
 */
public class UIWeaponReactor extends UIPowerBar{
	private Weapon weapon;
	private WeaponsModule weapModule;
	private UIWeaponModuleReacBar moduleUI;
	protected boolean clickPassthrough = false;
	
	//callback to manage powerup and down
	public ClickListener clicker = new ClickListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			if(event.getButton() == Buttons.RIGHT){
				setDesirePower(false);
				clickPassthrough = false;
				return true;
			}
			if(event.getButton() == Buttons.LEFT){
				if(!isWeapPowered()){
					setDesirePower(true);
				}else{
					clickPassthrough = true;
				}
			}
			return false;
		}
		
	};
	
	public UIWeaponReactor(Weapon weapon, WeaponsModule weapModule, UIWeaponModuleReacBar moduleUI) {
		super(weapon.getPowerCost(),0);
		this.weapon = weapon;
		this.weapModule = weapModule;
		this.moduleUI = moduleUI;
		register();
		
	}
	//register with the bar as equipped
	public void register(){
		moduleUI.addToQueue(weapon);
	}
	
	//unregister with the bar
	public void unRegister(){
		moduleUI.removeFromQueue(weapon);
	}
	
	private boolean isWeapPowered(){
		return weapon.isPowered();
	}
	
	public void setDesirePower(boolean desire){
		this.weapon.setWantsPower(desire);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		synchronize();
		super.draw(batch, parentAlpha);
	}
	
	/**
	 * Syncs visually displayed power with actual power
	 */
	private void synchronize(){
		this.setPowered(weapon.getPowered());
		this.updatePowered();
		if(weapon.isPowered()){
			clickPassthrough = true;
		}
	}
}
