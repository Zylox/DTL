package com.deeper.than.shipbuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deeper.than.DTL;
import com.deeper.than.FloorTile;
import com.deeper.than.Neighbors;
import com.deeper.than.ScriptParser;
import com.deeper.than.Ship;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;

public class ShipPartContainer {
	
	private DTL game;
	
	private int roomId;
	private int doorId;
	
	private String name;
	private int xDim; 
	private int yDim;
	private String floorTileImgHandle;
	private Sprite floorTileImg;
	private String doorImgHandle;
	private ArrayList<RoomContainer> rooms;
	private ArrayList<DoorContainer> doors;
	private ShapeRenderer shapeRen;
	private Stage shipStage;
	
	private Ship ship;
	
	private boolean colorizeRooms;
	
	public ShipPartContainer(DTL game, Stage shipStage){
		this.game = game;
		xDim = 10;
		yDim = 10;
		roomId = 0;
		doorId = 0;
		floorTileImgHandle = "floortile.png";
		floorTileImg = null;
		doorImgHandle = "door.pack";
		name = "";
		rooms = new ArrayList<RoomContainer>();
		doors = new ArrayList<DoorContainer>();
		
		shapeRen = new ShapeRenderer();
		this.shipStage = shipStage;
		colorizeRooms = false;
	}
	
	public void loadNewShip(FileHandle filepath){
//		if(filepath.){
//			System.out.println("ship no show");
//			return;
//		}
		if(ship != null){
			ship.remove();
		}
		rooms.clear();
		doors.clear();
		ship.dispose();
		ship = new Ship(filepath, game, DTL.firstOpenId++);
		shipStage.addActor(ship);
		ship.setX(Gdx.graphics.getWidth()/2 - ship.getWidth()/2);
		ship.setY(Gdx.graphics.getHeight()/2 - ship.getHeight()/2);
		this.name = ship.getShipName();
		this.xDim = ship.getXdim();
		this.yDim = ship.getYdim();
		loadShipFromScript(filepath);
		setColorizedRooms(colorizeRooms);
		//name = ship.getShipName();
	}
	
	public void reLoadShip(){
//		ship.reInit(getShipString());
		
		if(ship != null){
			ship.remove();
		}
		ship = new Ship(getShipString(), game, ship.getId());
		shipStage.addActor(ship);
		ship.setColorizeRooms(colorizeRooms);
	}
	
	public void setColorizedRooms(boolean colorize){
		this.colorizeRooms = colorize;
		reLoadShip();
	}
	
	public void draw(Batch batch, Stage ui){
		drawGrid(ui, batch);
	}
	
	private void drawGrid(Stage ui, Batch batch){
		float initialX = ui.getWidth()/2 - (xDim*FloorTile.TILESIZE)/2;
		float initialY =  ui.getHeight()/2- (yDim*FloorTile.TILESIZE)/2;
		
		shapeRen.setAutoShapeType(true);
		shapeRen.begin();

		Vector2 point1 = new Vector2();
		point1.x = initialX;
		point1.y = initialY;
		Vector2 sides = new Vector2(FloorTile.TILESIZE, FloorTile.TILESIZE);
		
		for(int j = 0; j<yDim; j++){
			for(int i = 0; i< xDim; i++){
				shapeRen.rect(point1.x+i*sides.x,point1.y+j*sides.y,sides.x,sides.y);
			}
		}
		
		
		shapeRen.end();
	}
	
	public Sprite getFloorTileImg(){
		return floorTileImg;
	}
	
	public String writeToFile() throws IOException{
		if(name == ""){
			return "Please name the ship"; 
		}
		
		FileHandle loc = Gdx.files.local("../android/assets/ships/" + name+ ".ship");
		if(!loc.parent().exists()){
			loc = Gdx.files.local("/assets/ships/" + name+ ".ship");
		}

		if(!loc.parent().exists()){
			loc.file().getParentFile().mkdirs();
		}
		if(!loc.exists()){
			loc.file().createNewFile();
		}
		loc.writeString(getShipString(), false);
		return "success";
	}
	
