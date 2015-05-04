/**
 * Ship doors implementation
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.deeper.than.Wall.WallType;
import com.deeper.than.crew.Crew;
import com.deeper.than.modules.HatchControlModule;


/**
 * Ship doors
 * @author zach
 *
 */
public class Door extends CellBorder{
	
	public static enum DoorState{
		Opening,
		Closing,
		Open,
		Closed;
	}
	
	//Size of the long and short side of the door
	public static final float DOORSIZELONG = FloorTile.TILESIZE/2;
	public static final float DOORSIZESHORT = DOORSIZELONG/3;
	
	
	private int doorId;
	//Module that determines if door can open or close under certain circumstances
	private HatchControlModule hatchControl;
	private DoorState doorState;
	//How much the door is opened at a certain point
	private float openAmount;
	//Doors can on internal or external walls, so this must be taken into consideration
	private WallType wallType;
	
	public Door(float x, float y, int orientation, Ship ship){
		super(x,y,orientation,ship);
	}
	
	public Door(Vector2 pos, int orientation, Ship ship){
		super(pos,orientation,ship);
	}
	
	public void init(){
		
		doorState = DoorState.Closed;
		openAmount=0;

		//if the ship is the players ship, let them control opening and closing of the doors
		if(getShip() instanceof PlayerShip){
			addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					changeOpen();
					printDoorControlState();
					return true;
			    }
			});
		}
		
		reinit();
	}
	
	public void reinit(){
		
		for(Actor a : getChildren()){
			removeActor(a);
		}
		DoorHalf[] doors = new DoorHalf[2];
		
		
		float middleX = ownerSquare.getX() + FloorTile.TILESIZE/2;
		float middleY = ownerSquare.getY() + FloorTile.TILESIZE/2;
		
		//initialize door positioning for drawing later
		switch (orientation){
			case Neighbors.UP:
				setBounds(middleX - DOORSIZELONG/2, middleY + FloorTile.TILESIZE/2 - DOORSIZESHORT/2, DOORSIZELONG, DOORSIZESHORT);
				doors[0] = new DoorHalf(true);
				doors[0].setBounds(0, 0, this.getWidth()/2, this.getHeight());
				doors[1] = new DoorHalf(false);
				doors[1].setBounds(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
				break;
			case Neighbors.DOWN:
				setBounds(middleX - DOORSIZELONG/2, middleY - FloorTile.TILESIZE/2 -DOORSIZESHORT/2, DOORSIZELONG, DOORSIZESHORT);
				doors[0] = new DoorHalf(true);
				doors[0].setBounds(0, 0, this.getWidth()/2, this.getHeight());
				doors[1] = new DoorHalf(false);
				doors[1].setBounds(this.getWidth()/2, 0, this.getWidth()/2, this.getHeight());
				break;
			case Neighbors.LEFT:
				setBounds(middleX - FloorTile.TILESIZE/2 -DOORSIZESHORT/2, middleY - DOORSIZELONG/2, DOORSIZESHORT, DOORSIZELONG);
				doors[0] = new DoorHalf(true);
				doors[0].setBounds(0, 0, this.getWidth(), this.getHeight()/2);
				doors[1] = new DoorHalf(false);
				doors[1].setBounds(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
				break;
			case Neighbors.RIGHT:
				setBounds(middleX + FloorTile.TILESIZE/2 -DOORSIZESHORT/2, middleY - DOORSIZELONG/2, DOORSIZESHORT, DOORSIZELONG);
				doors[0] = new DoorHalf(true);
				doors[0].setBounds(0, 0, this.getWidth(), this.getHeight()/2);
				doors[1] = new DoorHalf(false);
				doors[1].setBounds(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
				break;
		}
			
		doors[0].setDebug(DTL.GRAPHICALDEBUG);
		doors[1].setDebug(DTL.GRAPHICALDEBUG);
		
		this.addActor(doors[0]);
		this.addActor(doors[1]);
	}
	
	public void changeOpen(){
		//a hatch control is needed to open a door under normal circumstances
		if(hatchControl == null){
			return;
		}
		
		if(hatchControl.canControlDoors()){
			if(doorState == DoorState.Open || doorState == DoorState.Opening){
				doorState = DoorState.Closing;
			}else{
				doorState = DoorState.Opening;
			}
			addAction(new DoorOpen());
		}
	}
	
	/**
	 * Only to be used in situations where something otehr than the player is controlling the door
	 * ex. that ships crew moving through the door.
	 */
	public void changeOpenOverrideHatch(){
		if(doorState == DoorState.Open || doorState == DoorState.Opening){
			doorState = DoorState.Closing;
		}else{
			doorState = DoorState.Opening;
		}
		addAction(new DoorOpen());
	}
	
	public void printDoorControlState(){
		if(hatchControl != null){
			if(!hatchControl.canControlDoors()){
				DTL.printDebug("Door control disabled");
			}
		}else{
			DTL.printDebug("No hatch control Module");
		}
	}

	
	@Override
	public void draw(Batch batch, float parentAlpha){
		//width of the actual door section of the wall
		float width = (FloorTile.TILESIZE - DOORSIZELONG)/2;
		float height;
		//deterimines interior or exterior wall
		if(wallType == WallType.interior){
			height = Wall.INTERIORWALLSHORTSIDE;
			//and draws two wall sections accoring to orientation
			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				Wall.getExteriorWallImg().draw(batch, getX(), getY()+Wall.INTERIORWALLSHORTSIDE-height/2, -width, height);
				Wall.getExteriorWallImg().draw(batch, getX()+FloorTile.TILESIZE-width, getY()+Wall.INTERIORWALLSHORTSIDE-height/2, -width, height);
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
				Wall.getExteriorWallImg().draw(batch, getX()+Wall.INTERIORWALLSHORTSIDE-height/2, getY(), height, -width);
				Wall.getExteriorWallImg().draw(batch, getX()+Wall.INTERIORWALLSHORTSIDE-height/2, getY()+FloorTile.TILESIZE-width, height, -width);
			}
		}else{
			height = Wall.EXTERIORWALLSHORTSIDE;
			//exterior walls have to be inset based on their orientation, so each draw call is different
			if(orientation == Neighbors.DOWN){
				Wall.getExteriorWallImg().draw(batch, getX(), getY()+DOORSIZESHORT/2, -width, height);
				Wall.getExteriorWallImg().draw(batch, getX()+FloorTile.TILESIZE-width, getY()+DOORSIZESHORT/2, -width, height);
			}
			else if(orientation == Neighbors.UP){
				Wall.getExteriorWallImg().draw(batch, getX(), getY()+DOORSIZESHORT/2-height, -width, height);
				Wall.getExteriorWallImg().draw(batch, getX()+FloorTile.TILESIZE-width, getY()+DOORSIZESHORT/2-height, -width, height);
			}
			else if(orientation == Neighbors.RIGHT){
				Wall.getExteriorWallImg().draw(batch, getX()+DOORSIZESHORT/2-height, getY(), height, -width);
				Wall.getExteriorWallImg().draw(batch, getX()+DOORSIZESHORT/2-height, getY()+FloorTile.TILESIZE-width, height, -width);
			}
			else if(orientation == Neighbors.LEFT){
				Wall.getExteriorWallImg().draw(batch, getX()+DOORSIZESHORT/2, getY(), height, -width);
				Wall.getExteriorWallImg().draw(batch, getX()+DOORSIZESHORT/2, getY()+FloorTile.TILESIZE-width, height, -width);
			}
		}
		Color color = batch.getColor().cpy();
		//if the doors are not controllable by the player, tint them
		//in this case its a pink tint.
		if(hatchControl == null || !hatchControl.canControlDoors()){
			batch.setColor(Color.PINK);
		}
		super.draw(batch, parentAlpha);
		batch.setColor(color);
		
	}
	
	public boolean isOpen(){
		if(doorState == DoorState.Open || doorState == DoorState.Opening){
			return true;
		}
		return  false;
	}	
	
	public boolean isOpenableByCrewMem(Crew crewMem){
		//The only situation in which the door should not be able to be opened is if
		//there is a high enough leveled hatch control the keep enemy crew out
		boolean isEnemy = false;
		if(crewMem.getOwnerShipId() != ownerShip.getId()){
			isEnemy = true;
		}
		
		if(hatchControl != null && ((isEnemy && !hatchControl.canEnemiesUseDoors()))){
			return false;
		}
		
		return true;
	}
		
	public void setHatchControlModule(HatchControlModule hcm){
		this.hatchControl = hcm;
	}
	
	public float getOpenAmount() {
		return openAmount;
	}

	public void setOpenAmount(float openAmount) {
		this.openAmount = openAmount;
	}

	public int getDoorId() {
		return doorId;
	}

	public void setDoorId(int doorId) {
		this.doorId = doorId;
	}

	public WallType getWallType() {
		return wallType;
	}

	public void setWallType(WallType wallType) {
		this.wallType = wallType;
	}

	/**
	 * The half sections of the full door
	 * @author zach
	 *
	 */
	private class DoorHalf extends Actor{		
		
		//if the door is the left segment, true. false indicates the right segment
		public boolean left;
		
		public DoorHalf(boolean left){
			this.left = left;
		}
		
		//Uses a ninepath for the door half, and draw it to correct size based on how open the door is
		@Override
		public void draw(Batch batch, float alpha){
			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				if(left){
					ownerShip.getDoorImg().draw(batch, getX(), getY(), getWidth()-openAmount, getHeight());
				}
				else {	
					ownerShip.getDoorImg().draw(batch, getX()+openAmount, getY(), getWidth()-openAmount, getHeight());
				}
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
			
				if(left){
					ownerShip.getDoorImg().draw(batch, getX(), getY(), getWidth(), getHeight()-openAmount);
				}
				else {	
					ownerShip.getDoorImg().draw(batch, getX(), getY()+openAmount, getWidth(), getHeight()-openAmount);
				}
			}
		}
	}

	/**
	 * Scene2d actor action to open the door.
	 * @author zach
	 *
	 */
	private class DoorOpen extends Action{
		
		
		@Override
		public boolean act(float delta) {
			float longSide=0;
			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				longSide = getWidth()/2;
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
				longSide = getHeight()/2;
			}
			if(doorState == DoorState.Opening){
				openAmount += DTL.getRatePerTimeStep(DOORSIZELONG/16*60); //doorsizelong/16*60frames was a number that worked out nicely before frame independence was implemented
				if(openAmount >= longSide-DOORSIZELONG/8){                      
					openAmount = longSide-DOORSIZELONG/8;
					doorState = DoorState.Open;
					return true;
				}
			}else if(doorState == DoorState.Closing){
				openAmount -= DTL.getRatePerTimeStep(DOORSIZELONG/16*60);;
				if(openAmount <=0){
					openAmount = 0;
					doorState = DoorState.Closed;
					return true;
				}
			}	
			return false;
		}
	}
}
