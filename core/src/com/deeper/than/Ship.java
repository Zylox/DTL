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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.deeper.than.Wall.WallType;
import com.deeper.than.crew.Crew;
import com.deeper.than.modules.ClimateControlModule;
import com.deeper.than.modules.HatchControlModule;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.SensorsModule;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

/**
 * A ship in the game. Loads a ship from a shipScript. Contains all the logic to manage the ship.
 * @author zach
 *
 */
public class Ship extends Group{
	/**
	 * A reference to the game for reasons. Don't ask the reasons.
	 */
	private DTL game;
	private int id;
	/**Name from the script. Not a unique identifier*/
	protected String name;
	/**A list of crew owned by this ship.*/
	private ArrayList<Crew> crew;
	/**The actual floor layout of the ship.*/
	protected GridSquare[][] layout;
	private ArrayList<Room> rooms;
	private ArrayList<Door>	doors;
	/**Map of all the walls. It is a map to ensure uniqueness.*/
	private OrderedMap<Integer, CellBorder> walls;
	private ArrayList<Module> modules;
	
	/** Width in squares of the ship*/
	protected int gridWidth;
	/** Height in squares of the ship*/
	protected int gridHeight;
	private Sprite floorTileImg;
	private String floorTileImgHandle;
	private NinePatch doorImg;
	private TextureAtlas texAtl;
	private String doorImgHandle;
	private int wallIdCounter;
	private int highestModuleId = -1;
	private boolean colorizeRooms = false;
	private float health;
	private boolean isPlayerShip;
	
