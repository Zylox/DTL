package com.deeper.than.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deeper.than.DTL;

public class MainMenuScreen implements EnumerableScreen {

	private DTL game;
	private Stage stage;
	private Table table;
	
	private ShapeRenderer shapeRen;
	TextButton playButton;
	TextButton optionsButton;
	TextButton exitButton;
	Skin skin;
	
	public MainMenuScreen(){

	}
	
	public void create(DTL game){
		this.game = game;
		stage = new Stage(game.getViewport());
		Gdx.input.setInputProcessor(stage);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		
		table = new Table();
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("mainMenuBackground.png")))));
		table.setFillParent(true);
		stage.addActor(table);
		
		playButton = new TextButton("Play Game", skin);
		playButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	play();
		    }
		});
		optionsButton = new TextButton("Options", skin);
		optionsButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	options();
		    }
		});
		exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	Gdx.app.exit();
		    }
		});
		
		table.add().height(300);
		table.row();
		table.add(playButton).width(300).height(50).pad(10);
		table.row();
		table.add(optionsButton).width(300).height(50).pad(10);
		table.row();
		table.add(exitButton).width(300).height(50).pad(10);
		table.row();
		table.add().expandY();

		table.setDebug(DTL.DEBUG);
		
		shapeRen = new ShapeRenderer();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("ima back from another state");
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    stage.act(delta);
	    stage.draw();
	    
	}

	@Override
	public void resize(int width, int height) {
		
		stage.getViewport().update(width, height, true);
		game.setViewport(stage.getViewport());
	}

	public void play(){
		//sends to the GameplayScreen
		if(DTL.gameActive){
			game.setScreen(Screens.GAMEPLAY.getScreen());			
		}else{
			game.setScreen(Screens.NEWGAME.getScreen());
		}
	}
	
	public void options(){
		//sends to the OptionScreen
		game.setScreen(Screens.OPTIONS.getScreen());
	}
	
	@Override
	public void pause() {
		// When context is lost it goes here. Used to pause important things
	}

	@Override
	public void resume() {
		//Use this to restart things that stop when context is lost
	}

	@Override
	public void hide() {
		//When the screen is set to something else, this gets called first
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		shapeRen.dispose();
	}
	
}
