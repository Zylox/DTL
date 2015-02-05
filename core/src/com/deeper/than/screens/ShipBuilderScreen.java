package com.deeper.than.screens;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.DTL;
import com.deeper.than.Neighbors;
import com.deeper.than.Wall;
import com.deeper.than.modules.Modules;
import com.deeper.than.shipbuilder.ShipPartContainer;

public class ShipBuilderScreen implements EnumerableScreen{

	private static final String BASEDIR = "ships";
	
	private enum DrawMode{
		rooms,
		doors,
		shipControl,
		modules;
		
		public String toString(){

			if(this == rooms){
				return "roomMode";
			}else if(this == shipControl){
				return "shipControlMode";
			}else if(this == doors){
				return "doorsMode";
			}else if(this == modules){
				return "modulesMode";
			}
			return "unknown";
		}
	}
	
	private DrawMode drawMode = DrawMode.rooms; 
	
	private DTL game;
	private Stage ui;
	private Stage gameObjects;
	private float timeAccumulator;
	private ShipPartContainer shipParts;
	
	private TextField nameValue;
	private TextField xValue;
	private TextField yValue;
	
	private SelectBox<String> modulesBox;
	private SelectBox<DrawMode> drawModeSelect;
	private SelectBox<String> shipSelect;
	
	private Label drawModeLabel;
	
	private Vector2 click1Pos;
	private boolean clicking;
	private int touchDownButton;
	
	private InputMultiplexer input;

	Image tempBackground;
	
	private void loadNewShip(String shipName){
		//shipParts = new ShipPartContainer(game, gameObjects);
		shipParts.loadNewShip(Gdx.files.internal("ships/"+shipName+".ship"));
		
		xValue.setText(shipParts != null ? Integer.toString(shipParts.getXdim()) : "10");
		yValue.setText(shipParts != null ? Integer.toString(shipParts.getYdim()) : "10");
		nameValue.setText(shipParts.getName());
		
	}
	
