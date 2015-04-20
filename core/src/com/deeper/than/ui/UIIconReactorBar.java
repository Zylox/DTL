package com.deeper.than.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class UIIconReactorBar extends UIPowerBar{
	protected Image icon;

	
	public UIIconReactorBar(int sections, int powered, Sprite icon) {
		super(sections, powered);
		
		this.icon = new Image(icon.getTexture());
		addIconToTable();
	}
	
	public void addIconToTable(){
		this.addOnNewRow(this.icon).minHeight(this.icon.getHeight()).minWidth(this.icon.getWidth()).prefWidth(10);
	}
	
}
