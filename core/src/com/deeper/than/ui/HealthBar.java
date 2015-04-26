package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.Ship;
import com.deeper.than.screens.GameplayScreen;


public class HealthBar extends Widget {
	
	public static final float INCREMENT = 20;
	
	private Ship ship;
	
	
	public HealthBar(Ship ship){
		this.ship = ship;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		NinePatch img = ship.getDoorImg();
		Color color = batch.getColor().cpy();
		batch.setColor(Color.DARK_GRAY);
		float padding = 0;
		batch.draw(GameplayScreen.highlight,getX(), getY(), getWidth()-padding, getHeight());
		batch.setColor(color);
		
		color = img.getColor().cpy();
		img.setColor(Color.GREEN);
		
		int i;
		for(i = 0; i < ship.getHealth(); i++){
			img.draw(batch, getX()+i*INCREMENT, getY(), INCREMENT-padding, getHeight());
		}
		img.setColor(Color.DARK_GRAY);
		for( ; i < ship.getMaxHealth(); i++){
			img.draw(batch, getX()+i*INCREMENT, getY(), INCREMENT-padding, getHeight());
		}
		
		img.setColor(color);
	}
}
