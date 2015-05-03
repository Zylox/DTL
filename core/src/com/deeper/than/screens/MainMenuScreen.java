package com.deeper.than.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
	private TextButton playButton;
	private TextButton newGameButton;
	private TextButton optionsButton;
	private TextButton exitButton;
	
	private InputMultiplexer input;

	private boolean loadtest = true;
	
	public MainMenuScreen(){

	}
	
	public void create(DTL game){
		this.game = game;
		stage = new Stage(game.getViewport());
		
		input = new InputMultiplexer();
		input.addProcessor(stage);
		Gdx.input.setInputProcessor(input);
		
		table = new Table();
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("title_screen2.1.png")))));
		table.setFillParent(true);
		stage.addActor(table);
		
		playButton = new TextButton("Continue", DTL.skin);
		playButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	play();
		    }
		});
		
		newGameButton = new TextButton("New Game", DTL.skin);
		newGameButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	newGame();
		    }
		});
		optionsButton = new TextButton("Options", DTL.skin);
		optionsButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	options();
		    }
		});
		exitButton = new TextButton("Exit", DTL.skin);
		exitButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	Gdx.app.exit();
		    }
		});
		
		configureTable(false);
		
		
		table.setDebug(DTL.GRAPHICALDEBUG);
		
		loadAssets();
	}
	
	public void loadAssets(){
		
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("Main Menu State");
		Gdx.input.setInputProcessor(input);
		if(DTL.gameActive){
			configureTable(true);
		}
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    if(loadtest){

	    	long newTime = System.currentTimeMillis()-DTL.startTime;
	    	DTL.printDebug("Load took: " + newTime/1000f + " seconds" );
	    	loadtest = false;
	    }
	    
	    stage.act(delta);
	    
	    stage.draw();
	    
	}

	@Override
	public void resize(int width, int height) {
		
		//stage2.getViewport().update(width, height, false);
		stage.getViewport().update(width, height, true);
		game.setViewport(stage.getViewport());
	}

	public void play(){
		//sends to the GameplayScreen
//		if(DTL.gameActive){
//			game.setScreen(Screens.GAMEPLAY.getScreen());			
//		}else{
//			game.setScreen(Screens.NEWGAME.getScreen());
//		}
		game.setScreen(Screens.GAMEPLAY.getScreen());
	}
	
	public void newGame(){
		Screens.GAMEPLAY.create(game);
		DTL.gameActive = false;
		game.setScreen(Screens.NEWGAME.getScreen());
	}
	
	private void configureTable(boolean continueB){
		table.clear();
		table.add().expandX();
		table.add().expand().height(400);
		table.add().expandX();
		table.row();
		if(continueB){
			table.add().expandX();
			table.add(playButton).width(300).height(50).pad(10);
			table.add().expandX();
			table.row();
		}
		table.add().expandX();
		table.add(newGameButton).width(300).height(50).pad(10);
		table.add().expandX();
		table.row();
		table.add().expandX();
		table.add(optionsButton).width(300).height(50).pad(10);
		table.add().expandX();

		table.row();
		table.add().expandX();
		table.add(exitButton).width(300).height(50).pad(10);
		table.add().expandX();
		table.row();
		table.add().expandX();
		table.row();
		table.add().expandX();
		table.add().expand().height(300);
		table.add().expandX();
	}
	
	public void goToShipBuilder(){
		//game.setScreen(Screens.SHIPBUILDER.getScreen());
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
		DTL.previousScreen = this;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		shapeRen.dispose();
	}
	
}
