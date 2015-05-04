/**
 * Reactor bar with an icon at the bottom
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.deeper.than.screens.GameplayScreen;

/**
 * Reactor bar that has an icon at the bottom
 * @author zach
 *
 */
public class UIIconReactorBar extends UIPowerBar{
	protected Image icon;
	protected boolean isPowerVisible;
	
	public UIIconReactorBar(int sections, int powered, Sprite icon) {
		super(sections, powered);
		isPowerVisible = true;
		this.icon = new Image(icon.getTexture());
		addIconToTable();
	}
	
	/**
	 * Powere visibility switching for enemy ship window
	 * @param isVisible
	 */
	public void setPowerVisible(boolean isVisible){
		isPowerVisible = isVisible;
		setPowerVisibility();
	}
	
	/**
	 * sets children based on results. icon always bisible
	 */
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
	
	/**
	 * Draws square around the reactor when on lockdown
	 */
	@Override
	protected void drawLockdownSquare(Batch batch){
		if(!isPowerVisible){
			return;
		}
		batch.setColor(Color.YELLOW);
		float wOffset = getWidth()/5;
		Array<Actor> a = table.getChildren();
		GameplayScreen.drawEmptyRectable(getX()-wOffset, getY()+icon.getHeight() + 3, getWidth()+2*wOffset, (a.size-1)*(a.get(0).getHeight() + UIPowerBar.BETWEEN_CHUNK_PADDING) + UIPowerBar.BETWEEN_CHUNK_PADDING + 6 , 3, null, batch);
		drawCoolDownBar(batch);
	}
	
	@Override
	protected float getTopOfBarY(){
		Array<Actor> a = table.getChildren();
		return getY()+icon.getHeight() + (a.size-1)*(a.get(0).getHeight() + UIPowerBar.BETWEEN_CHUNK_PADDING) + UIPowerBar.BETWEEN_CHUNK_PADDING + 9 + 3;
	}
	
	protected void drawCoolDownBar(Batch batch){
		//nothing here for this, but there is in modulereactor
	}
	
	
}
