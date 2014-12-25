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
	private Stage stage2;
	private Table table;
	
	private ShapeRenderer shapeRen;
	TextButton playButton;
	TextButton optionsButton;
	TextButton exitButton;
	
	InputMultiplexer input;
	
	public MainMenuScreen(){

	}
	
	public void create(DTL game){
		this.game = game;
		stage = new Stage(game.getViewport());
		stage2 = new Stage(game.getViewport());
		stage2.getViewport().update((int) (DTL.VWIDTH/1), (int) (DTL.VHEIGHT/1), false);

		Image img = new Image(new Texture(Gdx.files.internal("demonjonathan.png")));
		img.setBounds(img.getX()+img.getWidth()/4, img.getY()+img.getHeight()/4, img.getWidth()/2, img.getHeight()/2);
		img.setOrigin(img.getWidth()/2, img.getHeight()/2);
//		img.setBounds(img.getX(), img.getY(), img.getWidth(), img.getHeight());
		img.addListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        System.out.println("down");
		        return true;
		    }
		});
		stage2.addActor(img);
		img.setPosition(20, (stage.getViewport().getWorldHeight()/2)-(img.getHeight()/2));
		
		input = new InputMultiplexer();
		input.addProcessor(stage);
		input.addProcessor(stage2);
		Gdx.input.setInputProcessor(input);
		
		table = new Table();
		table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("mainMenuBackground.png")))));
		table.setFillParent(true);
		stage.addActor(table);
		
		playButton = new TextButton("Play Game", DTL.skin);
		playButton.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	play();
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
		
		table.add().expandX();
		table.add().expand().height(400);
		table.add().expandX();
		table.row();
		table.add().expandX();
		table.add(playButton).width(300).height(50).pad(10);
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
		table.add().expand().height(300);
		table.add().expandX();
		
		table.setDebug(DTL.DEBUG);
		img.setDebug(DTL.DEBUG);
		
		//shapeRen = new ShapeRenderer();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		System.out.println("Main Menu State");
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
		for(Actor a : stage2.getActors()){
			//a.addAction(Actions.moveBy(1, 1));
			a.addAction(Actions.rotateBy(10));

		}
	    
	    stage.act(delta);
	    stage2.act(delta);
	    
	    stage.draw();
	    stage2.draw();
	    
	}

	@Override
	public void resize(int width, int height) {
		
		stage2.getViewport().update(width, height, false);
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
		DTL.previousScreen = this;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
		shapeRen.dispose();
	}
	
}
