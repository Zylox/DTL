/**
 * 
 */
package com.deeper.than.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * @author zach
 *
 */
public class UIWeaponReactor extends UIPowerBar{
	private Weapon weapon;
	private WeaponsModule weapModule;
	private UIWeaponModuleReacBar moduleUI;
	
	public ClickListener clicker = new ClickListener(){
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			if(event.getButton() == Buttons.RIGHT){
				setDesirePower(false);
			}
			if(event.getButton() == Buttons.LEFT){
				if(!isWeapPowered()){
					setDesirePower(true);
				}else{
					//select fro targetting
				}
			}
			return true;
		}
	};
	
	public UIWeaponReactor(Weapon weapon, WeaponsModule weapModule, UIWeaponModuleReacBar moduleUI) {
		super(weapon.getPowerCost(),0);
		this.weapon = weapon;
		this.weapModule = weapModule;
		this.moduleUI = moduleUI;
		register();
	}
	
	public void register(){
		moduleUI.addToQueue(weapon);
	}
	
	public void unRegister(){
		moduleUI.removeFromQueue(weapon);
	}
	
	private boolean isWeapPowered(){
		return weapon.isPowered();
	}
	
	private void setDesirePower(boolean desire){
		this.weapon.setWantsPower(desire);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		synchronize();
		super.draw(batch, parentAlpha);
	}
	
	private void synchronize(){
				this.setPowered(weapon.getPowered());
				this.updatePowered();
			//adjustSegments(ship.getPower(), getPowered()+(ship.getPower() - getSections()));
	}
}
