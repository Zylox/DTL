package com.deeper.than.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.deeper.than.DTL;

public class GameplayScreen implements EnumerableScreen{

	private DTL game;
	
	public void create(DTL game){
		this.game = game;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("gameplay state ");
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
	    	mainMenu();
	    }
	    
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
	
	public void mainMenu(){
		game.setScreen(Screens.MAINMENU.getScreen());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
