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
		this.add(hBar).prefWidth(Gdx.graphics.getWidth()/2).prefHeight(25).padRight(5);//.expandX();
		this.add(sheildLabel).spaceRight(1);
		this.add(sBar).prefWidth(Gdx.graphics.getWidth()/4).prefHeight(25);
		this.add().prefWidth(Gdx.graphics.getWidth());
	}
}
