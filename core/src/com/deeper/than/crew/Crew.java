package com.deeper.than.crew;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.deeper.than.CellBorder;
import com.deeper.than.DTL;
import com.deeper.than.Door;
import com.deeper.than.EnemyShip;
import com.deeper.than.FloorTile;
import com.deeper.than.GridSquare;
import com.deeper.than.Neighbors;
import com.deeper.than.NoWall;
import com.deeper.than.PlayerShip;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.CrewSkills.CrewSkillsTypes;
import com.deeper.than.modules.Module;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

public class Crew extends Actor{
	
	public enum CrewState{
		REPAIRING,
		WALKING,
		MANNING,
		FIGHTING,
		IDLE;
	}
	
	private static final float LARGE_ENOUGH = 999999999;
	private static final float DIAGONAL_MOVE = 14;
	private static final float SQUARE_MOVE = 10;
	public static final int CREW_HEIGHT = FloorTile.TILESIZE;
	public static final int CREW_WIDTH = FloorTile.TILESIZE;
	public static final float SCALE = 1.5f;

	private Ship ownerShip;
	private Ship occupiedShip;
	private Room room;
	private Vector2 tilePos;
	private String name;
	private int direction;
	private float stateTime;
	private Races race;
	private ArrayList<Vector2> moves;
	private Door doorToClose;
	private float health;
	private boolean isAlive;
	private boolean selected;
	private CrewState state;
	private CrewSkills skills;
	private CrewTask task;
	
	private boolean needNewPath;
	private Vector2 newEnd;
	
	private ArrayList<GridSquare> openList;
	private ArrayList<GridSquare> closedList;
		
