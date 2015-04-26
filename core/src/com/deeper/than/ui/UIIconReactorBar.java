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
	protected boolean isPowerVisible;
	
	public UIIconReactorBar(int sections, int powered, Sprite icon) {
		super(sections, powered);
		isPowerVisible = true;
		this.icon = new Image(icon.getTexture());
		addIconToTable();
	}
	
	public void setPowerVisible(boolean isVisible){
		isPowerVisible = isVisible;
		setPowerVisibility();
	}
	
	protected void setPowerVisibility(){
		for(Actor a : this.getTableChildren()){
			if(a instanceof PowerChunk){
				a.setVisible(isPowerVisible);
			}
		}

	}
	
	@Override
	public void adjustSegments(int sections, int powered){
		super.adjustSegments(sections, powered);
		setPowerVisibility();
	}
	
	protected void addIconToTable(){
		this.addOnNewRow(this.icon).minHeight(this.icon.getHeight()).minWidth(this.icon.getWidth()).prefWidth(this.icon.getWidth()).prefHeight(this.icon.getHeight()).padTop(6);
	}
	
	@Override
	protected void drawLockdownSquare(Batch batch){
		if(!isPowerVisible){
			return;
		}
		batch.setColor(Color.YELLOW);
		float wOffset = getWidth()/5;
		Array<Actor> a = table.getChildren();
		//GameplayScreen.drawEmptyRectable(getX()-wOffset, a.get(a.size - 2).getY() - 3f, getWidth()+2*wOffset, a.get(1).getY() + a.get(1).getHeight()  , 3, null, batch);
		GameplayScreen.drawEmptyRectable(getX()-wOffset, getY()+icon.getHeight() + 3, getWidth()+2*wOffset, (a.size-1)*(a.get(0).getHeight() + UIPowerBar.BETWEEN_CHUNK_PADDING) + UIPowerBar.BETWEEN_CHUNK_PADDING + 6 , 3, null, batch);
		drawCoolDownBar(batch);
	}
	
	@Override
	protected float getTopOfBarY(){
		Array<Actor> a = table.getChildren();
		return getY()+icon.getHeight() + (a.size-1)*(a.get(0).getHeight() + UIPowerBar.BETWEEN_CHUNK_PADDING) + UIPowerBar.BETWEEN_CHUNK_PADDING + 9 + 3;
		//return (a.get(a.size - 2).getY()) + (a.get(0).getY() + a.get(0).getHeight());
		
	}
	
	protected void drawCoolDownBar(Batch batch){
		//nothing here
	}
	
	
}
