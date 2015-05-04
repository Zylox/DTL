/**
 * Creates a background using opensimplex noise
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.DTL;
import com.deeper.than.MapGenerator;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

/**
 * Creates a background determined by Open Simplex noise
 * @author zach
 *
 */
public class Background extends Widget{

	private Image image;
	private RandNoiseGenerator randNoise;
	private int count;
	
	public Background(Texture tex) {
		randNoise = new RandNoiseGenerator(DTL.VWIDTH, DTL.VHEIGHT, System.currentTimeMillis());
		count = 0;
		genImg();
		this.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				count++;
				//creates new background every 10th frame for stability
				if(count>=10){
					genImg();
					count = 0;
				}
				return false;
			}
		});
	}
	
	private void genImg(){
		if(((GameplayScreen)Screens.GAMEPLAY.getScreen()).isPaused()){
			image = randNoise.getNextImage(0f);
			image.setColor(getColor().cpy().mul(Color.DARK_GRAY).mul(Color.BLUE).clamp());
		}else{
			image = randNoise.getNextImage(.01f);
			Color color2 = getColor().cpy();
			image.setColor(color2.mul(Color.BLUE).mul(Color.LIGHT_GRAY).clamp());
		}

	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		Color color = batch.getColor().cpy();
		image.setBounds(0, 0, DTL.VWIDTH, DTL.VHEIGHT);
		image.draw(batch, parentAlpha);
		batch.setColor(color);
	}
}
