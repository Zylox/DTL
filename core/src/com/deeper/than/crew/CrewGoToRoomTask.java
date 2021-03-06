/**
 * Task that sends crew to a room
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.crew;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.deeper.than.Room;
import com.deeper.than.crew.Crew.CrewState;

/**
 * Task for sending crew to a room
 * @author zach
 *
 */
public class CrewGoToRoomTask extends CrewTask implements Poolable{
	
	/**
	 * Pool of tasks
	 */
	private static final Pool<CrewGoToRoomTask> manTaskPool = new Pool<CrewGoToRoomTask>(){
	    @Override
	    protected CrewGoToRoomTask newObject() {
	        return new CrewGoToRoomTask();
	    }
	};
	
	/**
	 * Gets a task to use. make sure to init, and free when done
	 * @return
	 */
	public static CrewGoToRoomTask obtain(){
		return manTaskPool.obtain();
	}
	
	public static void free(CrewGoToRoomTask task){
		 manTaskPool.free(task);
	}
	
	private Room roomToMan;
	
	private CrewGoToRoomTask(){
		this.roomToMan = null;
	}
	
	@Override
	public void reset(){
		super.reset();
		roomToMan = null;
	}
	
	public void init(Room roomToMan){
		this.roomToMan = roomToMan;
	}

	@Override
	public boolean performTask() {
		if(roomToMan.isinRoom(crew.getTilePos())){
			this.setCompleted(true);
			return true;
		}
		if(this.crew.getState() == CrewState.IDLE || this.crew.getState() == CrewState.MANNING || this.crew.getState() == CrewState.FIGHTING){
			crew.moveTo(roomToMan.selectTileToWalkTo().getPos());
		}
		return false;
	}
	
	public Vector2 getLocation(){
		return roomToMan.selectTileToWalkTo().getPos();
	}
}
