package com.deeper.than.crew;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.deeper.than.Room;
import com.deeper.than.crew.Crew.CrewState;

public class CrewRepairTask extends CrewTask{

	//private final Array<CrewRepairTask> activeRepairTasks = new Array<CrewRepairTask>();
	
	private static final Pool<CrewRepairTask> repairTaskPool = new Pool<CrewRepairTask>(){
	    @Override
	    protected CrewRepairTask newObject() {
	        return new CrewRepairTask();
	    }
	};
	
	public static CrewRepairTask obtain(){
		return repairTaskPool.obtain();
	}
	
	public static void free(CrewRepairTask task){
		 repairTaskPool.free(task);
	}
	
	private Room damagedRoom;

	private  CrewRepairTask() {
		super();
		this.damagedRoom = null;
	}
	
	
	public void init(int priority, Room room){
		initPriority(priority);
		this.damagedRoom = room;
	}
	
	@Override
	public void reset(){
		super.reset();
		if(damagedRoom != null && damagedRoom.getModule() != null){
			damagedRoom.getModule().setRepairTaskQueued(false);
		}
		damagedRoom = null;
	}
	
	@Override
	public boolean performTask() {
		if(!damagedRoom.isDamaged()){
			this.setCompleted(true);
			return true;
		}
		if(damagedRoom.isinRoom(crew.getTilePos())){
			return false;
		}
		if(this.crew.getState() == CrewState.IDLE || this.crew.getState() == CrewState.MANNING || this.crew.getState() == CrewState.FIGHTING){
			crew.moveTo(damagedRoom.selectTileToWalkTo().getPos());
		}
		return false;
	}
	
	public Vector2 getLocation(){
		return damagedRoom.selectTileToWalkTo().getPos();
	}

}
