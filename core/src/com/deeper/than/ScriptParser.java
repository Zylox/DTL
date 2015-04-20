package com.deeper.than;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.Races;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.ClimateControlModule;
import com.deeper.than.modules.DockingModule;
import com.deeper.than.modules.EngineModule;
import com.deeper.than.modules.HatchControlModule;
import com.deeper.than.modules.MedbayModule;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SensorsModule;
import com.deeper.than.modules.SheildModule;


public class ScriptParser implements Poolable{
	
	private final Array<ScriptParser> activeParsers= new Array<ScriptParser>();
	public static  final Pool<ScriptParser> parserPool = new Pool<ScriptParser>(5){
		protected ScriptParser newObject(){
			return new ScriptParser();
		}
	};
	
	
	private int lineNum=0;
	
	public int loadShipScript(Ship ship, String str) throws IOException{
		return loadShipScript(ship, new Scanner(str));
	}
	
	public int loadShipScript(Ship ship, FileHandle filepath) throws IOException{
		return loadShipScript(ship, new Scanner(filepath.read()));
	}

	public int loadShipScript(Ship ship, Scanner scanner) throws IOException{
		lineNum =0;
		
		//checks to see if there is anything other than comments
		String line = getNextNonCommentLine(scanner);
		if(line.equals(null)){
			scanner.close();
			return -2;
		}
		
		lineNum++;
		//sets name
		String[] tokens = line.split(" ");
		if(tokens.length > 1){
			if(tokens[0].equals("name=")){
				ship.name = tokens[1].replaceAll("\\s", "");
			}else{
				System.out.println("name= XXX expected at line " + lineNum);
				scanner.close();
				return -1;
			}
		}
		
		//Getting x and y dim for grid
		//////////////////////////////////////////////////
		int xDim = 0;
		int yDim = 0;
		int power = 0;
		//gets line, checks if it exists, splits into tokens, and sets xDim and yDim if correct
		line = getNextNonCommentLine(scanner);
		if(line.equals(null)){
			scanner.close();
			return -2;
		}
		lineNum++;
		tokens = line.split(" ");
		
		if(tokens[0].equals("xDim=")){
			xDim = Integer.parseInt(tokens[1]);	
		}else{
			System.out.println("xDim= XXX expected at line " + lineNum);
			scanner.close();
			return -1;
		}

		line = getNextNonCommentLine(scanner);
		if(line.equals(null)){
			scanner.close();
			return -2;
		}
		lineNum++;
		tokens = line.split(" ");
		
		if(tokens[0].equals("yDim=")){
			yDim = Integer.parseInt(tokens[1]);	
		}else{
			System.out.println("yDim= XXX expected at line " + lineNum);
			scanner.close();
			return -1;
		}
		line = getNextNonCommentLine(scanner);
		if(line.equals(null)){
			scanner.close();
			return -2;
		}
		
		lineNum++;
		tokens = line.split(" ");
		if(tokens[0].equals("power=")){
			power = Integer.parseInt(tokens[1]);	
		}else{
			System.out.println("power= XXX expected at line " + lineNum);
			scanner.close();
			return -1;
		}
		
		//
		//////////////////////////////////////////////////
		//initializing the layout
		ship.gridWidth = xDim;
		ship.gridHeight = yDim;
		ship.setPower(power);
		ship.layout = new GridSquare[yDim][xDim];
		for(int i = 0; i< yDim; i++){
			for(int j = 0; j < xDim; j++){
				ship.layout[i][j] = null;
			}
		}
		
		line = getNextNonCommentLine(scanner);
		if(line.equals(null)){
			scanner.close();
			return -2;
		}
		lineNum++;
		tokens = line.split(" ");
		
		if(tokens.length > 1){
			if(tokens[0].equals("floortileimg=")){
				ship.setFloorTileImgHandle(tokens[1]);	
			}else{
				System.out.println("floortileimg= XXX expected at line " + lineNum);
				scanner.close();
				return -1;
			}
		}
		
		line = getNextNonCommentLine(scanner);
		if(line.equals(null)){
			scanner.close();
			return -2;
		}
		lineNum++;
		tokens = line.split(" ");
		
		if(tokens.length > 1){
			if(tokens[0].equals("doorimg=")){
				ship.setDoorImgHandle(tokens[1]);	
			}else{
				System.out.println("doorimg= XXX expected at line " + lineNum);
				scanner.close();
				return -1;
			}
		}
		while(scanner.hasNext()){
			lineNum++;
			line = scanner.nextLine();
			//floorlayout case
			if(line.replaceAll("\\s", "").equals("layout=")){
				Vector2 poss[] = null;
				int roomId = -1;
				int moduleId = -1;
				Room room;
				FloorTile fl;
				GridSquare gs;
				Crew crew;
				//internal layout proccesing loop
				while(scanner.hasNext()){
					line = getNextNonCommentLine(scanner);
					if(line.startsWith("{")){
						crew = null;
						roomId++;
						room = new Room(roomId, ship);
						tokens = line.split(" , ");
						if(tokens.length == 4){
							loadModule(tokens[1], Integer.parseInt(tokens[2]), moduleId, room, ship);
							crew = loadCrew(tokens[3], ship);
						}else if(tokens.length == 3){
							loadModule(tokens[1], Integer.parseInt(tokens[2]), moduleId, room, ship);
						}else if(tokens.length == 2){
							crew = loadCrew(tokens[1], ship);
						}
						poss = getRoomValues(tokens[0]);						
						for(Vector2 v : poss){
							gs = new GridSquare();
							fl = new FloorTile(v, gs);
							if(crew != null){
								gs.addCrewMember(crew);
								crew.initPosition(v);
								crew = null;
							}
							gs.setFloorTile(fl);
							gs.setRoom(room);
							gs.setShip(ship);
							fl.setShip(ship);
							ship.layout[(int)v.y][(int)v.x] = gs;
							room.addSquare(gs);
						}
						ship.addRoom(room);
					}else if(line.equals("endlayout")){
						break;
					}else{
						System.out.println("improper layout on line " + lineNum);
						scanner.close();
						return -1;
					}
				}
			}else if(line.replaceAll("\\s", "").equals("doors=")){
				Door door = null;
				while(scanner.hasNext()){
					line = getNextNonCommentLine(scanner);
					if(line.startsWith("{")){
						line = stripCurly(line);
						line.toLowerCase();
						tokens = line.split(" ");
						door = new Door(getCoordFromPair(tokens[0]), getDirection(tokens[1]), ship);
						door.setGridSquare((ship.getLayout())[(int)door.getPos().y][(int)door.getPos().x]);
						ship.addDoor(door);
					}else if(line.equals("enddoors")){
						break;
					}else{
						System.out.println("improper doors on line " + lineNum);
						scanner.close();
						return -1;
					}
				}				
			}
		}
		
		scanner.close();
		return 0;
		
	}
	