	public Crew(String name, Races race, Ship ship){
		this.ownerShip = ship;
		this.occupiedShip = ship;
		this.name = name;
		this.race = race;
		health = race.getHealth();
		isAlive = true;
		moves = new ArrayList<Vector2>();
		needNewPath = false;
		newEnd = new Vector2();
		direction = Neighbors.DOWN;
		stateTime = 0;
		doorToClose = null;
		selected = false;
		state = CrewState.IDLE;
		skills = new CrewSkills();
		task = null;
		
		if(ship instanceof PlayerShip){
			addListener(new ClickListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if(button == Buttons.LEFT){
						setAsSelected();	
						return true;
					}
					return false;
			    }
			});
		}
		
	}
	
	
	private void setAsSelected(){
		((GameplayScreen)Screens.GAMEPLAY.getScreen()).setSelectedCrew(this);
		selected = true;
	}
	
	public void gainExp(int exp, CrewSkillsTypes skill){
		skills.gainExp(exp, skill);
	}
	
	public void initPosition(Vector2 tilePos){
		this.tilePos = tilePos;
		float x = (tilePos.x* FloorTile.TILESIZE);
		float y = (tilePos.y* FloorTile.TILESIZE);
		this.setBounds(x+(FloorTile.TILESIZE/2f - CREW_WIDTH/SCALE/2), y+(FloorTile.TILESIZE/2f - CREW_HEIGHT/SCALE/2), (float)CREW_WIDTH/SCALE, (float)CREW_HEIGHT/SCALE);
	}
	
	public void initRoom(){
		 setRoom(occupiedShip.getLayout()[(int)tilePos.y][(int)tilePos.x].getRoom());
	}
	
	public void update(){
		setNextMove();
		if(room != null && room.getModule() != null){
//			if((state == CrewState.IDLE || state == CrewState.MANNING) && room.getModule().getDamage() > 0){
//				state = CrewState.REPAIRING;
//			}else if(state == CrewState.REPAIRING && room.getModule().getDamage() == 0){
//				setManningIfPossible();
//			}
		}
		if(task != null && task.performTask()){
			task = null;
		}
	}
	
	private void setManningIfPossible(){
		Module mod = room.getModule();
		
		if(mod != null && !mod.isManned() && !mod.isOnLockdown()){
			if(mod.getDamage() > 0){
				return;
			}
			if(mod.isManable()){
				moveTo(room.getManningLocation());
				return;
			}
		}
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		if(ownerShip instanceof EnemyShip ){
			if(ownerShip == occupiedShip){
				if(!((EnemyShip)ownerShip).canPlayerSeeMyTiles()){
					return;
				}
			}else{
				if(!room.isVisible()){
					return;
				}
			}
		}
		float delta = Gdx.graphics.getDeltaTime();
		if(state == CrewState.WALKING){
			stateTime += delta;
		}else{
			stateTime = Races.FRAME_TIME;
		}
		TextureRegion tex = getFrame(stateTime, direction);
		tex.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Color color = batch.getColor().cpy();
		if(isSelected())
		{
			batch.setColor(.0f, 1f, .0f, 1);
		}else if(CrewState.REPAIRING == state){
			batch.setColor(Color.RED);
		}
		batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		batch.setColor(color);
	}
	
	public TextureRegion getIcon(){
		return getFrame(stateTime, Neighbors.DOWN);
	}
	
	private TextureRegion getFrame(float stateTime, int direction){
		if(direction == Neighbors.UP){
			return race.getUpAnim().getKeyFrame(stateTime, true);
		}
		if(direction == Neighbors.DOWN){
			Animation anim = race.getDownAnim();
			return anim.getKeyFrame(stateTime, true);
		}
		
		if(direction == Neighbors.LEFT){
			return race.getLeftAnim().getKeyFrame(stateTime, true);
		}
		
		if(direction == Neighbors.RIGHT){
			return race.getRightAnim().getKeyFrame(stateTime, true);
		}
		
		return null;
	}
	
	public float getMannhatDistanceFrom(Vector2 tile){
		float dist = 0;
		dist += Math.abs(tile.x - tilePos.x) + Math.abs(tile.y - tilePos.y);
		return dist;
	}
	
	public void moveTo(Vector2 tile){
		
		//addAction(new NewSearch(tile));
		newEnd = tile;
		needNewPath = true;
	}
	
	private boolean stillMoving(){
		if(moves.isEmpty()){
			return false;
		}
		return true;
	}
	
	private void setNextMove(){
		if(state == CrewState.WALKING) return;
		if(needNewPath){
			moves = aStarFindPath(tilePos, newEnd);
			needNewPath = false;
		}
		if(moves.isEmpty()){
			if(occupiedShip.getLayout()[(int)tilePos.y][(int)tilePos.x].crewStandingCount() > 1){
				ArrayList<Crew> iIC= ownerShip.getLayout()[(int)tilePos.y][(int)tilePos.x].intrudingStandingCrew();
				for(Crew c : iIC){
					if(!c.stillMoving()){
						c.moveTo(c.findNearestOpenSpot(tilePos));
					}
				}
			}
			if(CrewState.REPAIRING == state){
				return;
			}else if(state == CrewState.MANNING){
				Module mod = ownerShip.getLayout()[(int)tilePos.y][(int)tilePos.x].getRoom().getModule();
				if(mod != null && !mod.isManned()){
					state = CrewState.IDLE;
				}
				
			}
			if(ownerShip.getLayout()[(int)tilePos.y][(int)tilePos.x].getRoom().getManningLocation() == tilePos && ownerShip == occupiedShip){
				Module mod = ownerShip.getLayout()[(int)tilePos.y][(int)tilePos.x].getRoom().getModule();
				if(mod != null && !mod.isManned() && !mod.isOnLockdown()){
					state = CrewState.MANNING;
					mod.setManning(this);
					return;
				}
			}
			setManningIfPossible();
			return;
		}
		if(state == CrewState.MANNING){
			Module mod = ownerShip.getLayout()[(int)tilePos.y][(int)tilePos.x].getRoom().getModule();
			if(mod != null){
				state = CrewState.IDLE;
				mod.setManning(null);
			}
		}
		Vector2 move = moves.remove(0);
		if(isWalkable(tilePos, move)){
			int xDiff = (int) (move.x - tilePos.x);
			int yDiff = (int) (move.y - tilePos.y);
			float moveFactor;
			float speedRatio;
			if(xDiff != 0 && yDiff != 0){
				moveFactor = DIAGONAL_MOVE/15;
			}else{
				moveFactor = SQUARE_MOVE/15;
			}
			if(yDiff == -1){
				direction = Neighbors.DOWN;
			}else if(yDiff == 1){
				direction = Neighbors.UP;
			}
			
			//This will overide changes due to Y. This is intentional.
			//We dont have a diagonal movement anim, so any diagonal movement will be represented by a side movement.
			if(xDiff == -1){
				direction = Neighbors.LEFT;
			}else if(xDiff == 1){
				direction = Neighbors.RIGHT;
			}
			
			GridSquare[][] layout = occupiedShip.getLayout();
			if(layout[(int)tilePos.y][(int)tilePos.x].getRoom().isWaterSwimHeight()){
				speedRatio= 1/race.getSwimRatio();
			}else{
				speedRatio = 1/race.getWalkRatio();
			}
			stateTime = 0;
			layout[(int)move.y][(int)move.x].addCrewMember(this);
			layout[(int)tilePos.y][(int)tilePos.x].removeCrewMember(this);
			//TODO set crew on module if neccesary
			this.addAction(Actions.sequence(Actions.moveTo(move.x*FloorTile.TILESIZE+(FloorTile.TILESIZE/2f - CREW_WIDTH/SCALE/2), move.y*FloorTile.TILESIZE+(FloorTile.TILESIZE/2f - CREW_HEIGHT/SCALE/2), moveFactor * speedRatio), new SetTile(move)));
			//this.act(Gdx.graphics.getDeltaTime());
			state = CrewState.WALKING;
		}else{
			System.out.println(moves.toString());
			if(moves.size() != 0){
				move = moves.get(moves.size()-1);
				moves.clear();
			}
			moves = aStarFindPath(tilePos, move);
			setNextMove();
		}
	}
	
	public Vector2 findNearestOpenSpot(Vector2 pos){
		if(openList == null){
			openList = new ArrayList<GridSquare>();
		}else{
			openList.clear();
		}
		if(closedList == null){
			closedList = new ArrayList<GridSquare>();
		}else{
			closedList.clear();
		}

		GridSquare[][] layout = occupiedShip.getLayout();
		for(int j = 0; j<layout.length; j++){
			for(int i = 0; i< layout[0].length; i++){
				if(layout[j][i] != null){
					layout[j][i].setOnClosedList(false);
					layout[j][i].setOnOpenList(false);
				}
			}	
		}
			
		GridSquare currSq; 
		
		openList.add(layout[(int)pos.y][(int)pos.x]);
		
		while(!openList.isEmpty()){
			currSq = openList.remove(0);
			if(!currSq.hasCrewMember()){
				return currSq.getPos();
			}
			closedList.add(currSq);
			addToOpenBFS(getNeighbor(currSq, Neighbors.UPPER_LEFT, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.UP, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.UPPER_RIGHT, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.RIGHT, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.LOWER_RIGHT, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.DOWN, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.LOWER_LEFT, layout));
			addToOpenBFS(getNeighbor(currSq, Neighbors.LEFT, layout));
		}
		return null;
	}
	
	private void addToOpenBFS(GridSquare gs){
		if(gs == null){
			return;
		}
		if(!closedList.contains(gs)){
			openList.add(gs);
		}
	}
	
	//ASSUMPTION: passed in start and end are valid points
	private boolean isWalkable(Vector2 start, Vector2 end){
		GridSquare[][] layout = occupiedShip.getLayout();
		if(layout[(int)end.y][(int)end.x].isPathClosed()){
			return false;
		}
		
		int dir = Neighbors.UNDEFINED;
		float xDiff = end.x - start.x;
		float yDiff = end.y - start.y;
		
		if(xDiff == -1 && yDiff == 1){
			dir = Neighbors.UPPER_LEFT;
		}else if(xDiff == 0 && yDiff == 1){
			dir = Neighbors.UP;
		}else if(xDiff == 1 && yDiff == 1){
			dir = Neighbors.UPPER_RIGHT;
		}else if(xDiff == 1 && yDiff == 0){
			dir = Neighbors.RIGHT;
		}else if(xDiff == 1 && yDiff == -1){
			dir = Neighbors.LOWER_RIGHT;
		}else if(xDiff == 0 && yDiff == -1){
			dir = Neighbors.DOWN;
		}else if(xDiff == -1 && yDiff == -1){
			dir = Neighbors.LOWER_LEFT;
		}else if(xDiff == -1 && yDiff == 0){
			dir = Neighbors.LEFT;
		}else{
			//not adjacent, cant walk them.
			return false;
		}
		
		if(dir == Neighbors.UP || dir == Neighbors.DOWN || dir == Neighbors.LEFT || dir == Neighbors.RIGHT){
			GridSquare currSq = layout[(int)start.y][(int)start.x];
			if(currSq.hasDoor(dir) && (currSq.getDoor(dir).isOpen() || currSq.getDoor(dir).isOpenableByCrewMem(this))){
				if(!currSq.getDoor(dir).isOpen()){
					currSq.getDoor(dir).changeOpenOverrideHatch();;
					doorToClose = currSq.getDoor(dir);
				}
				return true;
			}else if(currSq.getBorder(dir) instanceof NoWall){
				return true;
			}else{
				return false;
			}
		}
		
		return true;
	}
	

	
	private ArrayList<Vector2> aStarFindPath(Vector2 start, Vector2 end){
		GridSquare[][] layout = occupiedShip.getLayout();
		if(openList == null){
			openList = new ArrayList<GridSquare>();
		}else{
			openList.clear();
		}
		if(closedList == null){
			closedList = new ArrayList<GridSquare>();
		}else{
			closedList.clear();
		}
		
		
		//initialize
		for(int j = 0; j<layout.length; j++){
			for(int i = 0; i< layout[0].length; i++){
				if(layout[j][i] != null){
					layout[j][i].setOnClosedList(false);
					layout[j][i].setOnOpenList(false);
					//set manhantann heuristic
					layout[j][i].setHManh(end);
				}
			}
		}


		GridSquare currSq = layout[(int)start.y][(int)start.x];
		currSq.setGValue(0);
		currSq.setPathPointer(null);
		addToOpen(currSq);
		
		ArrayList<Vector2> moves = new ArrayList<Vector2>();
		while(true){
			if(openList.isEmpty()){
				DTL.printDebug("no path");
				return moves;
			}
			currSq = popSmallest(openList);
			addToClosed(currSq);
			if(currSq.getPos().equals(end)){
				break;
			}
			addNeighborToList(getNeighbor(currSq, Neighbors.UPPER_LEFT, layout), currSq, Neighbors.UPPER_LEFT);
			addNeighborToList(getNeighbor(currSq, Neighbors.UP, layout), currSq, Neighbors.UP);
			addNeighborToList(getNeighbor(currSq, Neighbors.UPPER_RIGHT, layout), currSq, Neighbors.UPPER_RIGHT);
			addNeighborToList(getNeighbor(currSq, Neighbors.RIGHT, layout), currSq, Neighbors.RIGHT);
			addNeighborToList(getNeighbor(currSq, Neighbors.LOWER_RIGHT, layout), currSq, Neighbors.LOWER_RIGHT);
			addNeighborToList(getNeighbor(currSq, Neighbors.DOWN, layout), currSq, Neighbors.DOWN);
			addNeighborToList(getNeighbor(currSq, Neighbors.LOWER_LEFT, layout), currSq, Neighbors.LOWER_LEFT);
			addNeighborToList(getNeighbor(currSq, Neighbors.LEFT, layout), currSq, Neighbors.LEFT);
		}
		
		
		
		
		while(currSq.getPathPointer() != null){
			moves.add(currSq.getPos());
			currSq = currSq.getPathPointer();
		}
		moves.add(currSq.getPos());
		Collections.reverse(moves);
		moves.remove(0);
		return moves;
	}
	
	private void addNeighborToList(GridSquare neighbor, GridSquare currSq, int dir){
		if(neighbor != null){
			float moveValue = 0;
			if(dir == Neighbors.UP || dir == Neighbors.RIGHT || dir == Neighbors.DOWN || dir == Neighbors.LEFT){
				moveValue = SQUARE_MOVE;
			}else if(dir == Neighbors.UPPER_LEFT || dir == Neighbors.UPPER_RIGHT || dir == Neighbors.LOWER_RIGHT || dir == Neighbors.LOWER_LEFT){
				moveValue = DIAGONAL_MOVE;
			}else{
				DTL.printDebug("Inporper direction in addNeighborToList");
				System.exit(-1);
			}
			
			
			if(neighbor.isOnOpenList()){
				if(neighbor.getGValue() > currSq.getGValue() + moveValue){
					neighbor.setGValue(currSq.getGValue() + moveValue);
					neighbor.setPathPointer(currSq);
				}
			}else{
				neighbor.setGValue(currSq.getGValue()+ moveValue);
				neighbor.setPathPointer(currSq);
				addToOpen(neighbor);
			}
		}
	}
	
	private void addToOpen(GridSquare currSq){
		openList.add(currSq);
		currSq.setOnOpenList(true);
	}
	
	private GridSquare removeFromOpen(int i){
		GridSquare square = openList.remove(i);
		square.setOnOpenList(false);
		return square;
	}
	
	private GridSquare removeFromOpen(GridSquare square){
		if(openList.contains(square)){
			openList.remove(square);
			square.setOnOpenList(false);
		}
		return square;
	}
	private void addToClosed(GridSquare currSq){
		closedList.add(currSq);
		removeFromOpen(currSq);
		currSq.setOnClosedList(true);
	}
	private GridSquare removeFromClosed(int i){
		GridSquare square = closedList.remove(i);
		square.setOnClosedList(false);
		return square;
	}
	
	private GridSquare removeFromClosed(GridSquare square){
		closedList.remove(square);
		square.setOnOpenList(false);
		return square;
	}
	
	
	
	private GridSquare popSmallest(ArrayList<GridSquare> openList){
		float smallest = LARGE_ENOUGH;
		float test;
		int indexOfSmallest = -1;
		for(int i = 0; i < openList.size(); i++){
			test = openList.get(i).getFValue();
			if(test < smallest){
				smallest = test;
				indexOfSmallest = i;
			}
		}
		
		if(indexOfSmallest == -1){
			DTL.printDebug("Something went wrong in finding smallest in crew pathfinding");
			System.exit(-1);
		}
		return removeFromOpen(indexOfSmallest);
	}
	
	private GridSquare getNeighbor(GridSquare currSq, int dir, GridSquare[][] layout){
		//do not modify pos
		Vector2 pos = currSq.getPos();
		Vector2 newPos = new Vector2(pos);
		int height = layout.length-1;
		int width = layout[0].length-1;
		int xAdjust = 0;
		int yAdjust = 0;
		switch(dir){
		case Neighbors.UPPER_LEFT:
			xAdjust = -1;
			yAdjust = 1;
			break;
		case Neighbors.UP:
			xAdjust = 0;
			yAdjust = 1;
			break;
		case Neighbors.UPPER_RIGHT:
			xAdjust = 1;
			yAdjust = 1;
			break;
		case Neighbors.RIGHT:
			xAdjust = 1;
			yAdjust = 0;
			break;
		case Neighbors.LOWER_RIGHT:
			xAdjust = 1;
			yAdjust = -1;
			break;
		case Neighbors.DOWN:
			xAdjust = 0;
			yAdjust = -1;
			break;
		case Neighbors.LOWER_LEFT:
			xAdjust = -1;
			yAdjust = -1;
			break;
		case Neighbors.LEFT:
			xAdjust = -1;
			yAdjust = 0;
			break;
		}
		
		newPos.add(xAdjust, yAdjust);
		
		
		if(newPos.x < 0 || newPos.x > width || newPos.y < 0 || newPos.y > height){
			return null;
		}
		
		GridSquare newSquare = layout[(int)newPos.y][(int)newPos.x];
		if(newSquare == null){
			return null;
		}
		if(newSquare.isPathClosed() || newSquare.isOnClosedList()){
			return null;
		}
		//might look like lots of useless code but im trying to instantiate as few objects as possible
		CellBorder wall;
		switch(dir){
		case Neighbors.UPPER_LEFT:
			wall = currSq.getBorder(Neighbors.UP);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = currSq.getBorder(Neighbors.LEFT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.DOWN);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.RIGHT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			break;
		case Neighbors.UP:
			wall = currSq.getBorder(Neighbors.UP);
			if(wall instanceof Door){
				Door door = (Door)wall;
				if(!door.isOpenableByCrewMem(this)){
					return null;
				}
			}else{
				if(!(wall instanceof NoWall)){
					return null;
				}	
			}
			break;
		case Neighbors.UPPER_RIGHT:
			wall = currSq.getBorder(Neighbors.UP);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = currSq.getBorder(Neighbors.RIGHT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.DOWN);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.LEFT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			break;
		case Neighbors.RIGHT:
			wall = currSq.getBorder(Neighbors.RIGHT);
			if(wall instanceof Door){
				Door door = (Door)wall;
				if(!door.isOpenableByCrewMem(this)){
					return null;
				}
			}else{
				if(!(wall instanceof NoWall)){
					return null;
				}	
			}
			break;
		case Neighbors.LOWER_RIGHT:
			wall = currSq.getBorder(Neighbors.DOWN);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = currSq.getBorder(Neighbors.RIGHT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.UP);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.LEFT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			break;
		case Neighbors.DOWN:
			wall = currSq.getBorder(Neighbors.DOWN);
			if(wall instanceof Door){
				Door door = (Door)wall;
				if(!door.isOpenableByCrewMem(this)){
					return null;
				}
			}else{
				if(!(wall instanceof NoWall)){
					return null;
				}	
			}
			break;
		case Neighbors.LOWER_LEFT:
			wall = currSq.getBorder(Neighbors.DOWN);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = currSq.getBorder(Neighbors.LEFT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.UP);
			if(!(wall instanceof NoWall)){
				return null;
			}
			wall = newSquare.getBorder(Neighbors.RIGHT);
			if(!(wall instanceof NoWall)){
				return null;
			}
			break;
		case Neighbors.LEFT:
			wall = currSq.getBorder(Neighbors.LEFT);
			if(wall instanceof Door){
				Door door = (Door)wall;
				if(!door.isOpenableByCrewMem(this)){
					return null;
				}
			}else{
				if(!(wall instanceof NoWall)){
					return null;
				}	
			}
			break;
		}

		return newSquare;
	}
	
	/**
	 * Returns a copy of the position
	 * @return
	 */
	public Vector2 getTilePos(){
		return tilePos.cpy();
	}
	
	private void setTilePos(Vector2 tilePos){
		this.tilePos = tilePos;
		GridSquare gs = occupiedShip.getLayout()[(int)tilePos.y][(int)tilePos.x];
		setRoom(gs.getRoom());
	}
	
	public float getHealth() {
		return health;
	}
	
	public boolean isAlive(){
		return isAlive;
	}

	public void setHealth(float health) {
		if(health > getRace().getHealth()){
			health = getRace().getHealth();
		}else if(health < 0){
			health = 0;
		}
		this.health = health;
		if(this.health == 0){
			isAlive = false;
		}
	}

	public Room getRoom(){
		return room;
	}
	
	public void setRoom(Room room){
		this.room = room;
	}
	
	public int getOwnerShipId(){
		return ownerShip.getId();
	}
	
	public Ship getOwnerShip(){
		return ownerShip;
	}
	
	public Races getRace() {
		return race;
	}
	
	public float getRepairRatio(){
		return race.getRepairRatio() + skills.getBonusRate(CrewSkillsTypes.REPAIR);
	}
	
	public float getSheildRechargeRatio(){
		return skills.getBonusRate(CrewSkillsTypes.SHIELDS);
	}
	
	public float getBridgeEvadeRatio(){
		return skills.getBonusRate(CrewSkillsTypes.BRIDGE);
	}
	
	public float getEngineEvadeRatio(){
		return skills.getBonusRate(CrewSkillsTypes.ENGINES);
	}

	public String getName() {
		return name;
	}
	
	public boolean isSelected() {
		return selected;
	}


	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public CrewState getState() {
		return state;
	}
	
	public CrewSkills getSkills() {
		return skills;
	}
	
	public CrewTask getTask() {
		return task;
	}


	public void forceSetTask(CrewTask task) {
		this.task = task;
	}
	
	/**
	 * Only sets task if one is not already assigned.
	 * Returns false on failed assignment
	 * @param task
	 * @return
	 */
	public boolean assignTask(CrewTask task){
		if(getTask() == null){
			forceSetTask(task);
			return true;
		}
		return false;
	}
	
	public boolean isAssignedTask(){
		if(getTask() != null){
			return true;
		}
		return false;
	}
	
	public void setRepairing(boolean repairing){
		if(repairing){
			if(state == CrewState.MANNING){
				Module mod = ownerShip.getLayout()[(int)tilePos.y][(int)tilePos.x].getRoom().getModule();
				if(mod != null){
					state = CrewState.IDLE;
					mod.setManning(null);
				}
			}
			state = CrewState.REPAIRING;
		}else{
			state = CrewState.IDLE;
		}
	}
	
	public class MoveToPointOfIntrest extends Action implements Poolable{
		private Vector2 posToMoveTo;
		
		public void init(Vector2 posToMoveTo){
			this.posToMoveTo = posToMoveTo;
		}
		
		@Override
		public boolean act(float delta) {
			moveTo(posToMoveTo);
			return false;
		}
		
		@Override
		public void reset(){
			this.posToMoveTo = null;
		}
		
	}

	private class SetTile extends Action{

		private Vector2 tilePos;
		
		private SetTile(Vector2 tilePos){
			this.tilePos = tilePos;
		}
		
		@Override
		public boolean act(float delta) {
			setTilePos(tilePos);
			state = CrewState.IDLE;

			if(doorToClose != null){
				doorToClose.changeOpenOverrideHatch();
				doorToClose = null;
			}
			return true;
		}
		
	}
	
}
