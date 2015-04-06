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
		this.resetMap();
		level++;
		map = new DTLMap(level);
		
		//this little block declares how many nodes will be on this map. minPoints is the minimum number of
		//nodes that can be generated, and minPoints+variance is the max number of nodes that can be generated
		double minPoints = 20.0;
		double variance = 10.0;
		int size = (int) ( (variance * Math.random()) + minPoints );
		
		//this for loop generates points, and adds them to the DTLMap object
		for(int i = 0; i < size; i++){
			String event;
			int x,y,xMax,yMax,xMin,yMin;
			
			//this block decides which event to assign to a node
			//this should be mostly random, with the Welcome_to_DTL.event added for the very first node of the game
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
	
	public void resetMap(){
		map = null;
	}
}
