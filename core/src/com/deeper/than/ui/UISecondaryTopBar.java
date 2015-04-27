package com.deeper.than.ui;


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deeper.than.DTL;
import com.deeper.than.PlayerShip;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.ui.UIRewardLabel.RewardLabelTypes;

public class UISecondaryTopBar extends Table{
	private PlayerShip ship;
	private UIRewardLabel currencyLabel;
	private UIRewardLabel fuelLabel;
	
	public UISecondaryTopBar(PlayerShip ship, float width, float height){
		this.ship = ship;
		
		currencyLabel = new UIRewardLabel(ship, RewardLabelTypes.CURRENCY);
		fuelLabel = new UIRewardLabel(ship, RewardLabelTypes.FUEL);
		//this.setFillParent(true);
		this.add(currencyLabel).height(height).spaceRight(5);
		this.add(fuelLabel).height(height).prefWidth(fuelLabel.getPrefWidth()).spaceRight(5);
		this.add().prefWidth(width);
		this.padTop(height/4);
	}

}