	private Crew loadCrew(String race, Ship ship){
		Races crewRace = Races.getRace(race);
		Crew crew = new Crew(crewRace.getRandomName(), crewRace, ship);
		ship.addCrewToList(crew);
		return crew;
	}
	
	private int loadModule(String type, int level, int moduleId, Room room, Ship ship){
		Module module = null;
		moduleId++;
		
		if(type.equals("ClimateControlModule")){
			module = Modules.ClimateControl.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}else if(type.equals("EngineModule")){
			module = Modules.Engine.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}else if(type.equals("BridgeModule")){
			module = Modules.Bridge.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}else if(type.equals("SensorsModule")){
			module = Modules.Sensors.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}else if(type.equals("HatchControlModule")){
			module = Modules.HatchControl.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}else if(type.equals("MedbayModule")){
			module = Modules.Medbay.instantiateModule(moduleId++, room, ship);
			
			module.setLevel(level);
		}else if(type.equals("DockingModule")){
			module = Modules.Docking.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}else if(type.equals("SheildModule")){
			module = Modules.Sheild.instantiateModule(moduleId++, room, ship);
			module.setLevel(level);
		}
		
		room.setModule(module);
		ship.addModule(module);
		return moduleId;
	}
	
	
	
	private String getNextNonCommentLine(Scanner scanner){
		String line = scanner.nextLine();
		while(line.startsWith("#")){
			if(scanner.hasNext()){
				line = scanner.nextLine();
				lineNum++;
			}else{
				return null;
			}
		}
		return line;
	}
	
	
	public static String stripCurly(String line){
		line = line.replace("{", "");
		line = line.replace("}", "");
		
		return line;
	}
	
	public static Vector2[] getRoomValues(String line){
		line = stripCurly(line);
		String[] tokens = line.split(" ");
		Vector2 poss[] = new Vector2[tokens.length];
		int i = 0;
		for(String s : tokens){
			poss[i] = getCoordFromPair(s);
			i++;
		}
		
		return poss;
	}
	
	public static Vector2 getCoordFromPair(String couple){
		String[] tuple = new String[2];
		tuple = couple.split(",");
		return new Vector2(Integer.parseInt(tuple[1]), Integer.parseInt(tuple[0]));
	}
	
	public static int getDirection(String dir){
		if(dir.equals("up")){
			return Neighbors.UP;
		}else if(dir.equals("down")){
			return Neighbors.DOWN;
		}else if(dir.equals("left")){
			return Neighbors.LEFT;
		}else if(dir.equals("right")){
			return Neighbors.RIGHT;
		}else{
			return Neighbors.UNDEFINED;
		}
	}

	@Override
	public void reset() {
		lineNum = 0;
	}

}
