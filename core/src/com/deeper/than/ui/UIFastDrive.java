/**
 * The ui widget for the players jump drive.
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */

package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.DTL;
import com.deeper.than.Ship;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.Cooldown;
import com.deeper.than.modules.EngineModule;
import com.deeper.than.modules.Modules;
import com.deeper.than.screens.GameplayScreen;

/**
 * Jump drive ui widget
 * @author zach
 *
 */
public class UIFastDrive extends WidgetGroup{
	private final static Color CHARGE_COLOR = Color.BLUE;
	private final static Color FULL_COLOR = new Color(.1f,.7f,.0f,1);
	private final static Color EMPTY_COLOR = Color.DARK_GRAY;
	private static Image engineIcon;
	private static Image engineBackground;
	private static Image bridgeIcon;
	private static Image bridgeBackground;
	
	private static final float CHARGE_MAX = 30;
	private static final float BASE_CHARGE_RATE = 1;
	
	private Ship ship;
	private EngineModule engine;
	private BridgeModule bridge;
	private Cooldown charge;

	private Container<DisruptionTab> disCont;
	private DisruptionTab disruptionTab;
	public boolean tabDown;
	public boolean layOutTab;
	
	public UIFastDrive(Ship ship){
		this.ship = ship;
		this.engine = (EngineModule)ship.getModule(EngineModule.class);
		this.bridge = (BridgeModule)ship.getModule(BridgeModule.class);
		charge = new Cooldown(CHARGE_MAX);
		charge.startCooldown();
		//stack lets you stack ui elements
		Stack stack = new Stack();
		stack.add(new ChargeBar());
		Label chargeLabel = new Label("Dive",DTL.skin);
		Container<Label> chargeLabelCont = new Container<Label>(chargeLabel);
		chargeLabelCont.center();
		stack.add(chargeLabelCont);
		Container<Stack> chargeCont = new Container<Stack>(stack);
		chargeCont.fill().center();
		chargeCont.setFillParent(true);
		disruptionTab = new DisruptionTab();
		tabDown = false;
		layOutTab = false;
		
		//set up tab taht falls down when a reason you cant jump exists
		disCont = new Container<UIFastDrive.DisruptionTab>(disruptionTab);
		int disWidth = 58;
		int disHeight= 25;
		disCont.setSize(disWidth, disHeight);
		disCont.setX(this.getX());
		disCont.setY(this.getY());
		disCont.fill();
		disCont.setVisible(false);
		this.addActor(disCont);
		this.addActor(chargeCont);
	}
	

	
	public void update(boolean inPeril){
		//layout tab on the first call
		if(!layOutTab){
			disCont.setX(disCont.getX()+this.getWidth()/2-disCont.getWidth()/2);
			layOutTab = true;
		}
		//if there is no peril, just charge the drive
		if(!inPeril && charge.isOnCooldown()){
			charge.endCooldown();
		}
		//set tab to descend if drive is disturbed
		if((!engine.enginesOn() || !bridge.canJump()) && !tabDown){
			disCont.setVisible(true);
			float destinationY = -disCont.getHeight();
			disCont.addAction(Actions.moveTo(disCont.getX(), destinationY, .1f, Interpolation.linear));
			tabDown = true;
		}
		//set tab back up if there are no more disruptions 
		if(engine.enginesOn() && bridge.canJump() && tabDown){
			
			disCont.addAction(Actions.sequence(Actions.moveTo(disCont.getX(), 0, .1f, Interpolation.linear), new RunnableAction(){
				public void run(){
					disCont.setVisible(false);
				}
			}));
			tabDown = false;
		}
		//advance charge if needbe
		if(engine.enginesOn() && charge.isOnCooldown()){
			charge.advanceCooldown(BASE_CHARGE_RATE*engine.getDriveChargeModifier());
		}
	}
	
	/**
	 * Not used atm, might be used for ai in the future
	 * @return
	 */
	public float getChargePercent(){
		return charge.getCooldownProgress() / charge.getCooldownLimit();
	}
	
	public void resetCharge(){
		charge.startCooldown();
	}
	
	public static void loadAssets(){
		bridgeIcon = new Image(Modules.getIcon(BridgeModule.class.getCanonicalName()));
		engineIcon = new Image(Modules.getIcon(EngineModule.class.getCanonicalName()));
		engineBackground = new Image(GameplayScreen.highlight);
		bridgeBackground = new Image(GameplayScreen.highlight);
	}
	
	/**
	 * Tab that falls down when engine or bridge disrupted
	 * @author zach
	 *
	 */
	private class DisruptionTab extends WidgetGroup{
		private static final int OULTINE_SIZE = 2;
		
		private DisruptionTab(){
			//layout tables
			Table table = new Table();
			table.setFillParent(true);
			Stack eStack = new Stack();
			eStack.add(engineBackground);
			eStack.add(engineIcon);
			table.add(eStack).spaceRight(OULTINE_SIZE).fill().expand();
			Stack bStack = new Stack();
			bStack.add(bridgeBackground);
			bStack.add(bridgeIcon);
			table.add(bStack).fill().expand();
			table.pad(OULTINE_SIZE);
			this.addActor(table);
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha){
			Color color = batch.getColor().cpy();
			//draw background
			batch.setColor(Color.GRAY);
			batch.draw(GameplayScreen.highlight, this.getX(), this.getY(),this.getWidth(),this.getHeight());
			//determine colors by what problems exist
			if(engine.enginesOn()){
				engineBackground.setColor(FULL_COLOR);
			}else{
				engineBackground.setColor(Color.RED);
			}
			
			if(bridge.canJump()){
				bridgeBackground.setColor(FULL_COLOR);
			}else{
				bridgeBackground.setColor(Color.RED);
			}
			
			batch.setColor(color);
			super.draw(batch, parentAlpha);
		}
	}

	/**
	 * Bar that charges as drive powers up
	 * @author zach
	 *
	 */
	private class ChargeBar extends Widget{
		private static final int PADDING_OFFSET = 2;
		@Override
		public void draw(Batch batch, float parentAlpha){
			Color color = batch.getColor().cpy();
			//draw background
			batch.setColor(Color.GRAY);
			batch.draw(GameplayScreen.highlight, getX(), getY(),this.getWidth(),this.getHeight());
			//determine color of bar
			if(charge.getCooldownProgress() != charge.getCooldownLimit()){
				batch.setColor(CHARGE_COLOR);
			}else{
				batch.setColor(FULL_COLOR);
			}
			float progress = getChargePercent()*(this.getWidth() - 2 * PADDING_OFFSET);
			//draw full part
			batch.draw(GameplayScreen.highlight, getX()+PADDING_OFFSET, getY()+PADDING_OFFSET, progress  ,this.getHeight() - 2 * PADDING_OFFSET);
			//draw empty part
			batch.setColor(EMPTY_COLOR);
			batch.draw(GameplayScreen.highlight, getX() + progress + PADDING_OFFSET, getY() + PADDING_OFFSET, (1 -getChargePercent())*(getWidth()- 2 * PADDING_OFFSET),getHeight()- 2 * PADDING_OFFSET);
			batch.setColor(color);
		}
	}
	
}
