/**
 * 
 */
package com.deeper.than.ui;


import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.DTL;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * @author zach
 *
 */
public class UIWeaponCard extends WidgetGroup {
	public static NinePatch background;
	
	private NinePatch bgInstance;
	private ClickListener targetSelect = new ClickListener(){
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
			if(powerBar.clickPassthrough){
				if(button == Buttons.LEFT){
					if(container != null){
						setSelfSelected();
					}
				}
			}
			if(button == Buttons.RIGHT){
				unSelectSelf();
			}
			return true;
		}
	};
	
	public static void loadAssets(){
		background = new NinePatch(new Texture("weaponBackground.png"),1,3,1,3);
	}
	
	private UIWeaponReactor powerBar;
	private UIWeaponBottomBar container;
	
	public UIWeaponCard(float width, float height,Weapon weapon, WeaponsModule weapModule, UIWeaponModuleReacBar moduleUI){
		container = null;
		powerBar = new UIWeaponReactor(weapon, weapModule, moduleUI);
		Container<UIWeaponReactor> barCont = new Container<UIWeaponReactor>(powerBar);
		barCont.fill();
		Table table = new Table();
		table.setFillParent(true);
		float padLeft = 3;
		table.add(barCont).expand().fillY().minWidth(20).padLeft(padLeft).bottom();
		Label label = new Label(weapon.getName(), DTL.skin);
		label.setFontScale(.9f);
		label.setWrap(true);
		table.add(label).fill().width(width-20-padLeft*2-2).padLeft(padLeft+2);
		bgInstance =new NinePatch(background);
		table.background(new NinePatchDrawable(bgInstance));
		this.addActor(table);
		this.clearListeners();
		this.addListener(this.targetSelect);
		this.addListener(powerBar.clicker);
		setWidth(width);
		setHeight(height);
	}
	
	public boolean setSelfSelected(){
		if(container != null){
			container.setSelected(this);
			return true;
		}
		return false;
	}
	public boolean unSelectSelf(){
		if(container != null && container.getSelected() == this){
			container.setSelected(null);
			return true;
		}
		return false;
	}	
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		if(container != null && container.getSelected() == this){
			bgInstance.setColor(Color.DARK_GRAY);
		}else{
			bgInstance.setColor(Color.WHITE);
		}
		super.draw(batch, parentAlpha);
	}
	
	public void registerContainer(UIWeaponBottomBar cont){
		this.container = cont;
	}
}