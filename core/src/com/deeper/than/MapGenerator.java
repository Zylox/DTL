package com.deeper.than;

public class MapGenerator {
	private int level;
	private DTLMap map;
	
	public MapGenerator(){
		this.resetLevel();
		this.resetMap();
	}
	
	public void generate(){
		//this block initializes the map object
		resetMap();
		level++;
		map = new DTLMap(level);
		
		//this block declares how many nodes will be on this map. minPoints is the minimum number of nodes
		//that can be generated, and minPoints+variance is the max number of nodes that can be generated
		double minPoints = 20.0;
		double variance = 10.0;
		int size = (int) ( (variance * Math.random()) + minPoints );
		
		//this for loop generates points, and adds them to the DTLMap object
		for(int i = 0; i < size; i++){
			String event;
			int x,y,xMax,yMax,xMin,yMin;
			
			//this block decides which event to assign to a node
			//this should be mostly random, with the Welcome_to_DTL.event added to the very first node of the game
			if(i==0 && level==1)
				event="Welcome_to_DTL.event";
			else{
				//TODO add logic to chose random events
				event="template.event";
			}
			
			//if this is the first node on the map, keep it in the upper left corner
			//if it's the last node, keep it in the bottom right corner
			//else it can go anywhere on the screen
			if(i==0){
				xMax=200;
				yMax=200;
				xMin=0;
				yMin=0;
			} else if (i == size-1){
				xMin=1080;
				yMin=520;
				xMax=1280;
				yMax=720;
			} else {
				xMin=0;
				yMin=0;
				xMax=1280;
				yMax=720;
			}
			x = (int) ((Math.random()*(xMax-xMin))+xMin);
			y = (int) ((Math.random()*(yMax-yMin))+yMin);
			
			//add the node to the map
			map.addNode(new DTLMapPoint(x,y,event));
			
			//generates the connections between all the nodes
			generateLattice();
		}
		
		
	}
	
	public void generateLattice(){
		//how far away things should and still be connected
		int range=50;
		int size=map.getNodes().size();
		
		for(int i=0; i<size ;i++){
			int ix = map.getNodes().get(i).getX();
			int iy = map.getNodes().get(i).getY();
			//keep track if a node is orphaned or not
			boolean connected=false;
			
			for(int j=i+1; j<size ;j++){
				int jx = map.getNodes().get(j).getX();
				int jy = map.getNodes().get(j).getY();
				
				//if two nodes are within range of each other connect them
				if(Math.abs(ix-jx) < range && Math.abs(iy-jy) < range){
					connected=true;
					map.addConnection(i, j);
					map.addConnection(j, i);
				}
			}
			//if a node is orphaned, connect it to its nearest neighbor
			if(!connected){
				//nearest is the physically nearest node, distance is how many units away it is
				int nearest=0;
				int distance=Integer.MAX_VALUE;
				
				//find the nearest neighbor
				for(int j=0;j<size;j++){
					int jx = map.getNodes().get(j).getX();
					int jy = map.getNodes().get(j).getY();
					
					//logic for keeping track of the nearest neighbor, distance starts at MAX_VALUE,
					//so it is guaranteed to be entered at least once
					if(Math.abs(ix-jx)+Math.abs(iy-jy)<distance){
						distance=Math.abs(ix-jx)+Math.abs(iy-jy);
						nearest=j;
					}
				}
				map.addConnection(i, nearest);
				map.addConnection(nearest, i);
			}
		}
	}
	
	public int getLevel(){
		return level;
	}
	
	public DTLMap getMap(){
		return map;
	}
	
	public void resetLevel(){
		this.level=0;
	}
	
	private void resetMap(){
		map = null;
	}
}
