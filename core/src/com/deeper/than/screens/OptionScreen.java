package com.deeper.than.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.deeper.than.DTL;

public class OptionScreen implements EnumerableScreen {

	private ShapeRenderer shapeRen;
	
	private DTL game;
	private Stage menu;
	
	public void create(DTL game){
		this.game = game;
		menu = new Stage(game.getViewport());
		
		Table table = new Table();
		table.setFillParent(true);
		TextButton fullscreen = new TextButton("FullScreen On/Off", DTL.skin);
		fullscreen.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	fullscreenToggle();
		    }
		});
		table.add(fullscreen);
		menu.addActor(table);
		
		
		shapeRen = new ShapeRenderer();
		table.setDebug(DTL.DEBUG);
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("Option Screen");
		Gdx.input.setInputProcessor(menu);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
	    	mainMenu();
	    }
	    
	    
		menu.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		menu.getViewport().update(width, height, true);
		game.setViewport(menu.getViewport());
	}
	
	public void fullscreenToggle(){
		if(Gdx.graphics.isFullscreen()){
			Gdx.graphics.setDisplayMode(DTL.VWIDTH, DTL.VHEIGHT, false);
			System.out.println("get out of fullscreen");
		}else{
			Gdx.graphics.setDisplayMode(DTL.VWIDTH, DTL.VHEIGHT, true);	
			System.out.println("set to fullscreen");
		}
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
		game.setScreen(DTL.previousScreen);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		DTL.previousScreen = this;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		menu.dispose();
		shapeRen.dispose();
	}

}
