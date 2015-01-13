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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.deeper.than.Wall.WallType;

public class Ship extends Group{
	
	private DTL game;
	
	protected String name;
	private ArrayList<Crew> crew;
	protected GridSquare[][] layout;
	private ArrayList<Room> rooms;
	private ArrayList<Door>	doors;
	private OrderedMap<Integer, CellBorder> walls;
	protected int gridWidth;
	protected int gridHeight;
	private Sprite floorTileImg;
	private String floorTileImgHandle;
	private NinePatch doorImg;
	private TextureAtlas texAtl;
	private String doorImgHandle;
	private int wallIdCounter;
	
	
	private float drainRate = -.01f;
	
	public Ship(){
		
	}
	
	public Ship(FileHandle filepath, DTL game){
		
		this.game = game;
		crew = new ArrayList<Crew>();
		doors = new ArrayList<Door>();
		rooms = new ArrayList<Room>();
		walls = new OrderedMap<Integer, CellBorder>();
		
		
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
		
		
//		
		determineWalls();
		for(CellBorder cb : walls.values()){
			addActor(cb);
			cb.init();
		}
//		
		for(Door d : doors){		
			GridSquare curGrid = layout[(int)d.pos.y][(int)d.pos.x];
			GridSquare neighGrid  = null;
			wallIdCounter++;
			d.setId(wallIdCounter);
			d.init();
			CellBorder removedWall = curGrid.getBorder(d.orientation);
			if(removedWall instanceof Wall){
				d.setWallType(((Wall) removedWall).getWallType());
			}
			removeActor(removedWall);
			walls.remove(removedWall.getId());
			curGrid.addBorder(d);
			
			
			if(d.orientation == Neighbors.RIGHT){
				if(d.pos.x+1 < gridWidth){
					neighGrid = layout[(int)d.pos.y][(int)d.pos.x+1];
					linkRooms(curGrid.getRoom(), neighGrid.getRoom(), d);
					if(neighGrid != null){
						d.getFullFlip();		
						neighGrid.addBorder(d);
						d.getFullFlip();
					}
				}else{
					linkRooms(curGrid.getRoom(), null, d);
				}
			}else if(d.orientation == Neighbors.LEFT){
				if(d.pos.x > 1){
					d = (Door) d.getFullFlip();
					neighGrid = layout[(int)d.pos.y][(int)d.pos.x-1];
					linkRooms(curGrid.getRoom(), neighGrid.getRoom(), d);
					if(neighGrid != null){
						d.getFullFlip();		
						neighGrid.addBorder(d);
						d.getFullFlip();
					}
				}else{
					linkRooms(curGrid.getRoom(), null, d);
				}
			}else if(d.orientation == Neighbors.UP){
				if(d.pos.y+1 < gridHeight){
					neighGrid = layout[(int)d.pos.y+1][(int)d.pos.x];
					linkRooms(curGrid.getRoom(), neighGrid.getRoom(), d);
					if(neighGrid != null){
						d.getFullFlip();		
						neighGrid.addBorder(d);
						d.getFullFlip();
					}
				}else{
					linkRooms(curGrid.getRoom(), null, d);
				}
			}else if(d.orientation == Neighbors.DOWN){
				if(d.pos.y > 1){
					neighGrid = layout[(int)d.pos.y-1][(int)d.pos.x];
					linkRooms(curGrid.getRoom(), neighGrid.getRoom(), d);
					if(neighGrid != null){
						d.getFullFlip();		
						neighGrid.addBorder(d);
						d.getFullFlip();
					}
				}else{
					linkRooms(curGrid.getRoom(), null, d);
				}
			}
			addActor(d);
			
		}
		
		int x = FloorTile.TILESIZE*layout[0].length;
		int y = FloorTile.TILESIZE*layout.length;
		
		setBounds(game.getViewport().getWorldWidth()/2 - x/2,game.getViewport().getWorldHeight()/2 - y/2, x, y);
		
		loadAssets();
	}
	
	public void update(){
//		Collections.sort(rooms, new Comparator<Room>() {
//			@Override
//			public int compare(Room  room1, Room  room2)
//			{
//				
//				return room1.getWaterLevel() < room2.getWaterLevel() ? -1 : room1.getWaterLevel() == room2.getWaterLevel() ? 0 : 1;
//			}
//		});
		
		Array<Actor> actors = getChildren();
		
		for(Actor a : actors){
			
		}
		
		for(Room r : rooms){
			r.calculateEnv();
		}
		for(Room r : rooms){
			r.updateEnv();
		}
	}
	
