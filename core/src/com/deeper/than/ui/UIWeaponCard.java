/**
 * 
 */
package com.deeper.than.ui;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.DTL;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * @author zach
 *
 */
public class UIWeaponCard extends WidgetGroup {
	private static NinePatch background;
	
	public static void loadAssets(){
		background = new NinePatch(new Texture("weaponBackground.png"),1,3,1,3);
	}
	
	private UIWeaponReactor powerBar;
	
	public UIWeaponCard(float width, float height,Weapon weapon, WeaponsModule weapModule, UIWeaponModuleReacBar moduleUI){
		powerBar = new UIWeaponReactor(weapon, weapModule, moduleUI);
		Table table = new Table();
		table.setFillParent(true);
		float padLeft = 3;
		table.add(powerBar).expand().fillY().minWidth(20).padLeft(padLeft).bottom();
		Label label = new Label(weapon.getName(), DTL.skin);
		label.setFontScale(.9f);
		label.setWrap(true);
		table.add(label).fill().width(width-20-padLeft*2-2).padLeft(padLeft+2);
		NinePatchDrawable bg =new NinePatchDrawable(background);
		table.background(bg);
		this.addActor(table);
		this.clearListeners();
		this.addListener(powerBar.clicker);
		setWidth(width);
		setHeight(height);
	}
}
