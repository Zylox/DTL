package com.deeper.than.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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

public class GameplayScreen implements EnumerableScreen{
	
	public static ShapeRenderer shapeRen;
	public static Texture highlight;
	public static Sprite reticule;
	
	private DTL game;
	
	private Label evadeValue;
	
	private UIReactorRow playerReacs;
	private UICrewPlateBar crewPlateBar;
	
	
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
	
	private InputMultiplexer input;

	private boolean isPaused;
	private boolean eventWindow;
	
	
	Background tempBackground;
	
	public void create(DTL game){
		this.game = game;

		loadAssets();
		weaponGen = new WeaponGenerator();
	}
	
	public void loadAssets(){
		UIWeaponCard.loadAssets();
		tempBackground = new Background(new Texture("tempbackground.png"));
		tempBackground.setColor(Color.WHITE);
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

		
	}
	

	/**
	 * 
	 */
	private void initializeGame(){
		DTL.MAP.setAdvance(false);
		
		try {
			playerShip = new PlayerShip(Gdx.files.internal("ships/" + game.getSelectedShip() +".ship"), true, game, DTL.firstOpenId++, weaponGen);
		} catch (ShipLoadException e) {
			e.printStackTrace();
		}
		
		gameObjects = new Stage(game.getViewport());
		
		gameObjects.addActor(tempBackground);
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
		
		UIEventTable uiEventTable = new UIEventTable(DTL.MAP.getNodes().get(DTL.MAP.getCurrentNode()).getEvent());
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
					playerShip.setCurrency(playerShip.getCurrency()+100);
					//playerShip.damageSheilds();
//					SheildModule s = (SheildModule)playerShip.getModule(SheildModule.class);
//					s.setLevel(s.getLevel()+1);
					
					for(Module m : playerShip.getModules()){
						m.setLevel(m.getLevel()+1);
					}
				}
				
				if(character == ' '){
					setPaused(!isPaused());
				}
				
				if(character == 'g'){
					gameOverScreen();
				}
				
				if(character=='m'){
					//mapTable.clear();
					//UIMapTable uiMapTable = new UIMapTable();
					//mapTable = new UIPopUpWindow<UIMapTable>(uiMapTable);
					//uiMapTable.setParent(mapTable);
					//uiMapTable.setUpMapUI();
					drawMap();
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
		setPaused(true);
		eventWindow = true;
	}
	
	public static Vector2 getStageLocation(Actor actor) {
	    return actor.localToStageCoordinates(new Vector2(0, 0));
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("gameplay state ");
		mapGenerator.generate();
		DTL.MAP = mapGenerator.getMap();
		
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
	    	if(!eventWindow){
	    		eventTable.clear();
	    	}
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
//	    	eventStage.act();
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
		    			//shapeRen.line(playerShip.getLayout()[i][j].getPos(), new Vector2(100,100));
		    		}
			    }	
		    }
		    shapeRen.end();
	    }
	    
	    if(DTL.MAP.getAdvance()) initializeGame();
	    
	}
	
	public void addProjectile(Projectile projectile){
		ui.addActor(projectile);
	}
	
	public void gameOverScreen(){
		input.clear();
		input.addProcessor(gameOverStage);
		gameOver.setVisible(!gameOver.isVisible());
	}

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
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gameObjects.getViewport().update(width, height, true);
		game.setViewport(gameObjects.getViewport());
		playerShip.refreshWeaponOrigins();
		enemyWindow.getShip().refreshWeaponOrigins();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
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
