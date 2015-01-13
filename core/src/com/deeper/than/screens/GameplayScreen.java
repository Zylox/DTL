package com.deeper.than.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deeper.than.DTL;
import com.deeper.than.Ship;
import com.deeper.than.Wall;

public class GameplayScreen implements EnumerableScreen{

	private DTL game;
	private Stage ui;
	private Stage gameObjects;
	private Ship ship;
	private float timeAccumulator;
	
	
	private InputMultiplexer input;

	Image tempBackground;
	
	public void create(DTL game){
		this.game = game;
		Wall.loadAssets();
		loadAssets();
		ship = new Ship(Gdx.files.internal("kes.ship"), game);
		ship.setOrigin(ship.getWidth()/2, ship.getHeight()/2);
		gameObjects = new Stage(game.getViewport());
		gameObjects.addActor(tempBackground);
		gameObjects.addActor(ship);
		input = new InputMultiplexer();
		input.addProcessor(gameObjects);
		
		timeAccumulator = 0;
	}
	
	public void loadAssets(){
		tempBackground = new Image(new Texture("tempbackground.png"));
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("gameplay state ");
		DTL.gameActive = true;
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
	    	mainMenu();
	    }

	    
	    timeAccumulator += delta;
	    if(timeAccumulator > DTL.getFrameTime()){
	    	ship.update();
	    	gameObjects.act();
	    	timeAccumulator -= DTL.getFrameTime();
	    }
	    
	    gameObjects.draw();
	    

	    
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gameObjects.getViewport().update(width, height, true);
		game.setViewport(gameObjects.getViewport());
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
		DTL.gameActive = false;
		game.setScreen(Screens.MAINMENU.getScreen());
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		DTL.previousScreen = this;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		gameObjects.dispose();
		
	}

}
