package com.deeper.than;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;


public class ScriptParser implements Poolable{
	
	private final Array<ScriptParser> activeParsers= new Array<ScriptParser>();
	public static  final Pool<ScriptParser> parserPool = new Pool<ScriptParser>(5){
		protected ScriptParser newObject(){
			return new ScriptParser();
		}
	};
	
	
	private int lineNum=0;

	public int loadShipScript(Ship ship, FileHandle filepath) throws IOException{
		Scanner scanner = new Scanner(filepath.read());
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
		if(tokens[0].equals("name=")){
			ship.name = tokens[1].replaceAll("\\s", "");
		}else{
			System.out.println("name= XXX expected at line " + lineNum);
			scanner.close();
			return -1;
		}
		
		//Getting x and y dim for grid
		//////////////////////////////////////////////////
		int xDim = 0;
		int yDim = 0;

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
		//
		//////////////////////////////////////////////////
		//initializing the layout
		ship.gridWidth = xDim;
		ship.gridHeight = yDim;
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
		
		if(tokens[0].equals("floortileimg=")){
			ship.setFloorTileImgHandle(tokens[1]);	
		}else{
			System.out.println("floortileimg= XXX expected at line " + lineNum);
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
		
		if(tokens[0].equals("doorimg=")){
			ship.setDoorImgHandle(tokens[1]);	
		}else{
			System.out.println("doorimg= XXX expected at line " + lineNum);
			scanner.close();
			return -1;
		}
		
		while(scanner.hasNext()){
			lineNum++;
			line = scanner.nextLine();
			//floorlayout case
			if(line.replaceAll("\\s", "").equals("layout=")){
				Vector2 poss[] = null;
				int roomId=-1;
				Room room;
				FloorTile fl;
				GridSquare gs;
				//internal layout proccesing loop
				while(scanner.hasNext()){
					line = getNextNonCommentLine(scanner);
					if(line.startsWith("{")){
						roomId++;
						room = new Room(roomId, ship);
						poss = getRoomValues(line);						
						for(Vector2 v : poss){
							gs = new GridSquare();
							fl = new FloorTile(v, gs);
							gs.setFloorTile(fl);
							gs.setRoom(room);
							gs.setShip(ship);
							fl.setShip(ship);
							ship.layout[(int)v.y][(int)v.x] = gs;
							
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
	
	
	private String stripCurly(String line){
		line = line.replace("{", "");
		line = line.replace("}", "");
		
		return line;
	}
	
	private Vector2[] getRoomValues(String line){
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
	
	private Vector2 getCoordFromPair(String couple){
		String[] tuple = new String[2];
		tuple = couple.split(",");
		return new Vector2(Integer.parseInt(tuple[1]), Integer.parseInt(tuple[0]));
	}
	
	private int getDirection(String dir){
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