	public void determineWalls(){

		GridSquare current = null;
		GridSquare left = null;
		GridSquare right = null;
		GridSquare up = null;
		GridSquare down = null;
		

		for(int x = 0; x<layout[0].length;x++){
			for(int y = 0; y<layout.length; y++){
				current  = layout[y][x];
				if(current != null){
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

					CellBorder bord = null;
					
					
					bord = detWall(current, left, Neighbors.LEFT);
					if(bord.getId() == CellBorder.IDSENTINEL){
						wallIdCounter++;
						bord.setId(wallIdCounter);
					}
//					bord.setGridSquare(layout[(int)bord.getPos().y][(int)bord.getPos().x]);
  					addUniqueWall(bord);
  					current.addBorder(bord);
					
  					bord = detWall(current, right, Neighbors.RIGHT);
					if(bord.getId() == CellBorder.IDSENTINEL){
						wallIdCounter++;
						bord.setId(wallIdCounter);
					}
//					bord.setGridSquare(layout[(int)bord.getPos().y][(int)bord.getPos().x]);
					addUniqueWall(bord);
  					current.addBorder(bord);
  
  					bord = detWall(current, up, Neighbors.UP);
					if(bord.getId() == CellBorder.IDSENTINEL){
						wallIdCounter++;
						bord.setId(wallIdCounter);
					}
					bord.setGridSquare(layout[(int)bord.getPos().y][(int)bord.getPos().x]);
					addUniqueWall(bord);
  					current.addBorder(bord);
					
  					bord = detWall(current, down, Neighbors.DOWN);
					if(bord.getId() == CellBorder.IDSENTINEL){
						wallIdCounter++;
						bord.setId(wallIdCounter);
					}
					//bord.setGridSquare(layout[(int)bord.getPos().y][(int)bord.getPos().x]);
					addUniqueWall(bord);
					current.addBorder(bord);
					
				}
			}
		}
		

	}
	
	private void linkRooms(Room room1, Room room2, Door door){
		room1.addLink(room2, door);
		if(room2 != null){
			room2.addLink(room1, door);
		}
	}
	
	private void addUniqueWall(CellBorder bord){
		if(!walls.containsKey(bord.getId())){
			walls.put(bord.getId(), bord);
		}
	}
	
	private CellBorder detWall(GridSquare current, GridSquare adj, int orientation){
//
		if(adj != null){
			CellBorder bord = adj.getBorder(Neighbors.reverseOrienation(orientation));
			if(bord != null){

				bord.getFullFlip();
				return bord;
			}
		}
		
		Door door = doorBetweenNeighTile(current, adj, orientation);
		
		if(door != null){
			if(adj != null){
				System.out.println("Door Between: " + current.getPos().toString() + " and " + adj.getPos().toString());
			}else{
				System.out.println("Door Between: " + current.getPos().toString() + " and " + door.orientation);
			}
			return door;
		}
		
		if(adj == null){
			return new Wall(current.getPos().x, current.getPos().y, orientation, this, WallType.exterior);
		}
		
		
		if(current.isSameRoom(adj)){
			return new NoWall(current.getPos().x, current.getPos().y ,orientation, this);
		}
		
		
		
		return new Wall(current.getPos().x, current.getPos().y, orientation, this, WallType.interior);
	}
	

	
	public Door doorBetweenNeighTile(GridSquare base, GridSquare ref, int orientation){
		
		if(base == null){
			return null;
		}
		
		Door baseDoor = base.getDoor(orientation);
		if(baseDoor != null){
			return baseDoor;
		}
		
		if(ref != null){
			Door refDoor  = ref.getDoor(Neighbors.reverseOrienation(orientation));
			//Door refDoor  = ref.getDoor(orientation);
			if(refDoor != null){
				refDoor.flipOrientation();
				refDoor.setPos(base.getPos());
				refDoor.init();
				return refDoor;
			}
		}
		
		
		return null;
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
	
	public float getDrainRate(){
		return drainRate;
	}
	
	

}
