package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.Ship;
import com.deeper.than.screens.GameplayScreen;

public class SheildBar extends Widget {
	private static final int SECTIONS = 5;
	private static final float INCREMENT = 100f/SECTIONS;
	
	private Ship ship;
	
	
	public SheildBar(Ship ship){
		this.ship = ship;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		NinePatch img = ship.getDoorImg();
		Color color = batch.getColor().cpy();
		batch.setColor(Color.DARK_GRAY);
		batch.draw(GameplayScreen.highlight,getX(), getY(), getWidth(), getHeight());
		batch.setColor(color);
		
		color = img.getColor().cpy();
		img.setColor(Color.BLUE);
		
		float width = getWidth()/SECTIONS;
		
		for(int i = 0; i < ship.getSheildSections(); i++){
			img.draw(batch, getX()+i*width, getY(), width, getHeight());
		}
		
		img.setColor(color);
	}
}