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
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;
import com.deeper.than.weapons.WeaponGenerator;
import com.deeper.than.weapons.WeaponGenerator.WeaponTypes;
import com.deeper.than.weapons.WeaponMakers;
import com.deeper.than.weapons.WeaponQualities;


public class ScriptParser implements Poolable{
	
	private final Array<ScriptParser> activeParsers= new Array<ScriptParser>();
	public static  final Pool<ScriptParser> parserPool = new Pool<ScriptParser>(5){
		protected ScriptParser newObject(){
			return new ScriptParser();
		}
	};
	
	private static final String DEFAULT_NAME = "NameYourDangShip";
	private static final int XDIM_DEFAULT = 1;
	private static final int YDIM_DEFAULT = 1;
	private static final int HEALTH_DEFAULT = 5;
	private static final int POWER_DEFAULT = 8;
	private static final String FLOORTILEIMG_DEFAULT = "floortile.png";
	private static final String DOORIMG_DEFAULT = "door.pack";
	
	private static final int FUEL_DEFAULT = 15;
	private static final int CURRENCY_DEFAULT = 30;
	
	private boolean nameSet;
	private boolean xDimSet;
	private boolean yDimSet;
	private boolean healthSet;
	private boolean powerSet;
	private boolean floortileImgSet;
	private boolean doorSet;
	
	private boolean fuelSet;
	private boolean currencySet;
	
	
	private int lineNum=0;
	private String[] tokens;
	
	public void loadShipScript(Ship ship, String str, WeaponGenerator weaponGen) throws IOException, ShipLoadException{
		Scanner scanner = new Scanner(str);
		try{
			loadShipScript(ship, scanner, weaponGen);
		}catch(Exception e){
			scanner.close();
			throw e;
		}
	}
	
	public void loadShipScript(Ship ship, FileHandle filepath, WeaponGenerator weaponGen) throws IOException, ShipLoadException{
		Scanner scanner = new Scanner(filepath.read());
		try{
			loadShipScript(ship, scanner, weaponGen);
		}catch(Exception e){
			scanner.close();
			throw e;
		}
	}

	public void proccessNextLine(String line, Ship ship, Scanner scanner, WeaponGenerator weaponGen) throws ShipLoadException{
		tokens = line.split(" ");
		
		switch(tokens[0]){
		case "currency=":
			if(ship instanceof PlayerShip){
				if(tokens.length > 1){
					((PlayerShip)ship).setCurrency(Integer.parseInt(tokens[1]));
					currencySet = true;
				}
			}
			break;
		case "fuel=":
			if(ship instanceof PlayerShip){
				if(tokens.length > 1){
					((PlayerShip)ship).setFuel(Integer.parseInt(tokens[1]));
					fuelSet = true;
				}
			}
			break;
		case "name=":
			if(tokens.length > 1){
				ship.name = tokens[1].replaceAll("\\s", "");
				nameSet = true;
			}
			break;
		case "xDim=":
			if(tokens.length > 1){
				ship.gridWidth = Integer.parseInt(tokens[1]);
				xDimSet = true;
				if(yDimSet){
					initializeShipLayout(ship);
				}
			}
			break;
		case "yDim=":
			if(tokens.length > 1){
				ship.gridHeight = Integer.parseInt(tokens[1]);
				yDimSet = true;
				if(xDimSet){
					initializeShipLayout(ship);
				}
			}
			break;
		case "health=":
			if(tokens.length > 1){
				ship.setHealth(Integer.parseInt(tokens[1]));
				ship.setMaxHealth(Integer.parseInt(tokens[1]));
				healthSet = true;
			}
			break;
		case "power=":
			if(tokens.length > 1){
				ship.setPower(Integer.parseInt(tokens[1]));
				powerSet = true;
			}
			break;
		case "floortileimg=":
			if(tokens.length > 1){
				ship.setFloorTileImgHandle(tokens[1]);
				floortileImgSet = true;
			}
			break;
		case "doorimg=":
			if(tokens.length > 1){
				ship.setDoorImgHandle(tokens[1]);
				doorSet = true;
			}
			break;
		case "weapons=":
			loadWeapons(scanner, ship, weaponGen);
			break;
		case "layout=":
			if(!xDimSet || !yDimSet){
				return;
			}
			loadRooms(scanner, ship);
			break;
		case "doors=":
			if(!xDimSet || !yDimSet){
				return;
			}
			loadDoors(scanner, ship);
			break;
		}
	}
	
