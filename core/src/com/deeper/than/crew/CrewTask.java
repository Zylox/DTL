package com.deeper.than.crew;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class CrewTask implements Poolable, Comparable<CrewTask>{
//	private final Array<CrewTask> activeTasks = new Array<CrewTask>();
//	
//	private final Pool<CrewTask> taskPool = new Pool<CrewTask>(){
//	    @Override
//	    protected CrewTask newObject() {
//	        return new CrewTask();
//	    }
//	};
	
	
	protected Crew crew;
	private boolean isAssigned;
	private boolean isCompleted;
	private int priority;
	
	public CrewTask(){
		this.crew = null;
		this.isAssigned = false;
		this.isCompleted = false;
		this.priority = 0;
	}
	
	public void initPriority(int priority){
		this.priority = priority;
	}
	
	public int compareTo(CrewTask other) {
	    return Integer.compare(this.priority, other.priority);
	}
	
	public boolean assign(Crew crew){
		this.isAssigned = crew.assignTask(this);
		if(isAssigned){
			this.crew = crew;
		}
		return isAssigned;
	}
	
	public boolean isCompleted(){
		return isCompleted;
	}
	
	protected void setCompleted(boolean completed){
		this.isCompleted = completed;
	}
	
	/**
	 * Checks if crew is still working on assignment.
	 * @return
	 */
	public boolean checkAssigned(){
		if(crew != null){
			if(crew.getTask().equals(this)) return true;
			else{
				unAssign();
				return false;
			}
		}
		this.isAssigned = false;
		return false;
	}
	
	public void unAssign(){
		if(crew != null){
			crew.forceSetTask(null);
		}
		this.isAssigned = false;
		this.crew = null;
	}
	
	@Override
	public void reset() {
		unAssign();
		isCompleted = false;
		priority = 0;
	}
	
	/**
	 * Actual work the task requires takes place here.
	 * if the task is complete, return true.
	 * @return if task is complete
	 */
	public abstract boolean performTask();

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
}
