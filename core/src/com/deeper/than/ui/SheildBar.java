package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.Ship;
import com.deeper.than.modules.SheildModule;
import com.deeper.than.screens.GameplayScreen;

public class SheildBar extends Widget {
	private static final float INCREMENT = 20;
	private static final float COOLDOWN_BAR_HEIGHT = 4;
	
	private Ship ship;
	
	
	public SheildBar(Ship ship){
		this.ship = ship;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		NinePatch img = ship.getDoorImg();
		Color color = batch.getColor().cpy();
		batch.setColor(Color.DARK_GRAY);
		batch.draw(GameplayScreen.highlight,getX(), getY(), SheildModule.MAX_SHEILD_SECTIONS*INCREMENT, getHeight());
		batch.setColor(color);
		
		color = img.getColor().cpy();
		img.setColor(Color.BLUE);

		int activeSheilds = ship.getActiveSheildSections();
		float lineWidth = 1;
		for(int i = 0; i < SheildModule.MAX_SHEILD_SECTIONS; i++){
			if(i < ship.getSheildSections() && i<activeSheilds){
				img.draw(batch, getX()+i*INCREMENT, getY()+COOLDOWN_BAR_HEIGHT, INCREMENT, getHeight() - COOLDOWN_BAR_HEIGHT);
			}else if (i < ship.getSheildSections()){
				GameplayScreen.drawEmptyRectable(getX()+i*INCREMENT+1, getY() + COOLDOWN_BAR_HEIGHT, INCREMENT-1, getHeight() - COOLDOWN_BAR_HEIGHT, lineWidth, Color.BLUE, batch);
			}else{
				GameplayScreen.drawEmptyRectable(getX()+i*INCREMENT+1, getY() + COOLDOWN_BAR_HEIGHT, INCREMENT-1, getHeight() - COOLDOWN_BAR_HEIGHT, lineWidth, Color.GRAY, batch);
			}
		}
		
		if(ship.getSheildCooldownAmt() != 0){
			img.draw(batch, getX(), getY(), SheildModule.MAX_SHEILD_SECTIONS*INCREMENT * (ship.getSheildCooldownAmt()/SheildModule.COOLDOWN_MAX) , COOLDOWN_BAR_HEIGHT);
		}
		img.setColor(color);
	}
}
