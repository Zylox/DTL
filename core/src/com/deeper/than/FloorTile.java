package com.deeper.than;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.crew.Crew;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

/**
 * A ship's floortiles. Extends Actor
 * @author zach
 *
 */
public class FloorTile extends Actor{
	
	public static final int TILESIZE = 32;
	protected Vector2 pos;
	private GridSquare gridSquare;
	
	private Ship ship;

	/**
	 * Generates a floortile contained by gridsquare at index pos.
	 * @param x x component of the Grid position within thips the floortile is at
	 * @param y y component of the Grid position within thips the floortile is at
	 * @param gridSquare gridsquare that owns this tile.
	 */
	public FloorTile(int x, int y, GridSquare gridSquare){
		this(new Vector2(x,y), gridSquare);
	}
	

	
	/**
	 * Generates a floortile contained by gridsquare at index pos.
	 * @param pos Grid position within thips the floortile is at
	 * @param gridSquare gridsquare that owns this tile.
	 */
	public FloorTile(Vector2 pos, final GridSquare gridSquare){
		this.pos = pos;
		this.gridSquare = gridSquare;

		addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				gridSquare.getRoom().setHoveredOver(true);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				gridSquare.getRoom().setHoveredOver(false);
			}
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				printCoords();
				
				Crew crew = ((GameplayScreen)Screens.GAMEPLAY.getScreen()).getSelectedCrew();
				if(crew != null){
					crew.moveTo(gridSquare.getRoom().selectTileToWalkTo().getPos());
					((GameplayScreen)Screens.GAMEPLAY.getScreen()).setSelectedCrew(null);
				}
			
				return true;
		    }
		});
		
		setOrigin(TILESIZE/2, TILESIZE/2);
		setDebug(DTL.GRAPHICALDEBUG);
	}
	
	/**
	 * Prints the grid index position of the floortile to the debug out.
	 */
	public void printCoords(){
		DTL.printDebug(Float.toString(pos.x) + " " + Float.toString(pos.y));
	}
	


	/**
	 * Draws the floortile
	 */
	@Override
	public void draw(Batch batch, float alpha){
		Color color = batch.getColor();
		if(ship.colorizeRooms()){
			long seed = gridSquare.getRoom().getId();
			Random ran = new Random(seed);
			
			batch.setColor(new Color(ran.nextFloat(),ran.nextFloat(),ran.nextFloat(),1f));
		}else{
			batch.setColor(new Color(getRedComponent(),getGreenComponent(),1,1f));
		}
		batch.draw(ship.getFloorTileImg(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(color);
		
		//DTL.font.draw(batch, Float.toString(gridSquare.getRoom().getWaterLevel()), getWidth()/2, getHeight()/2);
	}
	
	/**
	 * Gets the red component of the tile color.
	 * The higher the water level, the less red there is
	 * @return parameterized red compnent
	 */
	private float getRedComponent(){
		float waterLevel = gridSquare.getRoom().getWaterLevel();
		if(waterLevel < 1){
			return 1f;
		}else if(waterLevel < 20){
			return .80f;
		}else if(waterLevel < 40){
			return .60f;
		}else if(waterLevel < 60){
			return .40f;
		}else if(waterLevel < 80){
			return .20f;
		}else if(waterLevel < 90){
			return .10f;
		}else if(waterLevel >= 90){
			return 0f;
		}else if(waterLevel > 0){
			
		}
		return 1;
		//return 1-waterLevel/100f;
	}
	
	/**
	 * Returns the green compnent of the tile color.
	 * Returns half when over 90% filled with water
	 * @return
	 */
	private float getGreenComponent(){
		float waterLevel = gridSquare.getRoom().getWaterLevel();
		if(waterLevel > 90){
			return .5f;
		}else{
			return 1f;
		}
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
