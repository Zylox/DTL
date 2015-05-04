/**
 * Main gameplaye context. Gameplay logic all hooks in here, although a large portion takes place in ship.
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham and Nick Ferguson
 */
package com.deeper.than.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.deeper.than.DTL;
import com.deeper.than.EnemyShip;
import com.deeper.than.FloorTile;
import com.deeper.than.GridSquare;
import com.deeper.than.MapGenerator;
import com.deeper.than.PlayerShip;
import com.deeper.than.ShipLoadException;
import com.deeper.than.Wall;
import com.deeper.than.background.Background;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.Races;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;
import com.deeper.than.ui.UICrewPlateBar;
import com.deeper.than.ui.UICrewSkillsPlate;
import com.deeper.than.ui.UIEnemyWindow;
import com.deeper.than.ui.UIEventTable;
import com.deeper.than.ui.UIFastDrive;
import com.deeper.than.ui.UIMapScreen;
import com.deeper.than.ui.UIMapTable;
import com.deeper.than.ui.UIPauseButton;
import com.deeper.than.ui.UIPopUpWindow;
import com.deeper.than.ui.UIReactorRow;
import com.deeper.than.ui.UIRewardLabel;
import com.deeper.than.ui.UISecondaryTopBar;
import com.deeper.than.ui.UITopBar;
import com.deeper.than.ui.UIWeaponBottomBar;
import com.deeper.than.ui.UIWeaponCard;
import com.deeper.than.weapons.Projectile;
import com.deeper.than.weapons.WeaponGenerator;

/**
 * Screen where gameplay happens
 * @author zach
 *
 */
public class GameplayScreen implements EnumerableScreen{
	
	//for debuggin
	public static ShapeRenderer shapeRen;
	
	//1 white pixel for stretching and coloring to our needs
	public static Texture highlight;
	
	//reticule for aiming
	public static Sprite reticule;
	
	//all possible music
	//Music is by Rob cosh: https://soundcloud.com/rob-cosh/sets/ponder
	//Liscence: http://creativecommons.org/licenses/by-sa/3.0/
	public static final String musicHandles[] = {"music/Rob Cosh - PONDER - 03 Flow-01.wav",
		 										 "music/Rob Cosh - PONDER - 03 Flow-02.wav",
		 										 "music/Rob Cosh - PONDER - 03 Flow-03.wav",
												 "music/Rob Cosh - PONDER - 03 Flow-04.wav"};
	public static Music music;
	public static int musicCounter = 0;
	
	private DTL game;
	
	private Label evadeValue;
	
	private UIReactorRow playerReacs;
	private UICrewPlateBar crewPlateBar;
	
	//various ui elements
	private Stage ui;
	private Stage gameObjects;
	private Stage gameOverStage;
	private UIPopUpWindow<UIMapTable> mapTable;
	private UIPopUpWindow<UIEventTable> eventTable;
	private UIPopUpWindow<Table> gameOver;
	private PlayerShip playerShip;
	private UIFastDrive playerFastDrive;
	private float timeAccumulator;
	private Crew selectedCrew;
	private UIEnemyWindow enemyWindow;
	private WeaponGenerator weaponGen;
	private UIWeaponBottomBar bottomBar;
	private Label pausedLabel;
	private MapGenerator mapGenerator;
	
	//multiplexes input over multuple input proccesors
	private InputMultiplexer input;

	private boolean isPaused;
	private boolean eventWindow;
	
	
	private Background background;
	
	public void create(DTL game){
		this.game = game;

		loadAssets();
		weaponGen = new WeaponGenerator();
	}
	
