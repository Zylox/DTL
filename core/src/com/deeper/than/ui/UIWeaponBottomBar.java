/**
 * 
 */
package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * @author zach
 *
 */
public class UIWeaponBottomBar extends WidgetGroup{
	private static final float cardWidth = 120;
	private static final float cardHeight = 60;
	private WeaponsModule mod;
	
	public UIWeaponBottomBar(UIWeaponModuleReacBar modUI){
		mod = ((WeaponsModule)modUI.getModule());
		ArrayList<Weapon> equippedWeapons = mod.getEquippedWeapons();
		int i;
		UIWeaponCard card;
		for(i = 0; i<equippedWeapons.size(); i++){
			card = new UIWeaponCard(cardWidth, cardHeight, equippedWeapons.get(i), mod, modUI);
			card.setX(i*cardWidth + i * 4);
			this.addActor(card);
		}
		Image emptyCont;
		for(; i< mod.getShip().getMaxWeapons(); i++){
			emptyCont = new Image(new NinePatchDrawable(UIWeaponCard.background));
			emptyCont.setX(i*cardWidth + i * 4);
			emptyCont.setWidth(cardWidth);
			emptyCont.setHeight(cardHeight);
			this.addActor(emptyCont);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
	}
}
