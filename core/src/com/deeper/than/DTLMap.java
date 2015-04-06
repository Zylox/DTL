package com.deeper.than;

import java.util.ArrayList;

public class DTLMap {
	private ArrayList<DTLMapPoint> nodes = new ArrayList<DTLMapPoint>();
	private int level;
	
	public DTLMap(int level){
		setLevel(level);
	}
	
	public int getLevel(){
		return level;
	}
	
	public ArrayList<DTLMapPoint> getNodes(){
		return nodes;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void addNode(DTLMapPoint node){
		nodes.add(node);
	}
}
