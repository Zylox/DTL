package com.deeper.than.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;
import com.deeper.than.shipbuilder.ShipPartContainer;

public class ShipBuilderScreen implements EnumerableScreen{

	private enum DrawMode{
		rooms,
		doors,
		shipControl;
		
		public String toString(){

			if(this == rooms){
				return "roomMode";
			}else if(this == shipControl){
				return "shipControlMode";
			}else if(this == doors){
				return "doorsMode";
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
	
	private SelectBox<Modules> modulesBox;
	
	private Label drawModeLabel;
	
	private Vector2 click1Pos;
	private boolean clicking;
	private int touchDownButton;
	
	private InputMultiplexer input;

	Image tempBackground;
	
	public void create(DTL game){
		this.game = game;

		loadAssets();
		
		gameObjects = new Stage(game.getViewport());
		gameObjects.addActor(tempBackground);
		ui = new Stage(game.getViewport());
		input = new InputMultiplexer();
		input.addProcessor(new ClickManager());
		input.addProcessor(ui);
		input.addProcessor(gameObjects);
		
		shipParts = new ShipPartContainer(game, gameObjects);
		shipParts.loadNewShip(Gdx.files.internal("ships/kes.ship"));
		Table table = new Table(DTL.skin);
		table.setFillParent(true);
		ui.addActor(table);
		
		Label nameLabel = new Label("Name= ", DTL.skin);
		nameValue = new TextField(shipParts.getName(), DTL.skin);
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
		xValue = new TextField(shipParts != null ? Integer.toString(shipParts.getXdim()) : "10", DTL.skin);
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
		yValue = new TextField(shipParts != null ? Integer.toString(shipParts.getXdim()) : "10", DTL.skin);
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		
		modulesBox = new SelectBox<Modules>(DTL.skin);
		modulesBox.setItems(Modules.values());
		modulesBox.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println(modulesBox.getSelected());
			}
		});
		
		modulesBox.setSelectedIndex(0);
		
		
		drawModeLabel = new Label("DrawMode: " + drawMode.toString(), DTL.skin);
		changeDrawMode(drawMode);
		
		
		
		table.add(nameLabel);
		table.add(nameValue);
		table.add().expandX().colspan(5);
		table.row();
		table.add(drawModeLabel).expandX().colspan(7).left();
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
		table.add(save);
		

		
		timeAccumulator = 0;
		click1Pos = new Vector2();
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
	    	ui.act(DTL.getFrameTime());
	    	gameObjects.act(DTL.getFrameTime());
	    	timeAccumulator -= DTL.getFrameTime();
	    }
	   // shipParts.reLoadShip();
	    
	   ////Rendering goes here
	    syncButtons();
	    gameObjects.draw();
	    shipParts.draw(ui.getBatch());
	    ui.draw();
	    

	    
	}
	
	private void changeDrawMode(DrawMode drawMode){
		this.drawMode = drawMode;
		drawModeLabel.setText("DrawMode: " + drawMode.toString());
		if(drawMode == DrawMode.rooms){
			shipParts.setColorizedRooms(true);
		}else if(drawMode == DrawMode.doors){
			shipParts.setColorizedRooms(true);
		}else if(drawMode == DrawMode.shipControl){
			shipParts.setColorizedRooms(false);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		gameObjects.getViewport().update(width, height, true);
		ui.getViewport().update(width, height, true);
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
					shipParts.createRoom(shipParts.intersectedSections(click1Pos, new Vector2(screenX, screenY)));
				}else if(touchDownButton == Buttons.RIGHT){
					shipParts.removeTiles(shipParts.intersectedSections(click1Pos, new Vector2(screenX, screenY)));
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
				Vector2 doorPos = new Vector2(click1Pos.x, Gdx.graphics.getHeight() - click1Pos.y);
				doorPos = shipParts.convertToSquareCoord(doorPos);
				if(touchDownButton == Buttons.LEFT){
					shipParts.createDoor(doorPos, direction);
				}else if(touchDownButton == Buttons.RIGHT){
					shipParts.removeDoor(doorPos, direction);
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
