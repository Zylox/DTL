package com.deeper.than;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.Cooldown;
import com.deeper.than.modules.EngineModule;
import com.deeper.than.screens.GameplayScreen;

public class FastDrive extends WidgetGroup{
	private final static Color CHARGE_COLOR = Color.GREEN;
	private final static Color EMPTY_COLOR = Color.DARK_GRAY;
	
	private static final float CHARGE_MAX = 30;
	private static final float BASE_CHARGE_RATE = 1;
	
	private Ship ship;
	private EngineModule engine;
	private BridgeModule bridge;
	private Cooldown charge;
	private Stack stack;
	
	public FastDrive(Ship ship){
		this.ship = ship;
		this.engine = (EngineModule)ship.getModule(EngineModule.class);
		this.bridge = (BridgeModule)ship.getModule(BridgeModule.class);
		charge = new Cooldown(CHARGE_MAX);
		charge.startCooldown();
		stack = new Stack();
		Table table = new Table();
		stack.add(new ChargeBar());
		Label chargeLabel = new Label("Dive",DTL.skin);
		Table tab2 = new Table();
		tab2.add(chargeLabel).center();
		stack.add(tab2);
		table.add(stack).expand().fill().center();
		table.setFillParent(true);
		this.addActor(table);
	
	}

	public void setPos(){
		
	}
	
	public void update(boolean inPeril){
		if(!inPeril && charge.isOnCooldown()){
			charge.endCooldown();
		}
		
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
	

	private class ChargeBar extends Widget{
		private static final int PADDING_OFFSET = 2;
		@Override
		public void draw(Batch batch, float parentAlpha){
			Color color = batch.getColor().cpy();
			batch.setColor(Color.GRAY);
			batch.draw(GameplayScreen.highlight, getX(), getY(),this.getWidth(),this.getHeight());
			batch.setColor(CHARGE_COLOR);
			float progress = getChargePercent()*(this.getWidth() - 2 * PADDING_OFFSET);
			batch.draw(GameplayScreen.highlight, getX()+PADDING_OFFSET, getY()+PADDING_OFFSET, progress  ,this.getHeight() - 2 * PADDING_OFFSET);
			batch.setColor(EMPTY_COLOR);
			batch.draw(GameplayScreen.highlight, getX() + progress + PADDING_OFFSET, getY() + PADDING_OFFSET, (1 -getChargePercent())*(getWidth()- 2 * PADDING_OFFSET),getHeight()- 2 * PADDING_OFFSET);
			batch.setColor(color);
		}
	}
	
}
