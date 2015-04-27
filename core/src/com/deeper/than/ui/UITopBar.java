/**
 * The top bar in the ui, containing the health and the sheild counters
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */

package com.deeper.than.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deeper.than.DTL;
import com.deeper.than.Ship;

public class UITopBar extends Table {
	private static final float TOP_BAR_HEIGHT = 25;
	
	private Label healthLabel;
	private HealthBar hBar;
	private Label sheildLabel;
	private SheildBar sBar;
	
	public UITopBar(Ship ship, float width, boolean split){
		this(ship, width, TOP_BAR_HEIGHT, split);
	}
	
	public UITopBar(Ship ship, float width, float height, boolean split){
		healthLabel = new Label("Health:", DTL.skin);
		sheildLabel = new Label("Sheilds:", DTL.skin);
		float fontHeight = DTL.font.getBounds("Health:").height;
		
		if(fontHeight > height){
			float newHeight = (fontHeight/height)-1;
			healthLabel.setFontScale(newHeight);
			sheildLabel.setFontScale(newHeight);
		}
		hBar = new HealthBar(ship);
		sBar = new SheildBar(ship);
		this.add(healthLabel).spaceRight(1);
		this.add(hBar).minWidth(ship.getMaxHealth()*HealthBar.INCREMENT).minHeight(height).padRight(5);
		if(split){
			this.add().row();
		}
		this.add(sheildLabel).spaceRight(1);
		this.add(sBar).minWidth(width/4).minHeight(height);
		//this.add().prefWidth(Gdx.graphics.getWidth());
	}
}
