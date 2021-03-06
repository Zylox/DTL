/**
 * base level powerbar implementation
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
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
import com.badlogic.gdx.utils.SnapshotArray;
import com.deeper.than.screens.GameplayScreen;

/**
 * Base level class for powerbar implementation
 * @author zach
 *
 */
public class UIPowerBar extends WidgetGroup{
	public enum PowerBarState{
		POWERED,
		UNPOWERED,
		DAMAGED,
		IONIZED;
	}
	
	//sentinel for when a powerbar with no limit is wanted
	public static final int UNLIMITED_POWER = -999;
	public static final float PREF_WIDTH = PowerChunk.PREF_SIZE * 1.5f;
	public static final float BETWEEN_CHUNK_PADDING = 3;
	private int sections;
	private int powered;
	protected ArrayList<PowerChunk> powerChunks;
	protected Table table;
	protected boolean isLockedDown;
	
	public UIPowerBar(int sections, int powered){
		this.sections = sections;
		if(powered > sections){
			powered = sections;
		}
		this.powered = powered;
		isLockedDown = false;
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
		
		for(int i = 0; i<sections; i++){
			//as long as powered chunks remain set as powered and decrement temp
			if(i < getPowered()){
				powerChunks.add(new PowerChunk(PowerBarState.POWERED));
			}
			else if(i >= getPowered()){
				powerChunks.add(new PowerChunk(PowerBarState.UNPOWERED));
			}
		}
		table.setFillParent(true);
		table.add().expand();
		for(int i = powerChunks.size()-1; i >=0; i--){
			table.row();
			table.add(powerChunks.get(i)).space(BETWEEN_CHUNK_PADDING).prefHeight(PowerChunk.PREF_SIZE).prefWidth(PREF_WIDTH).left();
		}
		
		this.addActor(table);
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		Color color = batch.getColor().cpy();
		batch.setColor(Color.GREEN);
		if(isLockedDown){ 
			drawLockdownSquare(batch);
		}
		super.draw(batch, parentAlpha);
		batch.setColor(color);
	}
	
	/**
	 * Draws square in event of lockdown
	 * @param batch
	 */
	protected void drawLockdownSquare(Batch batch){
		batch.setColor(Color.YELLOW);
		float wOffset = getWidth()/4;
		GameplayScreen.drawEmptyRectable(getX()-wOffset, getY(), getWidth()+2*wOffset, getTopOfBarY() - getY(), 3, null, batch);
	}
	
	protected float getTopOfBarY(){
		return table.getChildren().get(0).getY() + table.getChildren().get(0).getHeight();
	}
	
	public boolean isEmpty(){
		if(getPowered() == UNLIMITED_POWER){
			return false;
		}
		return powered <= 0;
	}
	
	/**
	 * gives power to another bar.
	 * @param numToExchange how many sections to give power
	 * @param powerBar bar to transfer to
	 * @param minimumTransfer minimum amount that is able to be transfered. less available will result in no power transfered.
	 * @return the amount transfered
	 */
	public int givePower(int numToExchange ,UIPowerBar powerBar){
		//if other bar is infinite, just give it some power and be done with it
		if(powerBar.getPowered() == UNLIMITED_POWER){
			setPowered(getPowered() - numToExchange);
			updatePowered();
			return numToExchange;
		}
		
		//if bar is already full, retreate
		if(powerBar.sections == powerBar.powered){
			return 0;
		}

		//it if would overload it, adjust
		if(powerBar.powered + numToExchange > powerBar.sections){
			numToExchange = powerBar.sections - powerBar.powered;
		}
		//if this bar is infinite, give it what it wants
		if(getPowered() == UNLIMITED_POWER){
			powerBar.setPowered(powerBar.getPowered() + numToExchange);
			powerBar.updatePowered();
			return numToExchange;
		}
		
		//if there isnt enough to give, adjust to give what it can
		if(getPowered() < numToExchange){
			numToExchange = getPowered();
		}
		
		//finally, exchange
		setPowered(getPowered() - numToExchange);
		powerBar.setPowered(powerBar.getPowered() + numToExchange);
		updatePowered();
		powerBar.updatePowered();
		
		return numToExchange;
	}
	
	/**
	 * Adds listneer to all children
	 * @param listen
	 */
	public void addListenerToChildren(EventListener listen){
		for(Actor a : this.getChildren()){
			a.addListener(listen);
		}
	}
	
	/**
	 * UPdates chunks to powered or unpowerd status
	 */
	protected void updatePowered(){
		for(int i = 0; i < powerChunks.size(); i++){
			if(i < getPowered()){
				powerChunks.get(i).setState(PowerBarState.POWERED);
			}
			else if(i >= getPowered()){
				powerChunks.get(i).setState(PowerBarState.UNPOWERED);
			}
		}
	}
	
	protected SnapshotArray<Actor> getTableChildren(){
		return table.getChildren();
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
	
	
	public boolean isLockedDown() {
		return isLockedDown;
	}

	public void setLockedDown(boolean isLockedDown) {
		this.isLockedDown = isLockedDown;
	}


	/**
	 * @author zach
	 *
	 */
	protected class PowerChunk extends Widget{
		private static final float PREF_SIZE = 15;
		private PowerBarState state;
		
		private PowerChunk(PowerBarState state){
			this.state = state;
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha){
			if(isPowered()){
				batch.draw(GameplayScreen.highlight, getX(), getY(), getWidth(), getHeight());
			}else if(state == PowerBarState.UNPOWERED){
				GameplayScreen.drawEmptyRectable(getX(), getY(), getWidth(), getHeight(), 3, null, batch);
			}else if(state == PowerBarState.DAMAGED){
				GameplayScreen.drawEmptyRectable(getX(), getY(), getWidth(), getHeight(), 3, Color.BLACK, batch);
			}else if(state == PowerBarState.IONIZED){
				GameplayScreen.drawEmptyRectable(getX(), getY(), getWidth(), getHeight(), 3, Color.YELLOW, batch);
			}
		}

		public boolean isPowered() {
			return	state == PowerBarState.POWERED ? true : false;
		}

		public void setState(PowerBarState state){
			this.state = state;
		}
		
		
	}
	
}
