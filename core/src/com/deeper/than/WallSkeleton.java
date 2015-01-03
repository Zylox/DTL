package com.deeper.than;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class WallSkeleton extends Group{

	public static final int exteriorWall = 0;
	public static final int interiorWall = 1;
	public static final int doorWall = 2;
	public static final int noWall = 3;
	
	private static  NinePatch exteriorWallImg;
	private static NinePatch interiorWallImg;
	
	//private WallSkeletonComponent[]	walls;
	
	
	public WallSkeleton(float x, float y, int wallType1, int wallType2){
		setX(x);
		setY(y);
		WallSkeletonComponent[]	walls;
		walls = new WallSkeletonComponent[2];
		walls[0] = new WallSkeletonComponent(wallType1, Neighbors.LEFT);
		walls[1] = new WallSkeletonComponent(wallType2, Neighbors.DOWN);
		addActor(walls[0]);
		addActor(walls[1]);
		
		setDebug(DTL.DEBUG);
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
	
	private class WallSkeletonComponent extends Actor{
		
		private int wallType;
		private int orientation;
		
		public WallSkeletonComponent(int wallType, int orientation){
			this.wallType = wallType;
			this.orientation = orientation;
			setDebug(DTL.DEBUG);
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha){
			float height = Door.DOORSIZESHORT/3;
			
			switch(wallType){
			case exteriorWall :
				switch(orientation){
				case Neighbors.DOWN :
					WallSkeleton.getExteriorWallImg().draw(batch, getX(), getY()-height/2, FloorTile.TILESIZE, height);
					break;
				case Neighbors.LEFT :
					WallSkeleton.getExteriorWallImg().draw(batch, getX(), getY()-height/2, height, FloorTile.TILESIZE);
					break;
				}
				break;
			case interiorWall :
				switch(orientation){
				case Neighbors.DOWN :
					WallSkeleton.getExteriorWallImg().draw(batch, getX(), getY(), FloorTile.TILESIZE, height/1.5f);
					break;
				case Neighbors.LEFT :
					WallSkeleton.getExteriorWallImg().draw(batch, getX(), getY(), height/1.5f, FloorTile.TILESIZE);
					break;
				}
				break;
			}
		}
		
	}

}
