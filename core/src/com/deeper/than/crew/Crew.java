package com.deeper.than.crew;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.CellBorder;
import com.deeper.than.DTL;
import com.deeper.than.Door;
import com.deeper.than.FloorTile;
import com.deeper.than.GridSquare;
import com.deeper.than.Neighbors;
import com.deeper.than.NoWall;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

public class Crew extends Actor{
	
	private static final float LARGE_ENOUGH = 999999999;
	private static final float DIAGONAL_MOVE = 14;
	private static final float SQUARE_MOVE = 10;
	public static final int CREW_HEIGHT = 64;
	public static final int CREW_WIDTH = 64;
	public static final float SCALE = 2f;

	private Ship ownerShip;
	private Ship occupiedShip;
	private Room room;
	private Vector2 tilePos;
	private String name;
	private int direction;
	private float stateTime;
	private Races race;
	private ArrayList<Vector2> moves;
	private boolean moving;
	private Door doorToClose;
	
	private ArrayList<GridSquare> openList;
	private ArrayList<GridSquare> closedList;
		
	public Crew(String name, Races race, Ship ship){
		this.ownerShip = ship;
		this.occupiedShip = ship;
		this.name = name;
		this.race = race;
		moving = false;
		moves = new ArrayList<Vector2>();
		direction = Neighbors.DOWN;
		stateTime = 0;
		doorToClose = null;
		
		addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setAsSelected();
				return true;
		    }
		});
	}
	
	private void setAsSelected(){
		((GameplayScreen)Screens.GAMEPLAY.getScreen()).setSelectedCrew(this);
	}
	
	public void initPosition(Vector2 tilePos){
		this.tilePos = tilePos;
		float x = (tilePos.x* FloorTile.TILESIZE);
		float y = (tilePos.y* FloorTile.TILESIZE);
		this.setBounds(x+(FloorTile.TILESIZE/2f - CREW_WIDTH/SCALE/2), y+(FloorTile.TILESIZE/2f - CREW_HEIGHT/SCALE/2), (float)CREW_WIDTH/SCALE, (float)CREW_HEIGHT/SCALE);
	}
	
	public void update(){
		setNextMove();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		float delta = Gdx.graphics.getDeltaTime();
		this.setDebug(true);
		if(moving){
			stateTime += delta;
		}else{
			stateTime = Races.FRAME_TIME;
		}
		batch.draw(getFrame(stateTime, direction), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
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
	
	public void moveTo(Vector2 tile){
		moves = aStarFindPath(tilePos, tile);
	}
	
	private void setNextMove(){
		if(moving) return;
		if(moves.isEmpty()) return;
		Vector2 move = moves.remove(0);
		if(isWalkable(tilePos, move)){
			if(isWalkable(tilePos, move)){
				System.out.println("walkable");
			}else{
				System.out.println("not walkable");
			}
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
				speedRatio= race.getSwimRatio();
			}else{
				speedRatio = race.getWalkRatio();
			}
			stateTime = 0;
			layout[(int)move.y][(int)move.x].setCrewMember(this);
			layout[(int)tilePos.y][(int)tilePos.x].setCrewMember(null);
			//TODO set crew on module if neccesary
			this.addAction(Actions.sequence(Actions.moveTo(move.x*FloorTile.TILESIZE+(FloorTile.TILESIZE/2f - CREW_WIDTH/SCALE/2), move.y*FloorTile.TILESIZE+(FloorTile.TILESIZE/2f - CREW_HEIGHT/SCALE/2), moveFactor * speedRatio), new SetTile(move)));
			//this.act(Gdx.graphics.getDeltaTime());
			moving = true;
		}else{
			System.out.println(moves.toString());
			move = moves.get(moves.size()-1);
			moves.clear();
			moves = aStarFindPath(tilePos, move);
			setNextMove();
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
					currSq.getDoor(dir).changeOpen();
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
		openList = new ArrayList<GridSquare>();
		closedList = new ArrayList<GridSquare>();
		
		
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
	
	public void draw(){
		
	}
	
	private void setTilePos(Vector2 tilePos){
		this.tilePos = tilePos;
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
	
	private class SetTile extends Action{

		Vector2 tilePos;
		
		private SetTile(Vector2 tilePos){
			this.tilePos = tilePos;
		}
		
		@Override
		public boolean act(float delta) {
			setTilePos(tilePos);
			moving = false;
			if(doorToClose != null){
				doorToClose.changeOpen();
				doorToClose = null;
			}
			return true;
		}
		
	}
	
}