	/**
	 * Creates a ship based on the shipScript added in.
	 * @param filepath Internal path to the script. ex. "kes.ship".
	 * @param game Reference to the game object
	 */
	public Ship(FileHandle filepath, boolean isPlayerShip, DTL game, int id){
		
		this.game = game;
		crew = new ArrayList<Crew>();
		doors = new ArrayList<Door>();
		rooms = new ArrayList<Room>();
		walls = new OrderedMap<Integer, CellBorder>();
		modules = new ArrayList<Module>();
		this.isPlayerShip = isPlayerShip;
		
		try {
			//Pulls a parser from the parser pool and loads the script
			ScriptParser parser = ScriptParser.parserPool.obtain();
			parser.loadShipScript(this, filepath);
			ScriptParser.parserPool.free(parser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		init();
	}
	
	public Ship(String script, DTL game, int id){
		
		this.game = game;
		crew = new ArrayList<Crew>();
		doors = new ArrayList<Door>();
		rooms = new ArrayList<Room>();
		walls = new OrderedMap<Integer, CellBorder>();
		modules = new ArrayList<Module>();
		
		try {
			//Pulls a parser from the parser pool and loads the script
			ScriptParser parser = ScriptParser.parserPool.obtain();
			parser.loadShipScript(this, script);
			ScriptParser.parserPool.free(parser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		init();
	}
	
	public void reInit(FileHandle filepath){
		clearCollections();
		try {
			//Pulls a parser from the parser pool and loads the script
			ScriptParser parser = ScriptParser.parserPool.obtain();
			parser.loadShipScript(this, filepath);
			ScriptParser.parserPool.free(parser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}
	
	public void reInit(String script){
		clearCollections();
		try {
			//Pulls a parser from the parser pool and loads the script
			ScriptParser parser = ScriptParser.parserPool.obtain();
			parser.loadShipScript(this, script);
			ScriptParser.parserPool.free(parser);
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}
	
	private void clearCollections(){
		SnapshotArray<Actor> actors = this.getChildren();
		for(Actor a : actors){
			this.removeActor(a);
		}
		crew.clear();
		doors.clear();
		rooms.clear();
		walls.clear();
		modules.clear();		
	}
	
	private void init(){
	////Actors are added to the group in layers essentially:
			//The floor being first, 
			//Followed by the walls,
			//Followed by modules,
			//Followed by crew when they get implemented,
			//Followed by doors.
			
			//Adds the Gridsquares, which hold the floor primarily 
			//and references to the walls, to the group
			wallIdCounter = 0;
			for(GridSquare[] g : layout){
				for(GridSquare gs : g){
					if(gs != null){
						addActor(gs);
					}
				}
			}
			
			//Determines walls based on neighbors.
			//Doors do not factor in atm.
			determineWalls();
			for(CellBorder cb : walls.values()){
				addActor(cb);
				cb.init();
			}
			HatchControlModule hcm = null;
			SensorsModule sensors = null;
			
			//Some modules require explicit reference, so we get them here.
			//This implementation assumes only one of each module is in the list.
			//Uniqueness isnt strictly implemented anywhere at this point.
			for(Module m : modules){
				if(m instanceof HatchControlModule){
					hcm = (HatchControlModule) m;
				}else if(m instanceof SensorsModule){
					sensors = (SensorsModule) m;
				}
			}
			
			//Rooms have the sensor module set to determine visibility later.
			for(Room r : rooms){
				r.setSensorsModule(sensors);
			}
			
			//The door adding logic.
			for(Door d : doors){		
				GridSquare curGrid = layout[(int)d.pos.y][(int)d.pos.x];
				
				wallIdCounter++;
				
				//Initialize needed door values
				//hatch control reference set to see if doors will open.
				d.setHatchControlModule(hcm);
				d.setId(wallIdCounter);
				d.init();
				//remove the wall to put the door in
				CellBorder removedWall = curGrid.getBorder(d.orientation);
				if(removedWall instanceof Wall){
					//the new door will be of the same walltype as the one being removed (interior or exterior)
					d.setWallType(((Wall) removedWall).getWallType());
				}
				removeActor(removedWall);
				walls.remove(removedWall.getId());
				curGrid.addBorder(d);
				//Reflects the door to be added to the neighbor of curgrid that shares the door.
				resolveDoorReflections(curGrid, d);
				addActor(d);
			}	
				
			for(Crew c : crew){
				addActor(c);
			}
			
			int x = FloorTile.TILESIZE*layout[0].length;
			int y = FloorTile.TILESIZE*layout.length;
			
			if(isPlayerShip){
				setBounds(game.getViewport().getWorldWidth()/3 - x/2,game.getViewport().getWorldHeight()/2 - y/2, x, y);
			}
			
			health = 100;
			
			loadAssets();
	}
	
	//public
	
	/**
	 * Takes in a gridSquare and a door taht is on it, then reflects it and adds it to the coorsponding neighbor.
	 * @param curGrid Square with the grid already set
	 * @param d door being set
	 */
	private void resolveDoorReflections(GridSquare curGrid, Door d){
		GridSquare neighGrid  = null;
		Room neighRoom = null;
		//Check which orientation to figure out which neighbor to use
		//add a link between the rooms
		//add the reflected door to the neighbor
		//if the neighbor is outside the ship, link to null
		//(being linked to null represents outside).
		
		if(d.orientation == Neighbors.RIGHT){
			if(d.pos.x+1 < gridWidth){
				neighGrid = layout[(int)d.pos.y][(int)d.pos.x+1];
				if(neighGrid == null){
					neighRoom = null;
				}else{
					neighRoom = neighGrid.getRoom();
				}
				linkRooms(curGrid.getRoom(), neighRoom, d);
				if(neighGrid != null){
					neighGrid.setReflectedDoor(d);
				}
			}else{
				linkRooms(curGrid.getRoom(), null, d);
			}
		}else if(d.orientation == Neighbors.LEFT){
			if(d.pos.x >= 1){
				neighGrid = layout[(int)d.pos.y][(int)d.pos.x-1];
				if(neighGrid == null){
					neighRoom = null;
				}else{
					neighRoom = neighGrid.getRoom();
				}
				linkRooms(curGrid.getRoom(), neighRoom, d);
				if(neighGrid != null){
					neighGrid.setReflectedDoor(d);
				}
			}else{
				linkRooms(curGrid.getRoom(), null, d);
			}
		}else if(d.orientation == Neighbors.UP){
			if(d.pos.y+1 < gridHeight){
				neighGrid = layout[(int)d.pos.y+1][(int)d.pos.x];
				if(neighGrid == null){
					neighRoom = null;
				}else{
					neighRoom = neighGrid.getRoom();
				}
				linkRooms(curGrid.getRoom(), neighRoom, d);
				if(neighGrid != null){
					neighGrid.setReflectedDoor(d);
				}
			}else{
				linkRooms(curGrid.getRoom(), null, d);
			}
		}else if(d.orientation == Neighbors.DOWN){
			if(d.pos.y >= 1){
				neighGrid = layout[(int)d.pos.y-1][(int)d.pos.x];
				if(neighGrid == null){
					neighRoom = null;
				}else{
					neighRoom = neighGrid.getRoom();
				}
				linkRooms(curGrid.getRoom(), neighRoom, d);
				if(neighGrid != null){
					neighGrid.setReflectedDoor(d);
				}
			}else{
				linkRooms(curGrid.getRoom(), null, d);
			}
		}
	}
	
	/**
	 * Contains any non Action based update logic.
	 */
	public void update(){
		for(Room r : rooms){
			//first calculate where the water will flow at current state
			r.calculateEnv();
		}
		for(Room r : rooms){
			//set it to that state
			r.updateEnv();
		}
		
		for(Crew c : crew){
			c.update();
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		//draw the other stuff the ship is supposed to draw, then the modules
		if(isDrawable()){
			super.draw(batch, parentAlpha);
			
			for(Module m : modules){
				m.draw(batch);
			}
			for(Room r : rooms){
				if(r.isHoveredOver() && ((GameplayScreen)Screens.GAMEPLAY.getScreen()).isCrewSelected()){
					r.drawHover(batch);
				}
			}
			

		}
	}
	
	/**
	 * Determines the walls based on gridsquares neighbors.
	 * Adds all generated walls to the gridsquares reference as well as the wall map.
	 */
	private void determineWalls(){
		GridSquare current = null;
		GridSquare left = null;
		GridSquare right = null;
		GridSquare up = null;
		GridSquare down = null;
		
		//For each gridsquare of the ship
		for(int x = 0; x<layout[0].length;x++){
			for(int y = 0; y<layout.length; y++){
				current  = layout[y][x];
				//if the square is part of the ship
				if(current != null){
					//Get neighbors if they exits, null if they dont
					if(x>0){
						left = layout[y][x-1];
					}else{
						left = null;
					}
					if(x<layout[0].length-1){
						right= layout[y][x+1];
					}else{
						right= null;
					}
					if(y<layout.length-1){
						up   = layout[y+1][x];
					}else{
						up   = null;
					}
					if(y>0){
						down = layout[y-1][x];
					}else{
						down = null;
					}
					
					//Determine walls in each direction
					detWall(current, left, Neighbors.LEFT);
  					detWall(current, right, Neighbors.RIGHT);
  					detWall(current, up, Neighbors.UP);
  					detWall(current, down, Neighbors.DOWN);
				}
			}
		}
	}
	
	
	/**
	 * Creates a link between the two rooms.
	 * @param room1 
	 * @param room2
	 * @param door Door connecting room1 and room2
	 */
	private void linkRooms(Room room1, Room room2, Door door){
		room1.addLink(room2, door);
		if(room2 != null){
			room2.addLink(room1, door);
		}
	}
	
	/**
	 * Adds a cellborder to the wall map, but only if it is unique.
	 * @param bord
	 */
	private void addUniqueWall(CellBorder bord){
		if(!walls.containsKey(bord.getId())){
			walls.put(bord.getId(), bord);
		}
	}
	
	private void detWall(GridSquare current, GridSquare adj, int orientation){
		CellBorder bord = null;
		
		if(adj != null){
			//if the adjacent square exists, check to see if it has set its wall already
			bord = adj.getBorder(Neighbors.reverseOrienation(orientation));
			if(bord != null){
				//if the adjacent square has, flip the wall for later
				bord.getFullFlip();
			}else{
				//if the adjacent sqaure has not set its wall, check if it is in the same room
				if(current.isSameRoom(adj)){
					//if it is, no wall is there
					bord =  new NoWall(current.getPos().x, current.getPos().y ,orientation, this);
				}else{
					//If it isnt, there must be an interior wall
					bord = new Wall(current.getPos().x, current.getPos().y, orientation, this, WallType.interior);
				}
			}
		}else{
			//If the adjacent square doesnt exits, it must be outside, so make an exterior wall
			bord =  new Wall(current.getPos().x, current.getPos().y, orientation, this, WallType.exterior);
		}
		
		//If the wall is new, the id needs to be initialized.
		if(bord.getId() == CellBorder.IDSENTINEL){
			wallIdCounter++;
			bord.setId(wallIdCounter);
		}
		//add the wall to the walls map and the gridsquares walls reference
		addUniqueWall(bord);
		current.addBorder(bord);
	}
	
	/**
	 * Loads visual and audio assets needed for the ship.
	 */
	private void loadAssets(){
		if(texAtl != null && texAtl.getRegions().size != 0){
			texAtl.dispose();
		}
		if(floorTileImgHandle != null){
			floorTileImg = new Sprite(new Texture(Gdx.files.internal(floorTileImgHandle)));
		}
		if(doorImgHandle != null){
			texAtl       = new TextureAtlas(doorImgHandle);
			doorImg      = texAtl.createPatch("doorhalf");
		}
	}
	
	public GridSquare[][] getLayoutCopy(){
		GridSquare[][] copy = new GridSquare[layout.length][];
		
		for(int i = 0; i>layout.length; i++){
			GridSquare[] copyRow = layout[i];
			copy[i] = new GridSquare[copyRow.length];
			System.arraycopy(copyRow, 0, copy[i], 0, copyRow.length);
		}
		
		return copy;
	}
	
	public void dispose(){
		texAtl.dispose();
	}
		
	public String getShipName() {
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
	
	public void addCrewToList(Crew crewMem){
		crew.add(crewMem);
	}
	
	public void addCrewAfterShipLoad(Crew crewMem){
		crew.add(crewMem);
		addActor(crewMem);
	}
	
	/**
	 * Finds out if a crew member is in specified room
	 * @param room Room to search
	 * @return true if a crew member is in the room
	 */
	public boolean isCrewInRoom(Room room){
		for(Crew c : crew){
			if(c.getRoom() == room){
				return true;
			}
		}
		return false;
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
	
	public void addModule(Module module){
		highestModuleId = Math.max(module.getId(), highestModuleId);
		this.modules.add(module);
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
	
	public void setLayoutTile(int x, int y, GridSquare square){
		this.layout[y][x] = square;
	}
	
	public int getXdim(){
		return layout[0].length;
	}
	
	public int getYdim(){
		return layout.length;
	}
	
	public boolean isDrawable(){
		return !(floorTileImg == null || doorImg == null);
	}
	
	public boolean colorizeRooms(){
		return colorizeRooms;
	}
	
	public void setColorizeRooms(boolean colorize){
		this.colorizeRooms = colorize;
	}
	
	public int getHighestModuleID(){
		return highestModuleId;
	}
	
	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}
	
	public int getSheildSections(){
		//TODO
		return 5;
	}

	public boolean isPlayerShip() {
		return isPlayerShip;
	}

	public void setPlayerShip(boolean isPlayerShip) {
		this.isPlayerShip = isPlayerShip;
	}

	public int getId(){
		return id;
	}
	
	/**
	 * Retrieves the drain rate from the climate control module.
	 * @return ship's water drain rate.
	 */
	public float getDrainRate(){
		float drainRate = 0;
		for(Module m : modules){
			if(m instanceof ClimateControlModule){
				drainRate = ((ClimateControlModule) m).getWaterDrainRate();
			}
		}
		return drainRate;
	}
	

}
