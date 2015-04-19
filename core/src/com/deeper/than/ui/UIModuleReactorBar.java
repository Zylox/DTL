package com.deeper.than.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.Module;

public abstract class UIModuleReactorBar extends UIPowerBar{

	private MainModule module;
	private Image icon;
	private ReactorBar mainPower;
	
	public UIModuleReactorBar(int powered, Sprite icon, ReactorBar mainPower, MainModule module) {
		super(module.getLevel(), 0);
		mainPower.givePower(powered, this);
		module.setPowerLevel(getPowered());
		this.module = module;
		this.mainPower = mainPower;
		this.icon = new Image(icon.getTexture());
		this.addOnNewRow(this.icon).minHeight(this.icon.getHeight()).minWidth(this.icon.getWidth()).prefWidth(10);
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
	
	public void getPower(int amount){
		mainPower.givePower(amount, this);
		module.setPowerLevel(getPowered());
	}
	
	public void losePower(int amount){
		this.givePower(amount, mainPower);
		module.setPowerLevel(getPowered());
	}
	

	public void checkforSectionsChange(){
		if(module.getLevel() != getSections()){
			this.adjustSegments(module.getLevel(), this.getPowered());
			this.addOnNewRow(this.icon).minHeight(this.icon.getHeight()).minWidth(this.icon.getWidth()).prefWidth(10);
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
