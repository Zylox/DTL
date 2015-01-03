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
	

	protected String name;
	private ArrayList<Crew> crew;
	protected GridSquare[][] layout;
	private ArrayList<Door>	doors;
	protected int gridWidth;
	protected int gridHeight;
	private Sprite floorTileImg;
	private String floorTileImgHandle;
	private NinePatch doorImg;
	private TextureAtlas texAtl;
	private Sprite doorImgRot;
	private String doorImgHandle;
	
	
	public Ship(){
		
	}
	
	public Ship(FileHandle filepath){
		
		crew = new ArrayList<Crew>();
		doors = new ArrayList<Door>();
		
		
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
		
		for(Door d : doors){		
			layout[(int)d.pos.y][(int)d.pos.x].addDoor(d);
			addActor(d);
		}
		
		int x = FloorTile.TILESIZE*layout[0].length;
		int y = FloorTile.TILESIZE*layout.length;
		
		setBounds(Gdx.graphics.getWidth()/2 - x/2,Gdx.graphics.getHeight()/2 - y/2, x, y);
		
		loadAssets();
	}
	
	private void loadAssets(){
		floorTileImg = new Sprite(new Texture(Gdx.files.internal(floorTileImgHandle)));
		texAtl       = new TextureAtlas("door.pack");
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
	
	public Sprite getDoorImgRot() {
		return doorImgRot;
	}

	public void setFloorTileImgHandle(String handle){
		floorTileImgHandle = handle;
	}
	
	public void setDoorImgHandle(String handle){
		doorImgHandle = handle;
	}
}
