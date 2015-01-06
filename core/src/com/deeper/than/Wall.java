package com.deeper.than;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Wall extends CellBorder{

	public enum WallType{
		interior,
		exterior;
	}
	
	private static  NinePatch exteriorWallImg;
	private static NinePatch interiorWallImg;
	
	public static final float INTERIORWALLSHORTSIDE = Door.DOORSIZESHORT/2;
	public static final float EXTERIORWALLSHORTSIDE = Door.DOORSIZESHORT/4;
	
	private WallType wallType;

	public Wall(float x, float y, int orientation, Ship ship) {
		super(x, y, orientation, ship);
		this.wallType = null;
	}
	
	public Wall(float x, float y, int orientation, Ship ship, WallType wallType) {
		super(x, y, orientation, ship);
		this.wallType = wallType;
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub

		setPositionForDraw();
		addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println(getPos().toString() + " " + getX() + " " + getY());
				return true;
		    }
		});
	}
	
	public void setPositionForDraw(){
		Vector2 bLeft = new Vector2(getPos().x*FloorTile.TILESIZE, getPos().y*FloorTile.TILESIZE);
		
		float shortSide = 0;
		float longSide = FloorTile.TILESIZE;
		if(wallType == WallType.interior){
			shortSide = INTERIORWALLSHORTSIDE;
			if(orientation == Neighbors.DOWN){
				setBounds(bLeft.x, bLeft.y-shortSide/2, longSide, shortSide);
			}
			else if(orientation == Neighbors.UP){
				setBounds(bLeft.x, bLeft.y-shortSide/2+FloorTile.TILESIZE, longSide, shortSide);
			}
			else if(orientation == Neighbors.RIGHT){
				setBounds(bLeft.x+FloorTile.TILESIZE-shortSide/2, bLeft.y, shortSide, longSide);
			}
			else if(orientation == Neighbors.LEFT){
				setBounds(bLeft.x-shortSide/2, bLeft.y, shortSide, longSide);
			}
		}else{
			shortSide = EXTERIORWALLSHORTSIDE;
			if(orientation == Neighbors.DOWN){
				setBounds(bLeft.x, bLeft.y, longSide, shortSide);
			}
			else if(orientation == Neighbors.UP){
				setBounds(bLeft.x, bLeft.y-shortSide+FloorTile.TILESIZE, longSide, shortSide);
			}
			else if(orientation == Neighbors.RIGHT){
				setBounds(bLeft.x+FloorTile.TILESIZE-shortSide, bLeft.y, shortSide, longSide);
			}
			else if(orientation == Neighbors.LEFT){
				setBounds(bLeft.x, bLeft.y, shortSide, longSide);
			}
		}
		

		
		
	}
	
	public void setWallType(WallType wallType){
		this.wallType = wallType;
	}
	
	public WallType getWallType(){
		return wallType;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		getExteriorWallImg().draw(batch, getX(), getY(), getWidth(), getHeight());

		
		
	}
	
	
	
	public static void loadAssets(){
		TextureAtlas textAtl = new TextureAtlas("wallParts.pack");
		exteriorWallImg = textAtl.createPatch("wallPart");
		interiorWallImg = exteriorWallImg;
	}
	
	public static NinePatch getExteriorWallImg() {
		return exteriorWallImg;
	}

	public static void setExteriorWallImg(NinePatch _exteriorWallImg) {
		exteriorWallImg = _exteriorWallImg;
	}

	public static NinePatch getInteriorWallImg() {
		return interiorWallImg;
	}

	public static void setInteriorWallImg(NinePatch _interiorWallImg) {
		interiorWallImg = _interiorWallImg;
	}
	
}