	public void create(DTL game){
		this.game = game;

		loadAssets();
		
		gameObjects = new Stage(game.getViewport());
		ui = new Stage(game.getViewport());
		input = new InputMultiplexer();
		input.addProcessor(new ClickManager());
		input.addProcessor(ui);
		input.addProcessor(gameObjects);
		
		shipParts = new ShipPartContainer(game, gameObjects);
		Table table = new Table(DTL.skin);
		table.setFillParent(true);
		ui.addActor(table);
		
		Label nameLabel = new Label("Name= ", DTL.skin);
//		nameValue = new TextField(shipParts.getName(), DTL.skin);
		nameValue = new TextField("", DTL.skin);
		nameValue.setTextFieldListener(new TextFieldListener(){
		@Override
			public void keyTyped(TextField textField, char c) {
					shipParts.setName(textField.getText());
					if(c == '\n' || c == '\r'){
						ui.setKeyboardFocus(null);
					}
			}
		});
		nameValue.setTextFieldFilter(new TextFieldFilter() {
			@Override
			public boolean acceptChar (TextField textField, char c) {
				return !Character.isWhitespace(c) && c != '\n' && c != '\r';
			}

		});
		nameValue.addListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	ui.setKeyboardFocus(nameValue);
		        return true;
		    }
		});
		
		Label xDim = new Label("xDim= ", DTL.skin);
		Label yDim = new Label("yDim= ", DTL.skin);
		//xValue = new TextField(shipParts != null ? Integer.toString(shipParts.getXdim()) : "10", DTL.skin);
		xValue = new TextField("",DTL.skin);
		xValue.setTextFieldFilter(new TextFieldFilter() {
			@Override
			public boolean acceptChar (TextField textField, char c) {
				return Character.isDigit(c);
			}

		});
		xValue.setTextFieldListener(new TextFieldListener(){
		@Override
			public void keyTyped(TextField textField, char c) {
				// TODO Auto-generated method stub
				if(c == '\n' || c == '\r'){
					int value = Integer.parseInt(textField.getText());
					shipParts.setXdim(value);
					ui.setKeyboardFocus(null);
				}
			}
		});
		xValue.addListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	ui.setKeyboardFocus(xValue);
		        return true;
		    }
		});
		//yValue = new TextField(shipParts != null ? Integer.toString(shipParts.getXdim()) : "10", DTL.skin);
		yValue = new TextField("",DTL.skin);
		yValue.setTextFieldFilter(new TextFieldFilter() {
			@Override
			public boolean acceptChar (TextField textField, char c) {
				return Character.isDigit(c) || c == '\n';
			}

		});
		yValue.setTextFieldListener(new TextFieldListener(){
			@Override
				public void keyTyped(TextField textField, char c) {
					// TODO Auto-generated method stub
				if(c == '\n' || c == '\r'){
					int value = Integer.parseInt(textField.getText());						
					shipParts.setYdim(value);
					ui.setKeyboardFocus(null);
				}
			}
		});
		yValue.addListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		    	ui.setKeyboardFocus(yValue);
		        return true;
		    }
		});
		
		TextButton save = new TextButton("Save", DTL.skin);
		save.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				try {
					//System.out.println(shipParts.writeToFile());
					System.out.println("gonna write a file");
					shipParts.writeToFile();
					System.out.println("finished writing file");
					populateShipSelect();
					loadNewShip(shipSelect.getSelected());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		
		modulesBox = new SelectBox<String>(DTL.skin);
		ArrayList<String> moduleItems = new ArrayList<String>();
		for(Modules m : Modules.values()){
			moduleItems.add(m.getStringRep());
		}
		String[] moduleStrings = moduleItems.toArray(new String[moduleItems.size()]);
		modulesBox.setItems(moduleStrings);
		modulesBox.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println(modulesBox.getSelected());
			}
		});
		
		modulesBox.setSelectedIndex(0);
		
		
		
		DrawMode[] drawModeItems = DrawMode.values();
		drawModeSelect = new SelectBox<DrawMode>(DTL.skin);
		drawModeSelect.setItems(drawModeItems);
		drawModeSelect.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				changeDrawMode(drawModeSelect.getSelected());
			}
		});
		drawModeSelect.setSelected(drawMode);
		
		drawModeLabel = new Label("DrawMode: ", DTL.skin);
		changeDrawMode(drawMode);
		
		shipSelect = new SelectBox<String>(DTL.skin);
		shipSelect.addCaptureListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				if(shipSelect.getSelected() != null){
					loadNewShip(shipSelect.getSelected());
				}
			}
		});
		
		
		table.add(nameLabel);
		table.add(nameValue);
		table.add().expandX().colspan(5);
		table.row();
		table.add(drawModeLabel).pad(10);
		table.add(drawModeSelect);
		table.add().expandX().colspan(6).left();
		table.row();
		table.add().expand().colspan(7);
		table.row();
		table.add(xDim);
		table.add(xValue);
		table.add().pad(10);
		table.add(yDim);
		table.add(yValue).pad(10);
		table.add(modulesBox);
		table.add().expandX();
		table.add(shipSelect).pad(20);
		table.add(save);
		

		
		timeAccumulator = 0;
		click1Pos = new Vector2();
		
		populateShipSelect();

		loadNewShip(shipSelect.getSelected());
