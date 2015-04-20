package com.deeper.than.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.Module;
import com.deeper.than.ui.UIPowerBar.PowerBarState;

public abstract class UIModuleReactorBar extends UIIconReactorBar implements UIModuleSyncable{

	private UIPowerBar mainPower;
	private int powerWanted;
	private Module module;
	
	public UIModuleReactorBar(int powered, Sprite icon, UIPowerBar mainPower, Module module) {
		super(module.getLevel(), 0, icon);
		this.module = module;
		mainPower.givePower(powered, this);
		module.setPowerLevel(getPowered());
		this.mainPower = mainPower;
		powerWanted = 0;

		if(module instanceof MainModule){
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
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		icon.setColor(Color.WHITE);
		if(module.isManable() && module.isManned()){
			icon.setColor(Color.GREEN);
		}
		super.draw(batch, parentAlpha);
	}
	
	public void getPower(int amount){
		if(getPowered()+amount+ module.getDamage() > getSections()){
			amount = getSections()- module.getDamage()-getPowered();
		}
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
		if(getPowered() + module.getDamage() > getSections()){
			System.out.println("DINGGGGGGGGGG");
			powerWanted += getPowered() + module.getDamage() - getSections();
			losePower(getPowered() + module.getDamage() - getSections());
		}
		else if(getPowered() + module.getDamage() < getSections() && powerWanted != 0){
			System.out.println(powerWanted + " pwant");
			powerWanted -= mainPower.givePower(powerWanted, this);
			System.out.println(powerWanted + " pwant2");
		}
		//System.out.println(powerWanted);
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
			if(getSections() - i < module.getIonicCharges()){
				powerChunks.get(i).setState(PowerBarState.IONIZED);
			}
		}
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
