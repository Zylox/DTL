/**
 * A label and icon that display info on a reward object like fuel or currency
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.DTL;
import com.deeper.than.EnemyShip;
import com.deeper.than.PlayerShip;
import com.deeper.than.screens.GameplayScreen;

/**
 * UI element for reward objects
 * @author zach
 *
 */
public class UIRewardLabel extends Table {

	public enum RewardLabelTypes{
		CURRENCY,
		FUEL;
	}
	public static Sprite fuelIcon;
	public static Sprite currencyIcon;
	
	private PlayerShip ship;
	private int amount;
	private Label label;
	private RewardLabelTypes type;
	
	public static void loadAssets(){
		fuelIcon = new Sprite(new Texture(Gdx.files.internal("fuelicon.png")));
		currencyIcon = new Sprite(new Texture(Gdx.files.internal("bolt.png")));
	}
	
	public UIRewardLabel(int amount, RewardLabelTypes type){
		this.ship = null;
		this.amount = amount;
		this.type = type;
		layoutTable();
	}
	
	public UIRewardLabel(PlayerShip ship, RewardLabelTypes type){
		this.ship = ship;
		this.type = type;
		//callback to sync to actual values
		addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				syncLabelToShip();
				return false;
			}
		});
		layoutTable();
	}
	
	public int getAmount(){
		if(ship != null){
			switch(type){
			case CURRENCY:
				return ship.getCurrency();
			case FUEL:
				return ship.getFuel();
			default:
				return 0;
			}
		}else{
			return amount;
		}
		
	}
	
	private Sprite getSprite(){
		switch(type){
		case CURRENCY:
			return currencyIcon;
		case FUEL:
			return fuelIcon;
		}
		return null;
	}
	
	private void layoutTable(){
		label = new Label(Integer.toString(getAmount()), DTL.skin);
		Image img = new Image(getSprite());
		Table table = new Table();
		table.add(img).fill().minWidth(getSprite().getWidth());
		table.add(label).fill();
		this.add(table);
		this.setBackground(new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch));
	}
	
	private void syncLabelToShip(){
		if(ship != null){
			switch(type){
			case CURRENCY:
				label.setText(Integer.toString(ship.getCurrency()));
				break;
			case FUEL:
				label.setText(Integer.toString(ship.getFuel()));
				break;
			}
		}
	}
}
