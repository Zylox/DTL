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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.DTL;
import com.deeper.than.EnemyShip;
import com.deeper.than.PlayerShip;
import com.deeper.than.ShipLoadException;
import com.deeper.than.Wall;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.CrewGoToRoomTask;
import com.deeper.than.crew.Races;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.ClimateControlModule;
import com.deeper.than.modules.CloakingModule;
import com.deeper.than.modules.EngineModule;
import com.deeper.than.modules.HatchControlModule;
import com.deeper.than.modules.MedbayModule;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SensorsModule;
import com.deeper.than.modules.SheildModule;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.ui.CrewPlate;
import com.deeper.than.ui.ReactorBar;
import com.deeper.than.ui.UIClimateControlReacBar;
import com.deeper.than.ui.UICloakingReacBar;
import com.deeper.than.ui.UICrewPlateBar;
import com.deeper.than.ui.UICrewSkillsPlate;
import com.deeper.than.ui.UIEnemyWindow;
import com.deeper.than.ui.UIEngineReacBar;
import com.deeper.than.ui.UIFastDrive;
import com.deeper.than.ui.UIMedbayReacBar;
import com.deeper.than.ui.UIModuleReactorBar;
import com.deeper.than.ui.UIModuleSyncable;
import com.deeper.than.ui.UIPowerBar;
import com.deeper.than.ui.UIReactorRow;
import com.deeper.than.ui.UISheildReacBar;
import com.deeper.than.ui.UITopBar;

public class GameplayScreen implements EnumerableScreen{
	
	public static ShapeRenderer shapeRen;
	public static Texture highlight;
	
	private DTL game;
	private Stage ui;
	
	private Label evadeValue;
	
	private UIReactorRow playerReacs;
	private UICrewPlateBar crewPlateBar;
	
	
	private Stage gameObjects;
	private PlayerShip playerShip;
	private UIFastDrive playerFastDrive;
	private float timeAccumulator;
	private Crew selectedCrew;
	private UIEnemyWindow enemyWindow;
	
	private InputMultiplexer input;

	Image tempBackground;
	
	public void create(DTL game){
		this.game = game;

		loadAssets();
		
		
		
	}
	
	public void loadAssets(){
		tempBackground = new Image(new Texture("tempbackground.png"));
		highlight = new Texture(Gdx.files.internal("pixel.png"));
		Wall.loadAssets();
		Modules.loadAllModuleAssets();
		UICrewSkillsPlate.loadAssets();
		UIFastDrive.loadAssets();
		Races.loadAnims();
		shapeRen = new ShapeRenderer();
		UIEnemyWindow.loadAssets();
	}
	

	/**
	 * 
	 */
	private void initializeGame(){
		try {
			playerShip = new PlayerShip(Gdx.files.internal("ships/" + game.getSelectedShip() +".ship"), true, game, DTL.firstOpenId++);
		} catch (ShipLoadException e) {
			e.printStackTrace();
		}
		gameObjects = new Stage(game.getViewport());
		
		gameObjects.addActor(tempBackground);
		gameObjects.addActor(playerShip);
		EnemyShip enemy = null;
		try {
			enemy = new EnemyShip(Gdx.files.internal("ships/enemyships/scout.ship") , game, DTL.firstOpenId++, playerShip);
		} catch (ShipLoadException e) {
			e.printStackTrace();
		}
		
		
		ui = new Stage(game.getViewport());
		
		Table uiT = new Table();
		uiT.setFillParent(true);
		Table topBar = new Table();
		topBar.add(new UITopBar(playerShip, DTL.VWIDTH/2, false)).top();
		playerFastDrive = new UIFastDrive(playerShip);
		int width = 90;
		int height = 40;
		playerFastDrive.setBounds(DTL.VWIDTH/2 - width/2, DTL.VHEIGHT-height, width, height);
		ui.addActor(playerFastDrive);
		
		topBar.add().prefWidth(1000000);
		uiT.add(topBar);
		uiT.row();
		Table tab = new Table();
		LabelStyle backgroundedLabel = new LabelStyle(DTL.skin.getFont("default-font"), Color.WHITE);
		backgroundedLabel.background = new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch);
		evadeValue = new Label("Evade: " + Float.toString(playerShip.getEvade()), backgroundedLabel);
		evadeValue.setFontScale(.6f);
		tab.add(evadeValue).left().maxHeight(backgroundedLabel.font.getBounds("Evade: 0%").height).expand().padLeft(2);
		tab.add().prefWidth(1000000);
		uiT.add(tab).left();
		uiT.row();
//		crewPlates = new ArrayList<CrewPlate>();
//		CrewPlate crewPlate;
//		for(Crew c : playerShip.getCrew()){
//			crewPlate = new CrewPlate(c);
//			crewPlates.add(crewPlate);
//			uiT.add(crewPlate).prefWidth((Crew.CREW_HEIGHT/Crew.SCALE)+50+10).prefHeight(Crew.CREW_HEIGHT/Crew.SCALE+10).left().pad(1f);
//			uiT.row();
//		}
		crewPlateBar = new UICrewPlateBar(playerShip.getCrew());
		uiT.add(crewPlateBar).expand().left().fill();
		uiT.add().expand();
		uiT.row();
		

		playerReacs = new UIReactorRow(playerShip, true);
		uiT.add(playerReacs).fill().expand().bottom().left();
		playerReacs.setupReactorBars();
		uiT.row();
	
		Label tacos = new Label("tacos", DTL.skin);
		//tacos.setFontScale(.4f);
		uiT.add(tacos).bottom().left();
		
		ui.addActor(uiT);
		
		enemyWindow = new UIEnemyWindow(enemy);
		enemyWindow.setBounds(DTL.VWIDTH * 2/3, DTL.VHEIGHT/6, DTL.VWIDTH/4, DTL.VHEIGHT - DTL.VHEIGHT/6 * 2);
		enemyWindow.setUpTable();
		ui.addActor(enemyWindow);
		
		
		
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
				if(button == Buttons.LEFT){
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
//					SheildModule s = (SheildModule)playerShip.getModule(SheildModule.class);
//					s.setLevel(s.getLevel()+1);
					
					for(Module m : playerShip.getModules()){
						m.setLevel(m.getLevel()+1);
					}
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
	    	playerReacs.update();
	    	playerShip.update();
	    	if(enemyWindow != null){
	    		enemyWindow.update();
	    	}
	    	playerFastDrive.update(true);
	    	evadeValue.setText("Evade: " + Integer.toString((int)(playerShip.getEvade()*100)) + "%");
	    	ui.act();
	    	gameObjects.act();
	    	crewPlateBar.update();
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