	private String getShipString(){
		String str = "";
		str += "name= " + name + "\n";
		str += "xDim= " + xDim + "\n";
		str += "yDim= " + yDim + "\n";
		str += "floortileimg= " + floorTileImgHandle + "\n";
		str += "doorimg= " + doorImgHandle + "\n";
		str += "layout=" + "\n";
		for(RoomContainer r : rooms){
			str += r.getStringRep() + "\n";
		}
		str += "endlayout" + "\n";
		str += "doors=" + "\n";
		for(DoorContainer d : doors){
			str += d.getStringRep() + "\n";
		}
		
		str += "enddoors";
		return str;
	}
	
	public void createRoom(Vector2[] poss){
		if(poss.length == 0){
			return;
		}
		removeTiles(poss);
		
		RoomContainer room = new RoomContainer(roomId++);
		for(Vector2 pos : poss){
			room.addTile(pos);
		}
		rooms.add(room);
		reLoadShip();
	}
	
	public void createDoor(Vector2 pos, int dir){
		if(pos.x > xDim-1 || pos.y > yDim-1 || pos.x < 0 || pos.y < 0){
			return;
		}
		boolean inRoom = false;
		for(RoomContainer room : rooms){
			inRoom = inRoom || room.isInRoom(pos);
		}
		if(!inRoom){
			System.out.println("not in room");
			return;
		}
		Vector2 neighPos = new Vector2();
		neighPos.x = pos.x;
		neighPos.y = pos.y;
		if(dir == Neighbors.DOWN){
			neighPos.y -= 1;
		}else if(dir == Neighbors.UP){
			neighPos.y += 1;
		}else if(dir == Neighbors.LEFT){
			neighPos.x -= 1;
		}else if(dir == Neighbors.RIGHT){
			neighPos.x += 1;
		}
		if(neighPos.x < xDim || pos.y < yDim || pos.x > -1 || pos.y > -1){
			if(isSameRoom(pos, neighPos)){
				System.out.println("same room");
				return;
			}
		}
		for(DoorContainer door : doors){
			if(door.getPos().equals(pos) && door.getDirection() == dir){
				return;
			}else{
				neighPos.x = door.getPos().x;
				neighPos.y = door.getPos().y;
				if(dir == Neighbors.DOWN){
					neighPos.y += 1;
				}else if(dir == Neighbors.UP){
					neighPos.y -= 1;
				}else if(dir == Neighbors.LEFT){
					neighPos.x += 1;
				}else if(dir == Neighbors.RIGHT){
					neighPos.x -= 1;
				}
				if(pos.equals(neighPos) && dir == Neighbors.reverseOrienation(door.getDirection())){
					return;
				}
			}
		}
		DoorContainer door = new DoorContainer(doorId++);
		door.setPos(pos);
		door.setDirection(dir);
		
		doors.add(door);
		reLoadShip();
	}
	
	public boolean isSameRoom(Vector2 tile1, Vector2 tile2){
		for(RoomContainer room : rooms){
			if(room.isInRoom(tile1)){
				return room.isInRoom(tile2);
			}
		}
		
		return false;
	}
	
	public void moveEverything(int x, int y){
		for(RoomContainer room : rooms){
			room.move(x,y);
		}
		
		for(DoorContainer door : doors){
			door.pos.x += x;
			door.pos.y += y;
		}
		removeObjectsOutsideOfDimensions();
	}
	
	public void removeTiles(Vector2[] poss){
		if(poss.length == 0){
			return;
		}
		ArrayList<RoomContainer> roomsToRemove = new ArrayList<RoomContainer>();
		for(RoomContainer room : rooms){
			boolean obliterated = room.removeSubsetOfTiles(poss);
			if(obliterated){
				roomsToRemove.add(room);
			}
		}
		rooms.removeAll(roomsToRemove);
		removeDoors(poss);
		reLoadShip();
		
	}
	
