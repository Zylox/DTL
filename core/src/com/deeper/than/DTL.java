package com.deeper.than;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deeper.than.screens.Screens;

public class DTL extends Game {
	
	public static final int VWIDTH = 1280;
	public static final int VHEIGHT = 720;
	
	public static final boolean DEBUG = true;
	
	public static boolean gameActive = false;
	
	public static Screen previousScreen = Screens.MAINMENU.getScreen();
	private ExtendViewport viewport;
	
	@Override
	public void create () {
		
		viewport = new ExtendViewport(VHEIGHT, VHEIGHT);
		
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
		this.viewport = (ExtendViewport) view;
	}


}
