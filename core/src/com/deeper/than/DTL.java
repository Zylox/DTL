package com.deeper.than;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deeper.than.screens.NewGameScreen;
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
	public static boolean developmentMode = true;
	
	private static int frameTarget;
	
	public static Screen previousScreen = Screens.MAINMENU.getScreen();
	private FitViewport viewport;
	public static Skin skin;
	public static BitmapFont font;
	
	public static long startTime;
	
	public static int firstOpenId = 0;
	
	public DTL(int frameTarget){
		DTL.frameTarget = frameTarget;
	}
	
	@Override
	public void create () {
		startTime = System.currentTimeMillis();
		
		Gdx.graphics.setDisplayMode(VWIDTH, VHEIGHT, false);
		
		
		viewport = new FitViewport(VWIDTH, VHEIGHT);
		viewport.update(VWIDTH, VWIDTH, true);
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		DTL.skin.getFont("default-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = skin.getFont("default-font");
		Screens[] screens = Screens.values();
		
		for(Screens s : screens){
			s.create(this);
		}
		
		setScreen(Screens.MAINMENU.getScreen());
	}
	
	public String getSelectedShip(){
		String name = ((NewGameScreen) Screens.NEWGAME.getScreen()).getSelectedShip();
		return name;
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
	 * Pass a rate per second, returns rate per timeStep, which should be one frametime
	 * @param ratePerSecond
	 * @return
	 */
	public static float getRatePerTimeStep(float ratePerSecond){
		return ratePerSecond*getFrameTime();
	}
}
