package com.deeper.than;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.CrewGoToRoomTask;
import com.deeper.than.crew.CrewRepairTask;
import com.deeper.than.crew.CrewTask;

public class EnemyShip extends Ship{

	private PlayerShip playerShip;
	
	private ArrayList<CrewTask> taskQueue;
	
	
	public EnemyShip(FileHandle filepath, DTL game, int id, PlayerShip playerShip) {
		super(filepath, false, game, id);
		this.playerShip = playerShip;
		taskQueue = new ArrayList<CrewTask>();
	}
	
	public boolean canPlayerSeeMyTiles(){
		return playerShip.canSeeEnemyShipInterior();
	}
	
	public boolean canPlayerSeeMyPower(){
		return playerShip.getSensors().canSeeEnemyPowerUse();
	}

	@Override
	public void update(){
		ArrayList<Crew> untaskedCrew = getUntaskedCrew();
		Crew crew;
		Iterator<CrewTask> iter = taskQueue.iterator();
		CrewTask task = null;
		while(iter.hasNext()){
			task = iter.next();
			crew = null;
			
			if(task.isCompleted()){
				freeTask(task);
				iter.remove();
				continue;
			}
			
			if(!task.checkAssigned()){
				if(task instanceof CrewRepairTask){
					crew = findClosestCrew(((CrewRepairTask)task).getLocation(), untaskedCrew);
					if(crew != null){
						task.assign(crew);
					}
				}else if(task instanceof CrewGoToRoomTask){
					crew = findClosestCrew(((CrewGoToRoomTask)task).getLocation(), untaskedCrew);
					if(crew != null){
						task.assign(crew);
					}
				}
				
				
			}
			
		}
		
		super.update();
	}
	
	private void freeTask(CrewTask task){
		if(task instanceof CrewRepairTask){
			CrewRepairTask.free((CrewRepairTask)task);
		}
	}
	
	public void addToTaskQueue(CrewTask task){
		taskQueue.add(task);
	}
	
	public Crew findClosestCrew(Vector2 pos, ArrayList<Crew> crew){
		Crew closest = null;
		float dist = 1000000;
		float compDist = 0;
		for(Crew c : crew){
			compDist = c.getMannhatDistanceFrom(pos);
			if(compDist < dist){
				closest = c;
				dist = compDist;
			}
		}
		return closest;
	}
	
	public ArrayList<Crew> getUntaskedCrew(){
		ArrayList<Crew> untaskedCrew = new ArrayList<Crew>();
		for(Crew c : this.getCrew()){
			if(!c.isAssignedTask()){
				untaskedCrew.add(c);
			}
		}
		return untaskedCrew;
	}
}
