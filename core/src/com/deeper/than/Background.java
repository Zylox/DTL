package com.deeper.than;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

public class Background extends Widget{

	private Image image;
	
	public Background(Texture tex) {
		image = new Image(tex);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		Color color = batch.getColor().cpy();
		if(((GameplayScreen)Screens.GAMEPLAY.getScreen()).isPaused()){
			image.setColor(Color.LIGHT_GRAY);

		}else{
			image.setColor(Color.WHITE);
		}
		image.draw(batch, parentAlpha);
		batch.setColor(color);
	}
}
