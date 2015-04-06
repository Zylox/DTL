package com.deeper.than;

public class DTLMapPoint {
	//x and y coords of where on the map the point will be
	private int x;
	private int y;
	//The filename of the Event that will be triggered by going to this point
	private String event;
	//false by default, true if player has been to node already
	private boolean visited;
	
	public DTLMapPoint(int x, int y, String event){
		setVisited(false);
		setX(x);
		setY(y);
		setEvent(event);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}