	public void removeDoors(Vector2[] poss){
		ArrayList<DoorContainer> doorsToRemove = new ArrayList<DoorContainer>();
		for(Vector2 pos : poss){
			for(DoorContainer d : doors){
				if(d.getPos().equals(pos)){
					doorsToRemove.add(d);
				}
			}
		}
		doors.removeAll(doorsToRemove);
		reLoadShip();
	}
	
	public void removeDoor(Vector2 pos, int dir){
		Vector2 neighPos = new Vector2();
		for(DoorContainer d : doors){
			if(d.getPos().equals(pos) && d.getDirection() == dir){
				doors.remove(d);
				reLoadShip();
				return;
			}
			neighPos.x = pos.x;
			neighPos.y = pos.y;
			if(dir == Neighbors.DOWN){
				neighPos.y -= 1;
			}else if(dir == Neighbors.UP){
				neighPos.y += 1;
			}else if(dir == Neighbors.LEFT){
				neighPos.x -= 1;
			}else if(dir == Neighbors.RIGHT){
				neighPos.x += 1;
			}
			if(d.getPos().equals(neighPos) && d.getDirection() == Neighbors.reverseOrienation(dir)){
				doors.remove(d);
				reLoadShip();
				return;
			}
		}
	}
	
	public void addModuleToRoomAtPoint(Vector2 pos, String moduleStringRep, Stage ui){
		if(doesModuleAlreadyExist(moduleStringRep)){
			return;
		}
		pos = convertToSquareCoord(pos, ui);
		for(RoomContainer room : rooms){
			
			if(room.isInRoom(pos)){
				room.addModule(moduleStringRep);
				reLoadShip();
				return;
			}
		}
	}
	
	private boolean doesModuleAlreadyExist(String moduleStringRep){
		for(RoomContainer room : rooms){
			if(room.getModule().equals(moduleStringRep)){
				return true;
			}
		}
		return false;
	}
	
	public void removeModuleInRoomAtPoint(Vector2 pos, Stage ui){
		pos = convertToSquareCoord(pos, ui);
		for(RoomContainer room : rooms){
			
			if(room.isInRoom(pos)){
				room.addModule("");
				reLoadShip();
				return;
			}
		}
	}
	
	public Vector2[] intersectedSections(float x1, float y1, float x2, float y2, Stage ui){
		return intersectedSections(new Vector2(x1,y1), new Vector2(x2,y2), ui);
	}
	
	public Vector2[] intersectedSections(Vector2 pos1, Vector2 pos2, Stage ui){
		Vector2 lowerLeft = new Vector2(Math.min(pos1.x, pos2.x), Math.max(pos1.y, pos2.y));
		Vector2 upperRight = new Vector2(Math.max(pos1.x, pos2.x), Math.min(pos1.y, pos2.y));
		lowerLeft = convertToSquareCoord(lowerLeft, ui);
		upperRight = convertToSquareCoord(upperRight, ui);
		
		ArrayList<Vector2> squares = new ArrayList<Vector2>();
		for(int j = (int)Math.max(lowerLeft.y, 0); j<=Math.min(upperRight.y, yDim-1); j++){
			for(int i = (int)Math.max(lowerLeft.x, 0); i<= Math.min(upperRight.x,xDim-1); i++){
				squares.add(new Vector2(i,j));
			}
		}
		
		
		Vector2[] squaresArray = squares.toArray(new Vector2[squares.size()]);
		return squaresArray;
	}
	
	
	public Vector2 convertToSquareCoord(Vector2 pos, Stage ui){
		float initialX =   ui.getWidth()/2 - (xDim*FloorTile.TILESIZE)/2;// - ui.getViewport().getLeftGutterWidth();
		float initialY =  ui.getHeight()/2 - (yDim*FloorTile.TILESIZE)/2;// + ui.getViewport().getBottomGutterHeight();
		Vector2 initial = new Vector2(initialX, initialY);
		ui.getViewport().unproject(pos);
		pos.x = pos.x - initial.x;
		pos.y = pos.y - initial.y;
		pos.x = (float)Math.floor(pos.x/FloorTile.TILESIZE);
		pos.y = (float)Math.floor(pos.y/FloorTile.TILESIZE);
		return pos;
	}
	
