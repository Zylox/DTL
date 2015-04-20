package com.deeper.than.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.modules.MainModule;

public abstract class UIMainModuleReactorBar extends UIIconReactorBar implements UIModuleSyncable{

	private MainModule module;
	private UIPowerBar mainPower;
	
	public UIMainModuleReactorBar(int powered, Sprite icon, UIPowerBar mainPower, MainModule module) {
		super(module.getLevel(), 0, icon);
		mainPower.givePower(powered, this);
		module.setPowerLevel(getPowered());
		this.module = module;
		this.mainPower = mainPower;

		this.addListenerToChildren((new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(button == Buttons.LEFT){
					getPower(1);
					
				}else if(button == Buttons.RIGHT){
					losePower(1);
				}
				return true;
		    }
		}));
	} 
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		icon.setColor(Color.WHITE);
		if(module.isManable() && module.isManned()){
			icon.setColor(Color.GREEN);
		}
		super.draw(batch, parentAlpha);
	}
	
	public void getPower(int amount){
		mainPower.givePower(amount, this);
		updateModulePowerLevel();
	}
	
	public void losePower(int amount){
		this.givePower(amount, mainPower);
		updateModulePowerLevel();
	}
	
	@Override
	public void updateModulePowerLevel() {
		module.setPowerLevel(getPowered());		
	}
	

	public void checkforSectionsChange(){
		if(module.getLevel() != getSections()){
			this.adjustSegments(module.getLevel(), this.getPowered());
			addIconToTable();
			this.addListenerToChildren((new ClickListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if(button == Buttons.LEFT){
						getPower(1);
						
					}else if(button == Buttons.RIGHT){
						losePower(1);
					}
					return true;
			    }
			}));
		}
	}
}
