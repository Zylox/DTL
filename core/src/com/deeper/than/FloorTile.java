package com.deeper.than;

import java.awt.Font;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class FloorTile extends Actor{
	
	public static final int TILESIZE = 64;
	protected Vector2 pos;
	private GridSquare gridSquare;
	
	private Ship ship;
	
	public FloorTile(int x, int y, GridSquare gridSquare){
		this(new Vector2(x,y), gridSquare);
	}
	
	public FloorTile(Vector2 pos, GridSquare gridSquare){
		this.pos = pos;
		this.gridSquare = gridSquare;

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
		Color color = batch.getColor();
		batch.setColor(new Color(getRedComponent(),1,1,1f));
		batch.draw(ship.getFloorTileImg(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(color);
		DTL.font.draw(batch, Float.toString(gridSquare.getRoom().getWaterLevel()), getWidth()/2, getHeight()/2);
	}
	
	private float getRedComponent(){
		float waterLevel = gridSquare.getRoom().getWaterLevel();
//		if(waterLevel < 1){
//			return 1f;
//		}else if(waterLevel < 25){
//			return .9f;
//		}else if(waterLevel < 50){
//			return .5f;
//		}else if(waterLevel < 75){
//			return .25f;
//		}else if(waterLevel >= 100){
//			return 0f;
//		}
//		return 0;
		return 1-waterLevel/100f;
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
	}
		
	public Vector2 getPos() {
		return pos;
	}
	
	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public GridSquare getGridSquare() {
		return gridSquare;
	}	
	
}
