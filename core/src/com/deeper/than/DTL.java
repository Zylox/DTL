package com.deeper.than;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deeper.than.screens.Screens;

public class DTL extends Game {
	
	public static final int VWIDTH = 1280;
	public static final int VHEIGHT = 720;
	
	public static final boolean GLOBALDEBUG = false;
	@SuppressWarnings("unused")
	public static final boolean GRAPHICALDEBUG = false || GLOBALDEBUG;
	@SuppressWarnings("unused")
	public static final boolean TEXTDEBUG = false || GLOBALDEBUG;
	public static boolean gameActive = false;
	
	public static Screen previousScreen = Screens.MAINMENU.getScreen();
	private FitViewport viewport;
	public static Skin skin;
	
	public static long startTime;
	
	@Override
	public void create () {
		
		//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		
		startTime = System.currentTimeMillis();
		
		Gdx.graphics.setDisplayMode(VWIDTH, VHEIGHT, false);
		
		viewport = new FitViewport(VWIDTH, VHEIGHT);
		viewport.update(VWIDTH, VWIDTH, true);
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
	
	public static void printDebug(String message){
		if(TEXTDEBUG){
			System.out.println(message);
		}
	}


}
