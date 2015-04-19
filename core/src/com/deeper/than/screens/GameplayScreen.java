package com.deeper.than.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deeper.than.DTL;
import com.deeper.than.EnemyShip;
import com.deeper.than.PlayerShip;
import com.deeper.than.Wall;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.Races;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SheildModule;
import com.deeper.than.ui.CrewPlate;
import com.deeper.than.ui.ReactorBar;
import com.deeper.than.ui.UIModuleReactorBar;
import com.deeper.than.ui.UISheildReacBar;
import com.deeper.than.ui.UITopBar;

public class GameplayScreen implements EnumerableScreen{
	
	public static ShapeRenderer shapeRen;
	public static Texture highlight;
	
	private DTL game;
	private Stage ui;
	
	private ReactorBar reactorBar;
	
	private ArrayList<UIModuleReactorBar> moduleReactorBars;
	
	private Stage gameObjects;
	private PlayerShip playerShip;
	private float timeAccumulator;
	private Crew selectedCrew;
	private EnemyShip enemy;
	
	private InputMultiplexer input;

	Image tempBackground;
	
	public void create(DTL game){
		this.game = game;

		loadAssets();
		
		
		
	}
	
	public void loadAssets(){
		tempBackground = new Image(new Texture("tempbackground.png"));
		Wall.loadAssets();
		Modules.loadAllModuleAssets();
		Races.loadAnims();
		shapeRen = new ShapeRenderer();
		highlight = new Texture(Gdx.files.internal("pixel.png"));
	}
	
	

	/**
	 * 
	 */
	private void initializeGame(){
		playerShip = new PlayerShip(Gdx.files.internal("ships/" + game.getSelectedShip() +".ship"), true, game, DTL.firstOpenId++);
		//playerShip.setOrigin(playerShip.getWidth()/2, playerShip.getHeight()/2);
		gameObjects = new Stage(game.getViewport());
		
		gameObjects.addActor(tempBackground);
		gameObjects.addActor(playerShip);
		enemy = new EnemyShip(Gdx.files.internal("ships/enemyships/scout.ship") , game, DTL.firstOpenId++);
		
		
		
		ui = new Stage(game.getViewport());
		
		Table uiT = new Table();
		uiT.setFillParent(true);
		uiT.add(new UITopBar(playerShip)).expandX().top().colspan(10);
		uiT.row();
		for(Crew c : playerShip.getCrew()){
			uiT.add(new CrewPlate(c)).prefWidth((Crew.CREW_HEIGHT/Crew.SCALE)+50+10).prefHeight(Crew.CREW_HEIGHT/Crew.SCALE+10).left().pad(1f).colspan(10);
			uiT.row();
		}
		
		uiT.add().expand().colspan(3);
		uiT.row();
		reactorBar = new ReactorBar(playerShip); 
		uiT.add(reactorBar).padLeft(5).bottom().left().prefHeight(game.getViewport().getWorldHeight()/2).prefWidth(ReactorBar.PREF_WIDTH).minWidth(ReactorBar.PREF_WIDTH);
		
		moduleReactorBars = new ArrayList<UIModuleReactorBar>();
		UISheildReacBar uis =new UISheildReacBar(2, reactorBar, (SheildModule)playerShip.getModule(SheildModule.class));
		moduleReactorBars.add(uis);
		uiT.add(uis).padLeft(10).bottom().left().minWidth(ReactorBar.PREF_WIDTH).prefHeight(uis.getPrefHeight()).fillY();
		
		uiT.add().prefWidth(10000000);
		uiT.row();
		uiT.add(new Label("tacos", DTL.skin)).bottom().left().colspan(10);
		
		
		ui.addActor(uiT);
		
		ui.setDebugAll(DTL.GRAPHICALDEBUG);
		
		input = new InputMultiplexer();
		input.addProcessor(ui);
		input.addProcessor(gameObjects);
		input.addProcessor(new InputProcessor() {
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				if(button == Buttons.RIGHT){
					setSelectedCrew(null);
				}
				return false;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean scrolled(int amount) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				if(character == 'a'){
					for(Crew c : playerShip.getCrew()){
						c.setHealth(c.getHealth() - 10);
					}
					//playerShip.damageSheilds();
					SheildModule s = (SheildModule)playerShip.getModule(SheildModule.class);
					s.setLevel(s.getLevel()+1);
				}
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		timeAccumulator = 0;
		selectedCrew = null;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("gameplay state ");
		
		if(DTL.gameActive != true){
			initializeGame();
		}
		DTL.gameActive = true;
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    ////Key inputs here
	    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
	    	mainMenu();
	    }
	

	    //////Update logic goes here
	    
	    timeAccumulator += delta;
	    if(timeAccumulator > DTL.getFrameTime()){
	    	for(UIModuleReactorBar mrb : moduleReactorBars){
	    		mrb.checkforSectionsChange();
	    	}
	    	playerShip.update();
	    	ui.act();
	    	gameObjects.act();
	    	timeAccumulator -= DTL.getFrameTime();
	    }
	    
	   ////Rendering goes here
	    gameObjects.draw();
	    ui.draw();
	    

	    
	}

	public void setSelectedCrew(Crew crew){
		if(getSelectedCrew() != null){
			getSelectedCrew().setSelected(false);
		}
		this.selectedCrew = crew;
	}
	
	public Crew getSelectedCrew(){
		return selectedCrew;
	}
	
	public boolean isCrewSelected(){
		if(selectedCrew == null){
			return false;
		}
		return true;
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
		//DTL.gameActive = false;
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
		playerShip.dispose();
		gameObjects.dispose();
	}
	
	/**
	 * Draws an empty rectangle within the area designated.
	 * Passing null for color uses already loaded color.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param lineThickness
	 * @param color
	 * @param batch
	 */
	public static void drawEmptyRectable(float x, float y, float width, float height, float lineThickness, Color color, Batch batch){
		Color returnColor = null;
		if(color != null){
			returnColor = batch.getColor().cpy();
			batch.setColor(color);
		}
		batch.draw(highlight, x, y, width, lineThickness);
		batch.draw(highlight, x, y, lineThickness, height);
		batch.draw(highlight, x+width-lineThickness, y, lineThickness, height);
		batch.draw(highlight, x, y+height-lineThickness, width, lineThickness);
		if(color != null){
			batch.setColor(returnColor);
		}
	}

}
