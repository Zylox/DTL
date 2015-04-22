package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.deeper.than.screens.GameplayScreen;

public class UIIconReactorBar extends UIPowerBar{
	protected Image icon;

	
	public UIIconReactorBar(int sections, int powered, Sprite icon) {
		super(sections, powered);
		
		this.icon = new Image(icon.getTexture());
		addIconToTable();
	}
	
	public void addIconToTable(){
		this.addOnNewRow(this.icon).minHeight(this.icon.getHeight()).minWidth(this.icon.getWidth()).prefWidth(this.icon.getWidth()).prefHeight(this.icon.getHeight()).padTop(6);
	}
	
	@Override
	protected void drawLockdownSquare(Batch batch){
		batch.setColor(Color.YELLOW);
		float wOffset = getWidth()/4;
		Array<Actor> a = table.getChildren();
		GameplayScreen.drawEmptyRectable(getX()-wOffset, a.get(a.size - 2).getY() + icon.getHeight() - 3f, getWidth()+2*wOffset, a.get(0).getY() + a.get(0).getHeight() - icon.getHeight() , 3, null, batch);
		drawCoolDownBar(batch);
	}
	
	@Override
	protected float getTopOfBarY(){
		Array<Actor> a = table.getChildren();
		return (a.get(a.size - 2).getY()) + (a.get(0).getY() + a.get(0).getHeight());
		
	}
	
	protected void drawCoolDownBar(Batch batch){
		//nothing here
	}
	
	
}
