package com.deeper.than.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deeper.than.DTL;
import com.deeper.than.PlayerShip;
import com.deeper.than.Ship;
import com.deeper.than.ShipLoadException;

public class NewGameScreen implements EnumerableScreen {

	private DTL game;
	private Stage ui; 
	private SelectBox<String> shipSelect;
	
	private Ship ship;
	
	private static Texture background;
	
	private Image bg;
	private static final String BASEDIR = "ships";
	
	private InputMultiplexer input;
	
	public static void loadAssets(){
		background = new Texture(Gdx.files.internal("tempbackground.png")); 
	}
	
	public void create(DTL game){
		this.game = game;
		loadAssets();
		ui = new Stage(game.getViewport());
		bg = new Image(background);
		ui.addActor(bg);
		Table table = new Table(DTL.skin);
		shipSelect = new SelectBox<String>(DTL.skin);
		populateShipSelect();
		
		shipSelect.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//System.out.println(shipSelect.getSelected());
				loadShip();
			}
		});
		
		TextButton playGame = new TextButton("Use Ship", DTL.skin);
		playGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				playGame();
			}
		});
		table.setFillParent(true);
		
		table.add(shipSelect).top();
		table.row();
		table.add(playGame).pad(10);
		table.row();
		table.add().expand();
		
		ui.addActor(table);
		ui.setDebugAll(DTL.GRAPHICALDEBUG);
		input = new InputMultiplexer();
		input.addProcessor(ui);
		loadShip();
	}
	
	private void playGame(){
		game.setScreen(Screens.GAMEPLAY.getScreen());
	}
	
	private void populateShipSelect(){
		shipSelect.clearItems();
		shipSelect.setItems(getShipFileHandles(BASEDIR));
	}
	
	private void loadShip(){
		if(ship != null){
			ship.remove();
		}
		try {
			ship = new PlayerShip(Gdx.files.internal("ships/" + getSelectedShip() +".ship"), true, game, DTL.firstOpenId++, ((GameplayScreen)Screens.GAMEPLAY.getScreen()).getWeaponGenerator());
		} catch (ShipLoadException e) {
			e.printStackTrace();
			System.out.println("will im going to kill you");
		}
		ui.addActor(ship);
	}
	
	public String[] getShipFileHandles(String baseDir){
		
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			String base = baseDir + "/";
			System.out.println(base);
		   dirHandle = Gdx.files.internal(base);
		}else  {
			//(Gdx.app.getType() == ApplicationType.Android)
			dirHandle = Gdx.files.internal(baseDir);
		}
		
		int numberOfShips = 8;
		FileHandle[] fileHandles = new FileHandle[numberOfShips];
		//fileHandles[0] = Gdx.files.internal(baseDir + "/");
		fileHandles[0] = Gdx.files.internal(baseDir + "/kes.ship");
		fileHandles[1] = Gdx.files.internal(baseDir + "/tres.ship");
		fileHandles[2] = Gdx.files.internal(baseDir + "/wigwam.ship");
		fileHandles[3] = Gdx.files.internal(baseDir + "/theq.ship");
		fileHandles[4] = Gdx.files.internal(baseDir + "/swordfishii.ship");
		fileHandles[5] = Gdx.files.internal(baseDir + "/thebox.ship");
		fileHandles[6] = Gdx.files.internal(baseDir + "/thewut.ship");
		fileHandles[7] = Gdx.files.internal(baseDir + "/beetle.ship");
		
		ArrayList<String> collectedNames = new ArrayList<String>();
		for(int i = 0; i<fileHandles.length; i++){
			if(!fileHandles[i].isDirectory()){
				collectedNames.add(fileHandles[i].nameWithoutExtension());
			}
		}
		String[] names = collectedNames.toArray(new String[collectedNames.size()]);
		
		return names;
	}
	
	public String getSelectedShip(){
		return shipSelect.getSelected();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		DTL.printDebug("New Game Screen");
	
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
	    ui.act();
	    ui.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		ui.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		DTL.previousScreen = this;
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
