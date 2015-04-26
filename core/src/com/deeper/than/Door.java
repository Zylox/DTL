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

public class Door extends CellBorder{
	
	public static enum DoorState{
		Opening,
		Closing,
		Open,
		Closed;
	}
	

	public static final float DOORSIZELONG = FloorTile.TILESIZE/2;
	public static final float DOORSIZESHORT = DOORSIZELONG/3;
	

	private int doorId;
	private HatchControlModule hatchControl;
	private DoorState doorState;
	private float openAmount;
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

		if(getShip() instanceof PlayerShip){
			addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					changeOpen();
					printDoorState();
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
		
		
		float middleX = gs.getX() + FloorTile.TILESIZE/2;
		float middleY = gs.getY() + FloorTile.TILESIZE/2;
		
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
	
	public void changeOpenOverrideHatch(){
		if(doorState == DoorState.Open || doorState == DoorState.Opening){
			doorState = DoorState.Closing;
		}else{
			doorState = DoorState.Opening;
		}
		addAction(new DoorOpen());
	}
	
	public void printDoorState(){
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
		float width = (FloorTile.TILESIZE - DOORSIZELONG)/2;
		float height;
		if(wallType == WallType.interior){
			height = Wall.INTERIORWALLSHORTSIDE;
			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				Wall.getExteriorWallImg().draw(batch, getX(), getY()+Wall.INTERIORWALLSHORTSIDE-height/2, -width, height);
				Wall.getExteriorWallImg().draw(batch, getX()+FloorTile.TILESIZE-width, getY()+Wall.INTERIORWALLSHORTSIDE-height/2, -width, height);
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
				Wall.getExteriorWallImg().draw(batch, getX()+Wall.INTERIORWALLSHORTSIDE-height/2, getY(), height, -width);
				Wall.getExteriorWallImg().draw(batch, getX()+Wall.INTERIORWALLSHORTSIDE-height/2, getY()+FloorTile.TILESIZE-width, height, -width);
			}
		}else{
			height = Wall.EXTERIORWALLSHORTSIDE;
			
			
			
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
		boolean isEnemy = false;
		if(crewMem.getOwnerShipId() != ship.getId()){
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

	private class DoorHalf extends Actor{		
		
		public boolean left;
		
		public DoorHalf(boolean left){
			this.left = left;
		}
		
		
		@Override
		public void draw(Batch batch, float alpha){
		

			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				if(left){
					ship.getDoorImg().draw(batch, getX(), getY(), getWidth()-openAmount, getHeight());
				}
				else {	
					ship.getDoorImg().draw(batch, getX()+openAmount, getY(), getWidth()-openAmount, getHeight());
				
				}
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
			
				if(left){
					//ship.getDoorImg().setColor(Color.GREEN);
					ship.getDoorImg().draw(batch, getX(), getY(), getWidth(), getHeight()-openAmount);
				}
				else {	
					ship.getDoorImg().draw(batch, getX(), getY()+openAmount, getWidth(), getHeight()-openAmount);
				
				}
			}

		}
		
	
	}

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
				openAmount += DTL.getRatePerTimeStep(DOORSIZELONG/16*60);
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
