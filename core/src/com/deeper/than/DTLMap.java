package com.deeper.than;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DTLMap {
	//list of every node on the current map
	private ArrayList<DTLMapPoint> nodes = new ArrayList<DTLMapPoint>();
	//current level, first is 1
	private int level;
	//lattice of connection of nodes, not set up until after nodes are set up
	private Map<Integer,ArrayList<Integer>> lattice = new HashMap<Integer,ArrayList<Integer>>();
	private int currentNode;
	private boolean advance;
	
	public DTLMap(int level){
		setLevel(level);
		nodes.clear();
		lattice.clear();
		setCurrentNode(0);
		advance = true;
	}
	
	public int getLevel(){
		return level;
	}
	
	public ArrayList<DTLMapPoint> getNodes(){
		return nodes;
	}
	
	public Map<Integer,ArrayList<Integer>> getLattice(){
		return lattice;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void addNode(DTLMapPoint node){
		nodes.add(node);
	}
	
	public void addConnection(int start, int end){
		ArrayList<Integer> currentList = lattice.get(start);
		if(currentList == null) currentList = new ArrayList<Integer>();
		currentList.add(end);
		lattice.put(start, currentList);
	}
	
	public void setCurrentNode(int currentNode){
		this.currentNode = currentNode;
	}
	
	public int getCurrentNode(){
		return currentNode;
	}
	
	public int getSize(){
		return nodes.size();
	}
	
	public ArrayList<Integer> getReachableNodes(){
		return lattice.get(currentNode);
	}
	
	public boolean getAdvance(){
		return advance;
	}
	
	public void setAdvance(boolean advance){
		this.advance = advance;
	}
}
