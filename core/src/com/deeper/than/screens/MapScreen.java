package com.deeper.than.screens;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.deeper.than.DTL;
import com.deeper.than.DTLMap;
import com.deeper.than.DTLMapPoint;
import com.deeper.than.MapGenerator;

public class MapScreen implements EnumerableScreen {

	
	
	private DTL game;
	
	private int currentNode;
	private int level;
	
	private Map<Integer, Integer> lattice;
	private ArrayList<DTLMapPoint> nodes;
	private DTLMap map;
	private MapGenerator mapGenerator;
	
	private ArrayList<Rectangle> points;
	
	private Texture unvisitedPoint = new Texture(Gdx.files.internal("u_point.png"));
	private Texture visitedPoint = new Texture(Gdx.files.internal("v_point.png"));
	private Texture currentPoint = new Texture(Gdx.files.internal("c_point.png"));
	
	@Override
	public void create(DTL game) {
		this.game=game;
	}	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 1.0f/((float) level), 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void initMap(){
		
		mapGenerator.generate();
		map = mapGenerator.getMap();
		nodes = map.getNodes();
		lattice = map.getLattice();
		level = map.getLevel();
		currentNode = 0;
		
	}
	

}
