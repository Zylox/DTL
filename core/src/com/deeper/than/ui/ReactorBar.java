package com.deeper.than.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.deeper.than.Ship;

public class ReactorBar extends UIPowerBar {

	private Ship ship;
	
	public ReactorBar(Ship ship) {
		super(ship.getPower(), ship.getPower());
		this.ship = ship;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		synchronize();
		super.draw(batch, parentAlpha);
	}
	
	private void synchronize(){
		if(getSections() != ship.getPower()){
			adjustSegments(ship.getPower(), getPowered()+(ship.getPower() - getSections()));
		}
	}

}
