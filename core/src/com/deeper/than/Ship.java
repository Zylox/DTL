package com.deeper.than;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Ship extends Group{
	
	private DTL game;
	
	protected String name;
	private ArrayList<Crew> crew;
	protected GridSquare[][] layout;
	private WallSkeleton[][] walls;
	private ArrayList<Room> rooms;
	private ArrayList<Door>	doors;
	protected int gridWidth;
	protected int gridHeight;
	private Sprite floorTileImg;
	private String floorTileImgHandle;
	private NinePatch doorImg;
	private TextureAtlas texAtl;
	private String doorImgHandle;
	
	public Ship(){
		
	}
	
	public Ship(FileHandle filepath, DTL game){
		
		this.game = game;
		crew = new ArrayList<Crew>();
		doors = new ArrayList<Door>();
		rooms = new ArrayList<Room>();
		
		
		try {
			ScriptParser parser = ScriptParser.parserPool.obtain();
			parser.loadShipScript(this, filepath);
			ScriptParser.parserPool.free(parser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(GridSquare[] g : layout){
			for(GridSquare gs : g){
				if(gs != null){
					addActor(gs);
				}
			}
		}
		
		determineWalls();
		
		for(Door d : doors){		
			layout[(int)d.pos.y][(int)d.pos.x].addDoor(d);
			addActor(d);
		}
		
		int x = FloorTile.TILESIZE*layout[0].length;
		int y = FloorTile.TILESIZE*layout.length;
		
		setBounds(game.getViewport().getWorldWidth()/2 - x/2,game.getViewport().getWorldHeight()/2 - y/2, x, y);
		
		loadAssets();
	}
	
	public void determineWalls(){
		walls = new WallSkeleton[layout.length][layout[0].length];
		int wallTypeLeft = -1;
		int wallTypeDown = -1;
		GridSquare current = null;
		GridSquare left = null;
		GridSquare down = null;
		for(int x = 0; x < walls[0].length; x++){
			for(int y = 0; y < walls.length; y++){
				//y x
				current = layout[y][x];
				if(x==0){
					left = null;
				}else{
					left = layout[y][x-1];
				}
				if(y==0){
					down = null;
				}else{
					down = layout[y-1][x];
				}
				
				wallTypeLeft = detWallType(current, left);
				wallTypeDown = detWallType(current, down);
				
				walls[y][x] = new WallSkeleton(x*FloorTile.TILESIZE,y*FloorTile.TILESIZE, wallTypeLeft, wallTypeDown);
				addActor(walls[y][x]);
			}
		}
	}
	
	private int detWallType(GridSquare base, GridSquare ref){
		int wallType = -1;
		
		if(base != null){
			if(ref != null){
				if(base.isSameRoom(ref)){
					wallType= WallSkeleton.noWall;
				}else{
					if(isDoorBetweenNeighTile(base, ref)){
						wallType = WallSkeleton.doorWall;
					}else{
						wallType = WallSkeleton.interiorWall;
					}
				}
			}else{
				wallType = WallSkeleton.exteriorWall;
			}
		}
		
		return wallType;
	}
	
	public boolean isDoorBetweenNeighTile(GridSquare base, GridSquare ref){
		
		if(base == null || ref == null){
			return false;
		}
		
		if(base.getPos().x+1 == ref.getPos().x){
			//if base left
			if(base.hasDoor(Neighbors.RIGHT) || ref.hasDoor(Neighbors.LEFT)){
				return true;
			}
		}else if(base.getPos().x-1 == ref.getPos().x){
			//if base right
			if(base.hasDoor(Neighbors.LEFT) || ref.hasDoor(Neighbors.RIGHT)){
				return true;
			}
		}else if(base.getPos().x == ref.getPos().y){
			//if same column
			if(base.getPos().y-1 == ref.getPos().y){
				//if base up
				if(base.hasDoor(Neighbors.DOWN) || ref.hasDoor(Neighbors.UP)){
					return true;
				}
			}else if(base.getPos().y+1 == ref.getPos().y){
				//if base down
				if(base.hasDoor(Neighbors.UP) || ref.hasDoor(Neighbors.DOWN)){
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	private void loadAssets(){
		floorTileImg = new Sprite(new Texture(Gdx.files.internal(floorTileImgHandle)));
		texAtl       = new TextureAtlas(doorImgHandle);
		doorImg      = texAtl.createPatch("doorhalf");
	}
	
	@Override
	public void drawChildren(Batch batch, float parentAlpha){
		super.drawChildren(batch, parentAlpha);
	}
		
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Crew> getCrew() {
		return crew;
	}
	
	public void setCrew(ArrayList<Crew> crew) {
		this.crew = crew;
	}
	
	public GridSquare[][] getLayout() {
		return layout;
	}
	
	public void setLayout(GridSquare[][] layout) {
		this.layout = layout;
	}
	
	public ArrayList<Door> getDoors() {
		return doors;
	}
	
	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}
	
	public void addDoor(Door door){
		this.doors.add(door);
	}
	
	public void addRoom(Room room){
		this.rooms.add(room);
	}
	
	public int getGridWidth() {
		return gridWidth;
	}

	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

	public Sprite getFloorTileImg() {
		return floorTileImg;
	}

	public void setFloorTileImg(Sprite floorTileImg) {
		this.floorTileImg = floorTileImg;
	}

	public NinePatch getDoorImg() {
		return doorImg;
	}

	public void setFloorTileImgHandle(String handle){
		floorTileImgHandle = handle;
	}
	
	public void setDoorImgHandle(String handle){
		doorImgHandle = handle;
	}
	
	public WallSkeleton[][] getWalls() {
		return walls;
	}

	public void setWalls(WallSkeleton[][] walls) {
		this.walls = walls;
	}

}
