package com.deeper.than.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.DTL;
import com.deeper.than.MapGenerator;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

public class Background extends Widget{

	private Image image;
	RandNoiseGenerator randNoise;
	
	public Background(Texture tex) {
		randNoise = new RandNoiseGenerator(DTL.VWIDTH, DTL.VHEIGHT, System.currentTimeMillis());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		Color color = batch.getColor().cpy();
		
		if(((GameplayScreen)Screens.GAMEPLAY.getScreen()).isPaused()){
			image = randNoise.getNextImage(0f);
			image.setColor(this.getColor().cpy().mul(Color.DARK_GRAY).mul(Color.BLUE).clamp());

		}else{
			image = randNoise.getNextImage(.002f);
			Color color2 = this.getColor().cpy();
			
			image.setColor(color2.mul(Color.BLUE).mul(Color.LIGHT_GRAY).clamp());
		}
		image.setBounds(0, 0, DTL.VWIDTH, DTL.VHEIGHT);
		image.draw(batch, parentAlpha);
		batch.setColor(color);
	}
}
