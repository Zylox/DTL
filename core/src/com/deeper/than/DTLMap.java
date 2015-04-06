package com.deeper.than;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DTLMap {
	//list of every point on the current map
	private ArrayList<DTLMapPoint> nodes = new ArrayList<DTLMapPoint>();
	//current level, starts at 1
	private int level;
	//lattice of connection of nodes, not set up until nodes are set up
	private Map<Integer,Integer> lattice = new HashMap<Integer,Integer>();
	
	public DTLMap(int level){
		setLevel(level);
		nodes.clear();
		lattice.clear();
	}
	
	public int getLevel(){
		return level;
	}
	
	public ArrayList<DTLMapPoint> getNodes(){
		return nodes;
	}
	
	public Map<Integer,Integer> getLattice(){
		return lattice;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void addNode(DTLMapPoint node){
		nodes.add(node);
	}
	
	public void addConnection(int start, int end){
		lattice.put(start,end);
	}
}
