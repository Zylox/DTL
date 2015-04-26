/**
 * The top bar in the ui, containing the health and the sheild counters
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */

package com.deeper.than.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deeper.than.DTL;
import com.deeper.than.Ship;

public class UITopBar extends Table {

	Label healthLabel;
	HealthBar hBar;
	Label sheildLabel;
	SheildBar sBar;
	
	public UITopBar(Ship ship){
		
		healthLabel = new Label("Health:", DTL.skin);
		hBar = new HealthBar(ship);
		sheildLabel = new Label("Sheilds:", DTL.skin);
		sBar = new SheildBar(ship);
		this.add(healthLabel).spaceRight(1);;
		this.add(hBar).minWidth(DTL.VWIDTH/4).minHeight(25).padRight(5);//.expandX();
		this.add(sheildLabel).spaceRight(1);
		this.add(sBar).minWidth(DTL.VWIDTH/8).minHeight(25);
		//this.add().prefWidth(Gdx.graphics.getWidth());
	}
}
