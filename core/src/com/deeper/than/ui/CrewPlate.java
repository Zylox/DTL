package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.crew.Crew;
import com.deeper.than.screens.GameplayScreen;

public class CrewPlate extends Widget {
	private Crew crew;
	
	public CrewPlate(Crew crew){
		this.crew = crew;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		Texture back = GameplayScreen.highlight;
		Color color = batch.getColor().cpy();
		batch.setColor(Color.DARK_GRAY);
		TextureRegion icon = crew.getIcon();
		//batch.draw(back, getX(), getY(), Crew.CREW_WIDTH/Crew.SCALE + 100, Crew.CREW_HEIGHT/Crew.SCALE *1.5f);
		batch.setColor(color);
		batch.draw(icon, getX(), getY(),getWidth(), getHeight());
	}
}
