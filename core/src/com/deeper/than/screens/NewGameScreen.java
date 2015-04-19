package com.deeper.than.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deeper.than.DTL;

public class NewGameScreen implements EnumerableScreen {

	private Game game;
	private Stage ui; 
	private SelectBox<String> shipSelect;
	
	private static final String BASEDIR = "ships";
	
	private InputMultiplexer input;
	
	public void create(DTL game){
		this.game = game;
		
		ui = new Stage(game.getViewport());
		Table table = new Table(DTL.skin);
		shipSelect = new SelectBox<String>(DTL.skin);
		populateShipSelect();
		
		shipSelect.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				//System.out.println(shipSelect.getSelected());
			}
		});
		
		TextButton playGame = new TextButton("Use Ship", DTL.skin);
		playGame.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				playGame();
			}
		});
		table.setFillParent(true);
		
		table.add(shipSelect);
		table.row();
		table.add(playGame).pad(10);
		
		ui.addActor(table);
		input = new InputMultiplexer();
		input.addProcessor(ui);
	}
	
	private void playGame(){
		game.setScreen(Screens.GAMEPLAY.getScreen());
	}
	
	private void populateShipSelect(){
		shipSelect.clearItems();
		shipSelect.setItems(getShipFileHandles(BASEDIR));
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
		
		int numberOfShips = 3;
		FileHandle[] fileHandles = new FileHandle[numberOfShips];
		//fileHandles[0] = Gdx.files.internal(baseDir + "/");
		fileHandles[0] = Gdx.files.internal(baseDir + "/kes.ship");
		fileHandles[1] = Gdx.files.internal(baseDir + "/tres.ship");
		fileHandles[2] = Gdx.files.internal(baseDir + "/wigwam.ship");
		
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
		if(DTL.gameActive){
			game.setScreen(Screens.GAMEPLAY.getScreen());
		}
	
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
