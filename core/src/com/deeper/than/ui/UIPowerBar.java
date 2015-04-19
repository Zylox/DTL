package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.screens.GameplayScreen;

public class UIPowerBar extends WidgetGroup{
	public static final int UNLIMITED_POWER = -3;
	public static final float PREF_WIDTH = PowerChunk.PREF_SIZE * 1.5f;
	private int sections;
	private int powered;
	private ArrayList<PowerChunk> powerChunks;
	protected Table table;
	
	public UIPowerBar(int sections, int powered){
		this.sections = sections;
		if(powered > sections){
			powered = sections;
		}
		this.powered = powered;
		
		fillTable();
	}
	
	public void adjustSegments(int sections, int powered){
		table.reset();
		for(int i = 0; i<this.sections; i++){
			powerChunks.remove(0);
		}
		this.sections = sections;
		this.powered = powered;
		
		fillTable();
	}
	
	
	private void fillTable(){
		if(table == null || powerChunks == null){
			table = new Table();
			this.powerChunks = new ArrayList<PowerChunk>();
		}
		
		int temp = powered;
		for(int i = 0; i<sections; i++){
			//as long as powered chunks remain set as powered and decrement temp
			powerChunks.add(new PowerChunk(temp-->0?true:false));
		}
		
		table.setFillParent(true);
		table.add().expand();
		for(int i = powerChunks.size()-1; i >=0; i--){
			table.row();
			table.add(powerChunks.get(i)).space(3).prefHeight(PowerChunk.PREF_SIZE).prefWidth(PREF_WIDTH).left();
		}
		
		this.addActor(table);
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		Color color = batch.getColor().cpy();
		batch.setColor(Color.TEAL);
		super.draw(batch, parentAlpha);
		batch.setColor(color);
	}
	
	/**
	 * gives power to another bar.
	 * @param numToExchange how many sections to give power
	 * @param powerBar bar to transfer to
	 * @return false if there is not enough power, as much power as possible transfered
	 */
	public boolean givePower(int numToExchange ,UIPowerBar powerBar){
		boolean fullExchange;
		if(powerBar.sections == powerBar.powered){
			return false;
		}
		if(powerBar.powered + numToExchange > powerBar.sections){
			numToExchange = powerBar.sections - powerBar.powered;
		}
		
		if(getPowered() == UNLIMITED_POWER){
			powerBar.setPowered(powerBar.getPowered() + numToExchange);
			powerBar.updatePowered();
			return true;
		}
		
		if(getPowered() < numToExchange){
			fullExchange = false;
			numToExchange = getPowered();
		}else{
			fullExchange = true;
		}
		
		setPowered(getPowered() - numToExchange);
		powerBar.setPowered(powerBar.getPowered() + numToExchange);
		updatePowered();
		powerBar.updatePowered();
		
		return fullExchange;
	}
	
	public void addListenerToChildren(EventListener listen){
		for(Actor a : this.getChildren()){
			a.addListener(listen);
		}
	}
	
	private void updatePowered(){
		int temp = powered;
		for(int i = 0; i < powerChunks.size(); i++){
			powerChunks.get(i).setPowered(temp-->0?true:false);
		}
	}
	
	protected Cell<Widget> addOnNewRow(Widget widget){
		table.row();
		return table.add(widget);
	}

	public int getSections() {
		return sections;
	}

	public void setSections(int sections) {
		this.sections = sections;
	}

	public int getPowered() {
		return powered;
	}

	public void setPowered(int powered) {
		if(powered > getSections()){
		}
		this.powered = powered;
	}
	
	private class PowerChunk extends Widget{
		private static final float PREF_SIZE = 15;
		boolean powered;
		
		public PowerChunk(boolean powered){
			this.powered = powered;
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha){
			if(isPowered()){
				batch.draw(GameplayScreen.highlight, getX(), getY(), getWidth(), getHeight());
			}else{
				//batch.draw(GameplayScreen.highlight, getX(), getY(), getWidth(), getHeight());
				GameplayScreen.drawEmptyRectable(getX(), getY(), getWidth(), getHeight(), 3, null, batch);
			}
		}

		public boolean isPowered() {
			return powered;
		}

		public void setPowered(boolean powered) {
			this.powered = powered;
		}
		
		
	}
	
}
