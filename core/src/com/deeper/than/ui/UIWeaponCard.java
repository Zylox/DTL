/**
 * 
 */
package com.deeper.than.ui;


import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.DTL;
import com.deeper.than.Room;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;
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
						if(isSelected()){
							unSelectSelf();
						}else{
							setSelfSelected();
						}
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
	private Weapon weapon;
	private Room target;
	private Image reticule;
	private boolean stickTarget;
	
	public UIWeaponCard(float width, float height,Weapon weapon, WeaponsModule weapModule, UIWeaponModuleReacBar moduleUI){
		this.weapon = weapon;
		this.addActor(weapon);
		container = null;
		powerBar = new UIWeaponReactor(weapon, weapModule, moduleUI);
		Container<UIWeaponReactor> barCont = new Container<UIWeaponReactor>(powerBar);
		barCont.fill();
		Table table = new Table();
		table.setFillParent(true);
		float padLeft = 3;
		table.add(barCont).expand().fillY().minWidth(20).padLeft(padLeft+2).bottom();
		Label label = new Label(weapon.getName(), DTL.skin);
		label.setFontScale(.9f);
		label.setWrap(true);
		table.add(label).fill().width(width-20-padLeft*2-2-6).padLeft(padLeft+2);
		bgInstance =new NinePatch(background);
		table.background(new NinePatchDrawable(bgInstance));
		this.addActor(table);
		this.clearListeners();
		this.addListener(this.targetSelect);
		this.addListener(powerBar.clicker);
		setWidth(width);
		setHeight(height);
		
		reticule = new Image(GameplayScreen.reticule);
		reticule.setTouchable(Touchable.disabled);
		setTarget(null);
		this.addActor(reticule);
		this.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				if(powerBar.getPowered() ==0){
					unSelectSelf();
				}
				
				fireIfPossible();

				
				return false;
			}
		});
	}
	
	private void fireIfPossible(){
		if(weapon.isCharged() && target != null){
			container.shotFired();
			weapon.fire(target);
			weapon.startCharging();
			if(!stickTarget){
				setTarget(null);
			}
		}
	}
	
	public void tryGivePower(){
		powerBar.setDesirePower(true);
	}
	
	public void takePower(){
		powerBar.setDesirePower(false);
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
	
	public boolean isSelected(){
		if(container!=null){
			return container.getSelected() == this;
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
		batch.setColor(Color.GREEN);
		batch.draw(GameplayScreen.highlight, getX()+1,getY()+1,4,(getHeight()-2)*weapon.getCooldownPercent());
	}
	
	public void registerContainer(UIWeaponBottomBar cont){
		this.container = cont;
	}

	public Room getTarget() {
		return target;
	}

	public void setTarget(Room target) {
		if(target != null){
			if(target.equals(this.target)){
				stickTarget = true;
				reticule.setColor(Color.YELLOW);
			}else{
				stickTarget = false;
				reticule.setColor(Color.RED);
			}
			Vector2 centerLoc = target.getCenterLocInStage().cpy();
			this.stageToLocalCoordinates(centerLoc);
			reticule.setPosition(centerLoc.x - reticule.getWidth()/2, centerLoc.y-reticule.getHeight()/2);
			if(!target.isPlayerRoom()){
				reticule.setVisible(true);
			}
			
		}else{
			stickTarget = false;
			reticule.setVisible(false);
		}
		this.target = target;
	}
	
	
}
