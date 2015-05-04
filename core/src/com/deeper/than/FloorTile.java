/**
 * Contrians the implementation of the individual tiles used in the ship
 * Also contains a lot of the code for what happens when you click on the floor
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.crew.Crew;
import com.deeper.than.modules.Module;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

/**
 * A ship's floortiles. Extends Actor
 * @author zach
 *
 */
public class FloorTile extends Actor{
	//size of the floortile. Also used as the scaling factor for most if not all ship things
	public static final int TILESIZE = 32;
	protected Vector2 pos;
	//Square the tile belongs to
	private GridSquare gridSquare;
	
	//Ship the tile is in
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
	

	public void setHovered(boolean hovered){
		this.gridSquare.getRoom().setHoveredOver(hovered);
	}
	
	/**
	 * Generates a floortile contained by gridsquare at index pos.
	 * @param pos Grid position within thips the floortile is at
	 * @param gridSquare gridsquare that owns this tile.
	 */
	public FloorTile(Vector2 pos, final GridSquare gridSquare){
		this.pos = pos;
		this.gridSquare = gridSquare;

		ClickListener click = new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				setHovered(true);
			}
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
				setHovered(false);
			}
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				printCoords();
				
				if(button == Buttons.RIGHT){
					//if a crew member is selected, and the player right clicks a tile, initiate a pathfinding attempt
					Crew crew = ((GameplayScreen)Screens.GAMEPLAY.getScreen()).getSelectedCrew();
					if(crew != null && crew.getOccupiedShip() == ship){
						//attempt to retrieve a walkable location in the room
						GridSquare gs = gridSquare.getRoom().selectTileToWalkTo();
						Vector2 movePos;
						if(gs!= null){
							movePos = gs.getPos();
						}else{
							//if a position in the room isnt available, find one that is nearby using BFS
							movePos = crew.findNearestOpenSpot(getPos());
						}
						
						//queues a pathfinding attempt in the selected crew member
						if(movePos != null){
							crew.moveTo(movePos);
						}
					}else{
						//debug tool to give ionic damage to the module in that room
//						Module mod = gridSquare.getRoom().getModule();
//						if(mod != null) mod.receiveIonicCharge();
					}
					
					//Alternatively, if there is a player weapon trying to target a room,
					//and the tile is in an enem ship, set the target to null with a right click
					if(((GameplayScreen)Screens.GAMEPLAY.getScreen()).isTargetting() && ship instanceof EnemyShip){
						((GameplayScreen)Screens.GAMEPLAY.getScreen()).getTargetting().setTarget(null);
					}
				
					return true;
				}else if(button == Buttons.LEFT){
					//if the floortile belongs to an enemy ship, and the player is trying to target a weapon,
					//set the room the floortile belongs to as the target.
					if(((GameplayScreen)Screens.GAMEPLAY.getScreen()).isTargetting() && ship instanceof EnemyShip){
						((GameplayScreen)Screens.GAMEPLAY.getScreen()).getTargetting().setTarget(gridSquare.getRoom());
					}
					
					//debug tool for damaging modules
//					if(!((GameplayScreen)Screens.GAMEPLAY.getScreen()).isCrewSelected()){
//						Module mod = gridSquare.getRoom().getModule();
//						if(mod != null){
//							if(button == Buttons.LEFT){
//								mod.recieveDamage();
//							}
//						}
//					}
					//deselect crew on a left click
					((GameplayScreen)Screens.GAMEPLAY.getScreen()).setSelectedCrew(null);
					return false;
				}else{

					return false;
				}
			}
		};
		addListener(click);
		
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
			
			batch.setColor(new Color(ran.nextFloat(),ran.nextFloat(),ran.nextFloat(),alpha));
		}else{
			batch.setColor(new Color(getRedComponent(),getGreenComponent(),1,alpha));
		}
		batch.draw(ship.getFloorTileImg(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(color);
		
		//prints the water level in the center of the room for debug puproses
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
		}else if(waterLevel > 50){
			return .8f;
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
