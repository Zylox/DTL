/**
 * 
 */
package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * @author zach
 *
 */
public class UIWeaponBottomBar extends WidgetGroup{
	private static final float cardWidth = 120;
	private static final float cardHeight = 60;
	
	public UIWeaponBottomBar(UIWeaponModuleReacBar modUI){
		WeaponsModule mod = ((WeaponsModule)modUI.getModule());
		ArrayList<Weapon> equippedWeapons = mod.getEquippedWeapons();
		int i;
		UIWeaponCard card;
		for(i = 0; i<equippedWeapons.size(); i++){
			card = new UIWeaponCard(cardWidth, cardHeight, equippedWeapons.get(i), mod, modUI);
			card.setX(i*cardWidth + i * 4);
			this.addActor(card);
		}
//		Container<Actor> emptyCont;
//		for(; i< mod.getShip().getMaxWeapons(); i++){
//			emptyCont = new Container<Actor>();
//			emptyCont.background(new NinePatchDrawable(UIWeaponCard.background));
//			emptyCont.width(cardWidth);
//			emptyCont.height(cardHeight);
//			this.addActor(emptyCont);
//		}
		
		
	}
}