	//loads neede assets
	public void loadAssets(){
		UIWeaponCard.loadAssets();
		background = new Background(new Texture("tempbackground.png"));
		background.setColor(Color.WHITE);
		highlight = new Texture(Gdx.files.internal("pixel.png"));
		UIPauseButton.loadAssets();
		UIRewardLabel.loadAssets();
		Wall.loadAssets();
		Modules.loadAllModuleAssets();
		UICrewSkillsPlate.loadAssets();
		UIFastDrive.loadAssets();
		Races.loadAnims();
		shapeRen = new ShapeRenderer();
		reticule = new Sprite(new Texture(Gdx.files.internal("targetreticule.png")));
		UIEnemyWindow.loadAssets();
		mapGenerator = new MapGenerator();
		UIMapScreen.loadAssets();
		
		music = Gdx.audio.newMusic(Gdx.files.internal(musicHandles[musicCounter]));
		music.setOnCompletionListener(new OnCompletionListener() {
			//auto loads next song/song section.
			@Override
			public void onCompletion(Music music) {
				musicCounter++;
				if(musicCounter >= musicHandles.length){
					musicCounter = 0;
				}
				music.dispose();
				music = Gdx.audio.newMusic(Gdx.files.internal(musicHandles[musicCounter]));
				music.play();
				music.setOnCompletionListener(this);
			}
		});
	}
	

	/**
	 * lays out the ui. looks messy, just the nature of ui's
	 */
	private void initializeGame(){
		mapGenerator.generate();
		DTL.MAP = mapGenerator.getMap();
		
		try {
			playerShip = new PlayerShip(Gdx.files.internal("ships/" + game.getSelectedShip() +".ship"), true, game, DTL.firstOpenId++, weaponGen);
		} catch (ShipLoadException e) {
			e.printStackTrace();
		}
		
		gameObjects = new Stage(game.getViewport());
		
		gameObjects.addActor(background);
		gameObjects.addActor(playerShip);
		shapeRen.setProjectionMatrix(gameObjects.getViewport().getCamera().combined);
		EnemyShip enemy = null;
		try {
			enemy = new EnemyShip(Gdx.files.internal("ships/enemyships/scout.ship") , game, DTL.firstOpenId++, playerShip, weaponGen);
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
		uiT.add(new UISecondaryTopBar(playerShip, DTL.VWIDTH, 20));
		uiT.row();
		Table tab = new Table();
		LabelStyle backgroundedLabel = new LabelStyle(DTL.skin.getFont("default-font"), Color.WHITE);
		backgroundedLabel.background = new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch);
		evadeValue = new Label("Evade: " + Float.toString(playerShip.getEvade()), backgroundedLabel);
		evadeValue.setFontScale(.6f);
		evadeValue.setAlignment(Align.center);
		tab.add(evadeValue).left().maxHeight(backgroundedLabel.font.getBounds("Evade: 0%").height).expand().padLeft(2);
		tab.add().prefWidth(1000000);
		uiT.add(tab).left();
		uiT.row();

		crewPlateBar = new UICrewPlateBar(playerShip.getCrew());
		uiT.add(crewPlateBar).expand().left().fill();
		uiT.add().expand();
		uiT.row();
		

		playerReacs = new UIReactorRow(playerShip, true);
		uiT.add(playerReacs).fill().expand().bottom().left();
		playerReacs.setupReactorBars();
		uiT.row();
		
		bottomBar = new UIWeaponBottomBar(playerReacs.getWeaponUI());
		bottomBar.setX(DTL.VWIDTH/3);
		bottomBar.setY(10);
		
		pausedLabel = new Label("Paused\n(press space)", DTL.skin);
		pausedLabel.setAlignment(Align.center);
		float pLabelWidth = 55;
		float pLabelHeight = 25;
		pausedLabel.setBounds(DTL.VWIDTH/2 - pLabelWidth/2, DTL.VHEIGHT/3f - pLabelHeight/2, pLabelWidth, pLabelHeight);
		ui.addActor(pausedLabel);
		
		ui.addActor(uiT);
		enemyWindow = new UIEnemyWindow(enemy);
		enemyWindow.setBounds(DTL.VWIDTH * 2/3, DTL.VHEIGHT/6, DTL.VWIDTH/4, DTL.VHEIGHT - DTL.VHEIGHT/6 * 2);
		enemyWindow.setUpTable();
		ui.addActor(enemyWindow);
		enemyWindow.getShip().refreshWeaponOrigins();
		ui.addActor(bottomBar);
		
		
		
		ui.setDebugAll(DTL.GRAPHICALDEBUG);
		
		//////Author: Nick
		UIEventTable uiEventTable = new UIEventTable("template");
		eventTable = new UIPopUpWindow<UIEventTable>(uiEventTable);
		uiEventTable.setParent(eventTable);
		uiEventTable.setUpUI();
		
		ui.addActor(eventTable);
		
		UIMapTable uiMapTable = new UIMapTable();
		mapTable = new UIPopUpWindow<UIMapTable>(uiMapTable);
		uiMapTable.setParent(mapTable);
		uiMapTable.setUpMapUI();
		mapTable.setVisible(false);
		
		ui.addActor(mapTable);
		//////
		
		//initialize gamge over but set it invisible
		Table goT = new Table();
		Label goL = new Label("Game Over",DTL.skin);
		TextButton goB = new TextButton("Main Menu", DTL.skin);
		goB.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				DTL.gameActive = false;
				mainMenu();
			}
		});
		goT.add(goL).expand();
		goT.row();
		goT.add(goB).expand();
		gameOver = new UIPopUpWindow<Table>(goT);
		gameOver.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				if(playerShip.getHealth() <= 0){
					gameOverScreen();
					return true;
				}
				return false;
			}
		});
		gameOver.setVisible(false);
		gameOverStage = new Stage(game.getViewport());
		gameOverStage.addActor(gameOver);
		//set our multiplexer for input and add input, in order of precedence
		input = new InputMultiplexer();
		input.addProcessor(ui);
		input.addProcessor(gameObjects);
		//input for various actions in gameplay
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
					//useful for debugging module levels
					//simulates progression