	private void loadWeapons(Scanner scanner, Ship ship, WeaponGenerator weaponGen){
		String line;
		WeaponTypes type;
		WeaponMakers make;
		WeaponQualities quality;
		Weapon weapon;
		while(scanner.hasNext()){
			line = getNextNonCommentLine(scanner);
			type = null;
			make = null;
			quality = null;
			weapon = null;
			tokens = line.split(" ");
			if(tokens.length >= 1){
				if(tokens[0].equals("endweapons")){
					return;
				}
				else{
					type = getWeaponType(tokens[0]);
				}
			}
			if(tokens.length >= 2){
				quality = getWeaponQuality(tokens[1]);
			}
			if(tokens.length >= 3){
				make = getWeaponMake(tokens[2]);
			}
			if(type!=null){
				if(make==null && quality!=null){
					weapon = weaponGen.generate(type, quality);
				}else if(make!=null && quality==null){
					weapon = weaponGen.generate(type, make);
				}else if(make!= null && quality!=null){
					weapon = weaponGen.generate(type, quality, make);
				}else if(make== null && quality==null){
					weapon = weaponGen.generate(type);
				}
			}
			if(weapon != null){
				ship.addWeapon(weapon);
			}
		}
	}
	
	private WeaponMakers getWeaponMake(String make){
		switch(make){
		case "bnl":
			return WeaponMakers.BUY_N_LARGE;
		case "xyl":
			return WeaponMakers.XYL;
		case "threetek":
			return WeaponMakers.THREE_TEK;
		case "lukco":
			return WeaponMakers.LUK_CO;
		case "boomnzoom":
			return WeaponMakers.BOOM_N_ZOOM;
		}
		return null;
	}
	
	private WeaponQualities getWeaponQuality(String quality){
		switch(quality){
		case "exceptional":
			return WeaponQualities.EXCEPTIONAL;
		case "pristine":
			return WeaponQualities.PRISTINE;
		case "likenew":
			return WeaponQualities.LIKENEW;
		case "worn":
			return WeaponQualities.WORN;
		case "damaged":
			return WeaponQualities.DAMAGED;
		}
		return null;
	}
	
	private WeaponTypes getWeaponType(String type){
		switch(type){
		case "beamweapon":
			return WeaponTypes.BEAM_WEAPON;
		case "concussionbomb":
			return WeaponTypes.CONCUSSION_BOMB;
		case "emp":
			return WeaponTypes.EMP_WEAPON;
		case "laser":
			return WeaponTypes.LASER;
		case "railgun":
			return WeaponTypes.RAILGUN;
		case "supercool":
			return WeaponTypes.SUPERCOOLING_BEAM;
		case "torpedo":
			return WeaponTypes.TORPEDO_LAUNCHER;
		}
		return null;
	}
	
	private void initializeShipLayout(Ship ship){
		ship.layout = new GridSquare[ship.gridHeight][ship.gridWidth];
		for(int i = 0; i< ship.gridHeight; i++){
			for(int j = 0; j < ship.gridWidth; j++){
				ship.layout[i][j] = null;
			}
		}
	}
	
	private void loadDoors(Scanner scanner, Ship ship) throws ShipLoadException{
		Door door = null;
		String line;
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
				return;
			}
		}
		
		throw new ShipLoadException();
	}
	
	private void loadRooms(Scanner scanner, Ship ship) throws ShipLoadException{
		if(scanner == null || ship == null){
			throw new ShipLoadException();
		}
		String line;
		Vector2 poss[] = null;
		int roomId = -1;
		int moduleId = -1;
		Room room;
		FloorTile fl;
		GridSquare gs;
		Crew crew;
		while(scanner.hasNext()){
			line = getNextNonCommentLine(scanner);
			if(line.startsWith("endlayout")){
				return;
			}
			
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
			}
			
		}
		
		throw new ShipLoadException();
	}
	
	public void setUnsetsToDefault(Ship ship){
		if(!nameSet){
			ship.setName(DEFAULT_NAME);
		}
		if(!healthSet){
			ship.setHealth(HEALTH_DEFAULT);
			ship.setMaxHealth(HEALTH_DEFAULT);
		}
		if(!powerSet){
			ship.setPower(POWER_DEFAULT);
		}
		if(!floortileImgSet){
			ship.setFloorTileImgHandle(FLOORTILEIMG_DEFAULT);
		}
		if(!doorSet){
			ship.setDoorImgHandle(DOORIMG_DEFAULT);
		}
		
		if(ship instanceof PlayerShip){
			PlayerShip pShip = ((PlayerShip)ship);
			if(!fuelSet){
				pShip.setFuel(FUEL_DEFAULT);
			}
			if(!currencySet){
				pShip.setCurrency(CURRENCY_DEFAULT);
			}
		}
	}
	
	public void loadShipScript(Ship ship, Scanner scanner, WeaponGenerator weaponGen) throws IOException, ShipLoadException{
		nameSet = false;
		xDimSet = false;
		yDimSet = false;
		healthSet = false;
		powerSet = false;
		floortileImgSet = false;
		doorSet = false;
		
		fuelSet = false;
		currencySet = false;
		
		lineNum =0;
		
		String line;
		while(scanner.hasNext()){
			line = getNextNonCommentLine(scanner);
			proccessNextLine(line, ship, scanner, weaponGen);
		}
		
		setUnsetsToDefault(ship);
		scanner.close();
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
		}else if(type.equals("WeaponsModule")){
			module = Modules.Weapons.instantiateModule(moduleId++, room, ship);
			ship.setWeaponModule((WeaponsModule)module);
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
