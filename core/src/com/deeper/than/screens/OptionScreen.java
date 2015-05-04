/**
 * Screen where options are selected. currenlty this is jsut fullscreen or not
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deeper.than.DTL;

/**
 * Option screen
 * @author zach
 *
 */
public class OptionScreen implements EnumerableScreen {

	
	private DTL game;
	private Stage menu;
	
	//sets up table
	public void create(DTL game){
		this.game = game;
		menu = new Stage(game.getViewport());
		
		Table table = new Table();
		table.setFillParent(true);
		TextButton fullscreen = new TextButton("FullScreen On/Off", DTL.skin);
		//callback to toggle fullscreen
		fullscreen.addListener(new ChangeListener() {
		    public void changed (ChangeEvent event, Actor actor) {
		    	fullscreenToggle();
		    }
		});
		table.add(fullscreen);
		menu.addActor(table);
		
		
		table.setDebug(DTL.GRAPHICALDEBUG);
		
	}
	
	@Override
	public void show() {
		DTL.printDebug("Option Screen");
		Gdx.input.setInputProcessor(menu);
	}

	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
	    	mainMenu();
	    }
		menu.draw();
	}

	@Override
	public void resize(int width, int height) {
		menu.getViewport().update(width, height, true);
		game.setViewport(menu.getViewport());
	}
	
	public void fullscreenToggle(){
		if(Gdx.graphics.isFullscreen()){
			Gdx.graphics.setDisplayMode(DTL.VWIDTH, DTL.VHEIGHT, false);
		}else{
			if(Gdx.app.getType() == ApplicationType.Desktop){
				Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
			}else{
				Gdx.graphics.setDisplayMode(DTL.VWIDTH, DTL.VHEIGHT, true);
			}
		}
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
	
	public void mainMenu(){
		game.setScreen(DTL.previousScreen);
	}

	@Override
	public void hide() {
		DTL.previousScreen = this;
	}

	@Override
	public void dispose() {
		menu.dispose();
	}

}