//		ui.setDebugAll(true);
	}
	
	public void loadAssets(){
		tempBackground = new Image(new Texture("tempbackground.png"));
		tempBackground.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				ui.setKeyboardFocus(null);
			}
		});
		Wall.loadAssets();
		Modules.loadAllModuleAssets();
	}
	
	private void syncButtons(){
		if(ui.getKeyboardFocus() != xValue){
			xValue.setText(shipParts != null ? Integer.toString(shipParts.getXdim()) : "1");
		}
		if(ui.getKeyboardFocus() != yValue){
			yValue.setText(shipParts != null ? Integer.toString(shipParts.getYdim()) : "1");
		}
		if(ui.getKeyboardFocus() != nameValue){
			nameValue.setText(shipParts.getName());
		}
	}
	
	private void populateShipSelect(){
		//String shipSelected = shipSelect.getSelected();
		shipSelect.clearItems();
		
		shipSelect.setItems(getShipFileHandles(BASEDIR));
		if(shipSelect.getItems().contains(nameValue.getMessageText(), false)){
			shipSelect.setSelected(nameValue.getMessageText());
		}else{
			shipSelect.setSelectedIndex(0);
		}
	}
	
	public String[] getShipFileHandles(String baseDir){
		
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
		   dirHandle = Gdx.files.internal("./bin/" + baseDir);
		}else  {
			//(Gdx.app.getType() == ApplicationType.Android)
			dirHandle = Gdx.files.internal(baseDir);
		}
		
		FileHandle[] fileHandles = dirHandle.list();
		String[] names = new String[fileHandles.length-1];
		for(int i = 1; i<fileHandles.length; i++){
			names[i-1] = fileHandles[i].nameWithoutExtension();  
		}
		
		return names;
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("ShipBuilder state ");
		Gdx.input.setInputProcessor(input);
		clicking = false;
		touchDownButton = Buttons.LEFT;
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
	    	ui.act();
	    	gameObjects.act();
	    	timeAccumulator -= DTL.getFrameTime();
	    }
	   // shipParts.reLoadShip();
	    
	   ////Rendering goes here
	    syncButtons();
	    
	    gameObjects.getBatch().begin();
	    tempBackground.draw(gameObjects.getBatch(), 1);
	    gameObjects.getBatch().end();
	    
	    shipParts.draw(ui.getBatch(), ui);
	    gameObjects.draw();
	    ui.draw();
	    

	    
	}
	
	private void changeDrawMode(DrawMode drawMode){
		this.drawMode = drawMode;
		drawModeSelect.setSelected(drawMode);
		if(drawMode == DrawMode.rooms){
			shipParts.setColorizedRooms(true);
		}else if(drawMode == DrawMode.doors){
			shipParts.setColorizedRooms(true);
		}else if(drawMode == DrawMode.shipControl){
			shipParts.setColorizedRooms(false);
		}else if(drawMode == DrawMode.modules){
			shipParts.setColorizedRooms(true);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gameObjects.getViewport().update(width, height, true);
		ui.setViewport(gameObjects.getViewport());
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
		ui.dispose();
	}
	
	private class ClickManager implements InputProcessor{

		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			if(ui.getKeyboardFocus() != nameValue){
			    if(Keys.R == keycode){
			    	changeDrawMode(DrawMode.rooms);
			    }else if(Keys.S == keycode){
			    	changeDrawMode(DrawMode.shipControl);
			    }else if(Keys.D == keycode){
			    	changeDrawMode(DrawMode.doors);
			    }else if(Keys.M == keycode){
			    	changeDrawMode(DrawMode.modules);
			    }
			}
			
			if(Keys.UP == keycode){
				shipParts.moveEverything(0, 1);
			}else if(Keys.DOWN == keycode){
				shipParts.moveEverything(0, -1);
			}else if(Keys.LEFT == keycode){
				shipParts.moveEverything(-1, 0);
			}else if(Keys.RIGHT == keycode){
				shipParts.moveEverything(1, 0);
			}
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
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			// TODO Auto-generated method stub
			
			touchDownButton = button;
			
//			System.out.println(shipParts.convertToSquareCoord(new Vector2(screenX, screenY)));
			click1Pos.x = screenX;
			click1Pos.y = screenY;
			clicking = true;
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			clicking = false;
			if(drawMode == DrawMode.rooms){
				if(touchDownButton == Buttons.LEFT){
					shipParts.createRoom(shipParts.intersectedSections(click1Pos, new Vector2(screenX, screenY), ui));
				}else if(touchDownButton == Buttons.RIGHT){
					shipParts.removeTiles(shipParts.intersectedSections(click1Pos, new Vector2(screenX, screenY), ui));
				}
				
			}else if(drawMode == DrawMode.doors){
				float screenDiffX = click1Pos.x - screenX;
				float screenDiffY = click1Pos.y - screenY;
				int direction = Neighbors.UNDEFINED;
				if(Math.abs(screenDiffX) > Math.abs(screenDiffY)){
					if(screenDiffX <= 0){
						direction = Neighbors.RIGHT;
					}else{
						direction = Neighbors.LEFT;
					}
				}else{
					if(screenDiffY <= 0){
						direction = Neighbors.DOWN;
					}else{
						direction = Neighbors.UP;
					}
				}
				Vector2 doorPos = new Vector2(click1Pos.x, click1Pos.y);
				doorPos = shipParts.convertToSquareCoord(doorPos, ui);
				if(touchDownButton == Buttons.LEFT){
					shipParts.createDoor(doorPos, direction);
				}else if(touchDownButton == Buttons.RIGHT){
					shipParts.removeDoor(doorPos, direction);
				}
			}else if(drawMode == DrawMode.modules){
				if(touchDownButton == Buttons.LEFT){
					shipParts.addModuleToRoomAtPoint(click1Pos, modulesBox.getSelected(), ui);
				}else if(touchDownButton == Buttons.RIGHT){
					shipParts.removeModuleInRoomAtPoint(click1Pos, ui);
				}
			}
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
