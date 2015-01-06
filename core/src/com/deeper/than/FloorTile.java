package com.deeper.than;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class FloorTile extends Actor{
	
	private static final float BASETEMP = 50;
	private static final float BASEPRESSURE = 50;
	private static final float BASEWATERLEVEL= 0;
	
	public static final int TILESIZE = 64;
	protected Vector2 pos;
	protected Neighbors neighs[];
	
	private Ship ship;

	protected float temp;
	protected float pressure;
	protected float waterLevel;
	
	public FloorTile(){
		this(null);
	}
	
	public FloorTile(int x, int y){
		this(new Vector2(x,y));
	}
	
	public FloorTile(Vector2 pos){
		this.pos = pos;
		neighs = new Neighbors[4];
		temp = BASETEMP;
		pressure = BASEPRESSURE;
		waterLevel = BASEWATERLEVEL;
		addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				printCoords();
				return true;
		    }
		});
		
		setOrigin(TILESIZE/2, TILESIZE/2);
		setDebug(DTL.GRAPHICALDEBUG);
	}
	
	public void printCoords(){
		DTL.printDebug(Float.toString(pos.x) + " " + Float.toString(pos.y));
	}
	
	@Override
	public void draw(Batch batch, float alpha){
		batch.draw(ship.getFloorTileImg(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
	}
	
	public void setNeighbor(int dir, Neighbors n){
		neighs[dir] = n;
	}
	
	public Neighbors getNeighbor(int dir){
		return neighs[dir];
	}
	
	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	
	public float getTemp() {
		return temp;
	}
	
	public void setTemp(float temp) {
		this.temp = temp;
	}
	
	public float getPressure() {
		return pressure;
	}
	
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	
	public float getWaterLevel() {
		return waterLevel;
	}
	
	public void setWaterLevel(float waterLevel) {
		this.waterLevel = waterLevel;
	}
}