	public void setXdim(int xDim){
		this.xDim = xDim;
		removeObjectsOutsideOfDimensions();
	}
	
	public int getXdim(){
		return xDim;
	}
	
	public void setYdim(int yDim){
		this.yDim = yDim;
		removeObjectsOutsideOfDimensions();
	}
	
	public int getYdim(){
		return yDim;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void removeObjectsOutsideOfDimensions(){
		ArrayList<RoomContainer> roomsToRemove = new ArrayList<RoomContainer>();
		for(RoomContainer r : rooms){
			if(r.isEntireRoomOutOfBounds(xDim, yDim)){
				roomsToRemove.add(r);
			}else{
				r.removeOutOfBounds(xDim, yDim);
			}
		}
		rooms.removeAll(roomsToRemove);
		
		ArrayList<DoorContainer> doorsToRemove = new ArrayList<DoorContainer>();
		for(DoorContainer d : doors){
			if(d.isOutOfBounds(xDim, yDim)){
				doorsToRemove.add(d);
			}
		}
		doors.removeAll(doorsToRemove);
		reLoadShip();
	}
	
	public boolean loadShipFromScript(FileHandle filepath){
		int roomId = 0;
		int doorId = 0;
		Scanner scanner = new Scanner(filepath.read());
		String line = getNextNonCommentLine(scanner);
		//if file is empty get out!
		if(line.equals(null)){
			scanner.close();
			return false;
		}
		while(true){
			if(line.startsWith("name= ")){
				this.name = line.substring(6);
			}else if(line.startsWith("xDim= ")){
				this.xDim = Integer.parseInt(line.substring(6));
			}else if(line.startsWith("yDim= ")){
				this.yDim = Integer.parseInt(line.substring(6));
			}else if(line.startsWith("floortileimg= ")){
				this.floorTileImgHandle = line.substring(14);	
			}else if(line.startsWith("doorimg= ")){
				this.doorImgHandle = line.substring(9);
			}else if(line.startsWith("layout=")){
				line = getNextNonCommentLine(scanner);
				RoomContainer room;
				while(!line.startsWith("endlayout")){
					room = new RoomContainer(roomId++);
					room.loadFromString(line);
					rooms.add(room);
					line = getNextNonCommentLine(scanner);
				}
			}else if(line.startsWith("doors=")){
				line = getNextNonCommentLine(scanner);
				DoorContainer door;
				while(!line.startsWith("enddoors")){
					door = new DoorContainer(doorId++);
					door.loadFromString(line);
					doors.add(door);
					line = getNextNonCommentLine(scanner);
				}
			}
			if(scanner.hasNext()){
				line = getNextNonCommentLine(scanner);
			}else{
				break;
			}
		}
		scanner.close();
		return true;
	}
	
	
	private String getNextNonCommentLine(Scanner scanner){
		String line = scanner.nextLine();
		while(line.startsWith("#")){
			if(scanner.hasNext()){
				line = scanner.nextLine();
			}else{
				return null;
			}
		}
		return line;
	}
	private class DoorContainer{
		private final int id;
		private Vector2 pos;
		private int direction;

		
		public DoorContainer(int id){
			this.id = 0;
			this.pos = new Vector2();
			this.direction = Neighbors.UNDEFINED;
		}
		
		
		
		public boolean isOutOfBounds(int maxX, int maxY){
			if(pos.x >= maxX || pos.y >= maxY || pos.x < 0 || pos.y < 0){
				return true;
			}
			return false;
		}
		
		public void setPos(Vector2 pos){
			this.pos = pos;
		}
		
		public Vector2 getPos(){
			return pos;
		}
		
		public void setDirection(int direction){
			this.direction = direction;
		}
		
		public int getDirection(){
			return direction;
		}
		
		public int getId(){
			return id;
		}
		
		public boolean loadFromString(String info){
			info = ScriptParser.stripCurly(info);
			String[] tokens = info.split(" ");
			if(tokens.length == 2){
				this.pos = ScriptParser.getCoordFromPair(tokens[0]);
				this.direction = ScriptParser.getDirection(tokens[1]);
			}else{
				return false;
			}
			return true;
		}
		
		public String getStringRep(){				
			String acc = "{" + Integer.toString((int)pos.y) + "," + Integer.toString((int)pos.x) + " ";
			String dir = "undefined";
			if(direction == Neighbors.UP){
				dir = "up";
			}else if(direction == Neighbors.DOWN){
				dir = "down";
			}else if(direction == Neighbors.LEFT){
				dir = "left";
			}else if(direction == Neighbors.RIGHT){
				dir = "right";
			}
			acc += dir;
			acc += "}";
			return acc;
		}
	}

	
	private class RoomContainer{
		private final int id;
		private ArrayList<Vector2> poss;
		private String module;
		
		public RoomContainer(int id){
			poss = new ArrayList<Vector2>();
			module = "";
			this.id = id;
		}
		
		/**
		 * Removes tiles from the room.
		 * If all tiles are removed from a room, returns true for deletion of room.
		 * @param tiles
		 * @return whether or not the room still has tiles
		 */
		public boolean removeSubsetOfTiles(Vector2[] tiles){
			ArrayList<Vector2> tilesToRemove = new ArrayList<Vector2>();
			for(Vector2 thisPos : poss){
				for(Vector2 remTile : tiles){
					if(thisPos.equals(remTile)){
						tilesToRemove.add(thisPos);
					}
				}
			}
			poss.removeAll(tilesToRemove);
			
			if(poss.size() == 0){
				return true;
			}else{
				return false;
			}
		}
		
		public boolean isInRoom(Vector2 tile){
			for(Vector2 pos : poss){
				if(tile.equals(pos)){
					return true;
				}
			}
			return false;
		}
		
		public boolean isEntireRoomOutOfBounds(int maxX, int maxY){
			for(Vector2 v : poss){
				if(v.x < maxX && v.y < maxY && v.x > -1 && v.y > -1){
					return false;
				}
			}
			return true;
		}
		
		public void removeOutOfBounds(int maxX, int maxY){
			ArrayList<Vector2> tilesToRemove = new ArrayList<Vector2>();
			for(Vector2 v : poss){
				if(v.x >= maxX || v.y >= maxY || v.x < 0 || v.y < 0){
					tilesToRemove.add(v);
				}
			}			
			poss.removeAll(tilesToRemove);
		}
		
		public void move(int x, int y){
			for(Vector2 pos : poss){
				pos.x += x;
				pos.y += y;
			}
		}
		
		public void addTile(Vector2 pos){
			this.poss.add(pos);
		}
		
		public void addModule(String module){
			this.module = module;
		}
		
		public String getStringRep(){
			if(poss.size() < 0){
				return "";
			}
			String acc = "{";
			
			for(Vector2 v : poss){
				acc += Integer.toString((int)v.y) + "," + Integer.toString((int)v.x) + " "; 
			}
			acc = acc.substring(0, acc.length()-1);
			acc += "}";
			if(module != ""){
				acc += " , " + module;
			}
			
			return acc;
		}
		
		public boolean loadFromString(String info){
			String[] tokens = info.split(" , ");
			if(tokens.length > 0){
				Vector2[] poss = ScriptParser.getRoomValues(tokens[0]);
				for(Vector2 v : poss){
					this.poss.add(v);
				}
				
				if(tokens.length == 2){
					module = tokens[1];
				}
			}else{
				return false;
			}
			return true;
		}
		
		public int getId(){
			return id;
		}
		
		public String getModule(){
			return module;
		}
	}
}
