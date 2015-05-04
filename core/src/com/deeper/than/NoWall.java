/**
 * Placeholder object in  the event of no wall being on a side of the tile
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than;

/**
 * A cellborder that indicates no wall existing at an orientation
 * @author zach
 *
 */
public class NoWall extends CellBorder{

	public NoWall(float x, float y, int orientation, Ship ship) {
		super(x, y, orientation, ship);
	}

	@Override
	public void init(){
		
	}
	
	@Override
	public void reinit(){
		
	}
}
