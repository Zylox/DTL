package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.Ship;
import com.deeper.than.screens.GameplayScreen;


public class HealthBar extends Widget {
	
	private static final int SECTIONS = 20;
	private static final float INCREMENT = 100f/SECTIONS;
	
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
		if(ship.getHealth() < 25)      img.setColor(Color.RED);
		else if(ship.getHealth() < 50) img.setColor(Color.YELLOW);
		else if(ship.getHealth() < 75) img.setColor(Color.OLIVE);
		else if(ship.getHealth() > 75) img.setColor(Color.GREEN);
		
		float width = getWidth()/SECTIONS;
		for(int i = 0; i < Math.ceil(ship.getHealth()/INCREMENT); i++){
			img.draw(batch, getX()+i*width, getY(), width-padding, getHeight());
		}
		
		img.setColor(color);
	}
}
