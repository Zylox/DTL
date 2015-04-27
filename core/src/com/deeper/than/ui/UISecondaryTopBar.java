package com.deeper.than.ui;


import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.PlayerShip;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;
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
		Stack stack = new Stack();
		stack.add(new Image(new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch)));
		stack.add(new UIPauseButton(((GameplayScreen)Screens.GAMEPLAY.getScreen())));
		this.add(stack).fill().minHeight(height).minWidth(1.5f*height);
		this.add().prefWidth(width);
		this.padTop(height/4);
	}

}