//					for(Module m : playerShip.getModules()){
//						m.setLevel(m.getLevel()+1);
//					}
				}
				
				//pauses on space
				if(character == ' '){
					setPaused(!isPaused());
				}
			
				/////author nick
				if(character=='m'){
					//mapTable.clear();
					UIMapTable uiMapTable = new UIMapTable();
					mapTable = new UIPopUpWindow<UIMapTable>(uiMapTable);
					uiMapTable.setParent(mapTable);
					uiMapTable.setUpMapUI();
					drawMap();
				}
				/////
				
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
		setPaused(true);
		eventWindow = true;
	}
	
	public static Vector2 getStageLocation(Actor actor) {
	    return actor.localToStageCoordinates(new Vector2(0, 0));
	}
	
	/**
	 * Called upon screen showing
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("gameplay state ");
		
		if(DTL.gameActive != true){
			initializeGame();
		}
		DTL.gameActive = true;
		Gdx.input.setInputProcessor(input);
		music.play();
	}

	//Render update loop
	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    //frame tied key inputs here
	    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
	    	mainMenu();
	    }
	
	    //////Update logic goes here
	    //updates once per frametime propritonally to time passed
	    timeAccumulator += delta;
	    if(timeAccumulator > DTL.getFrameTime()){
	    	///Author nick
	    	if(!eventWindow){
	    		eventTable.clear();
	    	}
	    	/////
	    	playerReacs.update();
	    	playerShip.update();
	    	if(enemyWindow != null){
	    		enemyWindow.update();
	    	}
	    	playerFastDrive.update(true);
	    	evadeValue.setText("Evade: " + Integer.toString((int)(playerShip.getEvade()*100)) + "%");
	    	
	    	gameObjects.act();
	    	ui.act();
	    	gameOverStage.act();
	    	crewPlateBar.update();
	    	timeAccumulator -= DTL.getFrameTime();
	    	if(enemyWindow != null && enemyWindow.getShip().getHealth() <=0){
	    		enemyWindow.remove();
	    		enemyWindow = null;
	    		bottomBar.clearTargets();
	    	}
	    }

	   ////Rendering goes here
	    gameObjects.draw();
	    ui.draw();
	    gameOverStage.draw();

	    //pathfinding visualization used for debug
	    if(DTL.GRAPHICALDEBUG || DTL.PATHDEBUG){
		    shapeRen.begin(ShapeType.Line);
		    shapeRen.setColor(Color.RED);
		    Vector2 pos = new Vector2();
		    Vector2 end = new Vector2();
		    GridSquare sq = null;
		    GridSquare sq2 = null;
		    for(int i = 0; i<playerShip.getLayout().length;i++){
		    	for(int j = 0; j<playerShip.getLayout()[0].length;j++){
		    		if(playerShip.getLayout()[i][j] != null){
		    			sq = playerShip.getLayout()[i][j];
		    			sq2 = sq.getPathPointer();
		    			pos.x = playerShip.getX() + sq.getPos().x * FloorTile.TILESIZE + FloorTile.TILESIZE/2;
		    			pos.y = playerShip.getY() + sq.getPos().y * FloorTile.TILESIZE + FloorTile.TILESIZE/2;
		    			end.x = playerShip.getX() + sq2.getPos().x * FloorTile.TILESIZE + FloorTile.TILESIZE/2;
		    			end.y = playerShip.getY() + sq2.getPos().y * FloorTile.TILESIZE + FloorTile.TILESIZE/2;
		    			shapeRen.line(pos, end);
		    		}
			    }	
		    }
		    shapeRen.end();
	    }
	    

	    
	}
	
	public void addProjectile(Projectile projectile){
		ui.addActor(projectile);
	}
	
	/**
	 * Sets gameOver screen to display
	 * also blocks input to other things
	 */
	public void gameOverScreen(){
		input.clear();
		input.addProcessor(gameOverStage);
		gameOver.setVisible(!gameOver.isVisible());
	}

	/**
	 * Set a crew to the selected one
	 * @param crew
	 */
	public void setSelectedCrew(Crew crew){
		if(getSelectedCrew() != null){
			getSelectedCrew().setSelected(false);
		}
		this.selectedCrew = crew;
	}
	
	public UIEnemyWindow getEnemyWindow(){
		return enemyWindow;
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
	
	/**
	 * Returns if the player is targettting
	 * @return
	 */
	public boolean isTargetting(){
		if(bottomBar != null){
			return bottomBar.getSelected() != null; 
		}
		return false;
	}
	
	public UIWeaponCard getTargetting(){
		if(bottomBar != null){
			return bottomBar.getSelected();
		}
		return null;
	}
	
	//Called upon resize
	@Override
	public void resize(int width, int height) {
		gameObjects.getViewport().update(width, height, true);
		game.setViewport(gameObjects.getViewport());
		playerShip.refreshWeaponOrigins();
		enemyWindow.getShip().refreshWeaponOrigins();
	}

	@Override
	public void pause() {
		setPaused(true);
	}
	
	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		pausedLabel.setVisible(isPaused);
		this.isPaused = isPaused;
	}

	@Override
	public void resume() {
		
	}
	
	public void mainMenu(){
		game.setScreen(Screens.MAINMENU.getScreen());
	}

	@Override
	public void hide() {
		DTL.previousScreen = this;
		music.pause();
	}

	@Override
	public void dispose() {
		playerShip.dispose();
		gameObjects.dispose();
		music.dispose();
		
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
	
	/**
	 * @author nick
	 */
	public void drawMap(){
		if(!mapTable.isVisible()){
			pause();
			mapTable.setVisible(true);
		} else {
			mapTable.setVisible(false);
			setPaused(false);
		}
	}
	
	public WeaponGenerator getWeaponGenerator(){
		return weaponGen;
	}
}
