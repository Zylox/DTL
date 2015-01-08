package com.deeper.than;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class CellBorder extends Group {
	
	public static final int IDSENTINEL  = -1;
	
	private int id;
	protected Ship ship;
	protected GridSquare gs;
	protected Vector2 pos;
	protected int orientation;
	
	
	public CellBorder(float x, float y, int orientation, Ship ship){
		this(new Vector2(x,y), orientation, ship);
	}
	
	public CellBorder(Vector2 pos, int orientation, Ship ship){
		this.pos = pos;
		this.orientation = orientation;
		this.ship = ship;
		id = IDSENTINEL;
	}	
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof CellBorder)){
			return false;
		}
		
		if(((CellBorder)o).getId() == this.getId()){
			if(this.getId() != IDSENTINEL){
				return true;
			}
		}
		return false;
	}
	
	public abstract void init();
	public abstract void reinit();

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public GridSquare getGridSquare() {
		return gs;
	}

	public void setGridSquare(GridSquare gs){
		this.gs = gs;
	}
	
	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	
	public void setPos(float x, float y) {
		this.pos = new Vector2(x,y);
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public void flipOrientation(){
		this.orientation = Neighbors.reverseOrienation(orientation);
	}
	
	/**
	 * Flips the wall over to its neighbor. Not safe for the edge of the ship grid, needs to be checked before calling.
	 */
	public CellBorder getFullFlip(){
		CellBorder tmp = this;
		switch(tmp.orientation){
		case Neighbors.UP :
			setPos(tmp.getPos().x, tmp.getPos().y+1);
			break;
		case Neighbors.RIGHT :
			setPos(tmp.getPos().x+1, tmp.getPos().y);
			break;
		case Neighbors.DOWN :
			setPos(tmp.getPos().x, tmp.getPos().y-1);
			break;
		case Neighbors.LEFT :
			setPos(tmp.getPos().x-1, tmp.getPos().y);
			break;
		default:
			DTL.printDebug("Orientation messud up for Cellborder fullflip");
			break;
		}
		tmp.flipOrientation();
		tmp.reinit();
		
		return tmp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
