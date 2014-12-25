package com.deeper.than;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deeper.than.screens.Screens;

public class DTL extends Game {
	
	public static final int VWIDTH = 1920;
	public static final int VHEIGHT = 1080;
	
	public static final boolean DEBUG = true;
	public static boolean gameActive = false;
	
	public static Screen previousScreen = Screens.MAINMENU.getScreen();
	private FitViewport viewport;
	public static Skin skin;
	
	@Override
	public void create () {
		
		Gdx.graphics.setDisplayMode(VWIDTH, VHEIGHT, false);
		
		viewport = new FitViewport(VWIDTH, VHEIGHT);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		Screens[] screens = Screens.values();
		
		for(Screens s : screens){
			s.create(this);
		}
		
		setScreen(Screens.MAINMENU.getScreen());
	}
	
	public Viewport getViewport(){
		return viewport;
	}
	
	public void setViewport(Viewport view){
		this.viewport = (FitViewport) view;
	}


}
