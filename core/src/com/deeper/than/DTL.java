package com.deeper.than;

import java.io.Console;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
	public static final boolean TEXTDEBUG = true || GLOBALDEBUG;
	public static boolean gameActive = false;
	
	private static int frameTarget;
	
	public static Screen previousScreen = Screens.MAINMENU.getScreen();
	private FitViewport viewport;
	public static Skin skin;
	public static BitmapFont font;
	
	public static long startTime;
	
	public DTL(int frameTarget){
		DTL.frameTarget = frameTarget;
	}
	
	@Override
	public void create () {
		
		
		//Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);
		
		startTime = System.currentTimeMillis();
		
		Gdx.graphics.setDisplayMode(VWIDTH, VHEIGHT, false);
		
		viewport = new FitViewport(VWIDTH, VHEIGHT);
		viewport.update(VWIDTH, VWIDTH, true);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		font = skin.getFont("default-font");
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

	public static int getFrameTarget() {
		return frameTarget;
	}

	public static float getFrameTime() {
		return 1f/frameTarget;
	}

	/**
	 * Pass a rate per second, returns rate per frame
	 * @param ratePerSecond
	 * @return
	 */
	public static float getRatePerFrame(float ratePerSecond){
		return ratePerSecond*getFrameTime();
	}
}
