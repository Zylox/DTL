package com.deeper.than;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.deeper.than.Wall.WallType;

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

		addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				changeOpen();
				printDoorState();
				return true;
		    }
		});
		
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
			
		doors[0].setDebug(DTL.DEBUG);
		doors[1].setDebug(DTL.DEBUG);
		
		this.addActor(doors[0]);
		this.addActor(doors[1]);
	}
	
	public void changeOpen(){
		if(doorState == DoorState.Open || doorState == DoorState.Opening){
			doorState = DoorState.Closing;
		}else{
			doorState = DoorState.Opening;
		}
		

	}
	
	public void printDoorState(){
		switch(doorState){
		case Open : 
			System.out.println("Door open");
			break;
		case Closed :
			System.out.println("Door closed");
			break;
		case Opening : 
			System.out.println("Door opening");
			break;
		case Closing : 
			System.out.println("Door closing");
			break;
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
//			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
//				Wall.getExteriorWallImg().draw(batch, getX(), getY()+Wall.INTERIORWALLSHORTSIDE-height/2, -width, height);
//				Wall.getExteriorWallImg().draw(batch, getX()+FloorTile.TILESIZE-width, getY()+Wall.INTERIORWALLSHORTSIDE-height/2, -width, height);
//			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
//				Wall.getExteriorWallImg().draw(batch, getX()+Wall.INTERIORWALLSHORTSIDE-height/2, getY(), height, -width);
//				Wall.getExteriorWallImg().draw(batch, getX()+Wall.INTERIORWALLSHORTSIDE-height/2, getY()+FloorTile.TILESIZE-width, height, -width);
//			}
			
			
			
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
		
//		if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
//			Wall.getExteriorWallImg().draw(batch, getX(), getY()+DOORSIZESHORT/2-height/2, -width, height);
//			Wall.getExteriorWallImg().draw(batch, getX()+FloorTile.TILESIZE-width, getY()+DOORSIZESHORT/2-height/2, -width, height);
//		}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
//			Wall.getExteriorWallImg().draw(batch, getX()+DOORSIZESHORT/2-height/2, getY(), height, -width);
//			Wall.getExteriorWallImg().draw(batch, getX()+DOORSIZESHORT/2-height/2, getY()+FloorTile.TILESIZE-width, height, -width);
//		}
		super.draw(batch, parentAlpha);
		
	}
	
	public boolean isOpen(){
		if(doorState == DoorState.Open || doorState == DoorState.Opening){
			return true;
		}
		return  false;
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

		
			float longSide=0;
			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				longSide = this.getWidth();
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
				longSide = this.getHeight();
			}
			if(doorState == DoorState.Opening){
				openAmount += DOORSIZELONG/32;
				if(openAmount >= longSide-DOORSIZELONG/8){
					openAmount = longSide-DOORSIZELONG/8;
					doorState = DoorState.Open;
				}
			}else if(doorState == DoorState.Closing){
				openAmount -= DOORSIZELONG/32;
				if(openAmount <=0){
					openAmount = 0;
					doorState = DoorState.Closed;
					
				}
			}
		
			if(orientation == Neighbors.UP || orientation == Neighbors.DOWN){
				if(left){
					ship.getDoorImg().draw(batch, getX(), getY(), getWidth()-openAmount, getHeight());
				}
				else {	
					ship.getDoorImg().draw(batch, getX()+openAmount, getY(), getWidth()-openAmount, getHeight());
				
				}
			}else if (orientation == Neighbors.RIGHT || orientation == Neighbors.LEFT){
			
				if(left){
					ship.getDoorImg().draw(batch, getX(), getY(), getWidth(), getHeight()-openAmount);
				}
				else {	
					ship.getDoorImg().draw(batch, getX(), getY()+openAmount, getWidth(), getHeight()-openAmount);
				
				}
			}

		}
		
	
	}

}
