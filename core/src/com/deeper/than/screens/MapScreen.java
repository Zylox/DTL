package com.deeper.than.screens;

import com.deeper.than.DTL;
import com.deeper.than.DTLMap;
import com.deeper.than.MapGenerator;

public class MapScreen implements EnumerableScreen {

	private DTL game;
	
	private DTLMap map;
	private MapGenerator mapGenerator;
	
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

	private void initMap(){
		mapGenerator.generate();
		map = mapGenerator.getMap();
	}
	

}
