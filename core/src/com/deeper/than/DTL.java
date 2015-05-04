/**
 * The main hook of the game from the launchers.
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
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
import com.deeper.than.weapons.Weapon;
/**
 * Driver class for the game
 * @author zach
 *
 */
public class DTL extends Game {
	
	//The virtual height and width the game operates at
	public static final int VWIDTH = 1280;
	public static final int VHEIGHT = 720;
	public static DTLMap MAP;
	
	//Debug tools
	public static final boolean GLOBALDEBUG = false;
	public static final boolean GRAPHICALDEBUG = false || GLOBALDEBUG;
	@SuppressWarnings("unused")
	public static final boolean PATHDEBUG = true || GLOBALDEBUG;
	@SuppressWarnings("unused")
	public static final boolean TEXTDEBUG = true || GLOBALDEBUG;
	public static boolean developmentMode = true;

	//stores whether or not a game is active in newGameScreen
	public static boolean gameActive = false;
	//Frame rate that is being aimed for
	private static int frameTarget;
	
	public static Screen previousScreen = Screens.MAINMENU.getScreen();
	//an abstract Viewport through wich the game is displayed regardless of actual resoluiont or aspect ratio
	//puts black bar gutters outside of viewport
	private FitViewport viewport;
	//The default formatting for our ui elements
	public static Skin skin;
	public static BitmapFont font;
	
	public static long startTime;
	
	//A static utility for iding items
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
																			//sets the font to lineraly filter when scaling
		DTL.skin.getFont("default-font").getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font = skin.getFont("default-font");
		
		Weapon.loadWeaponAssets();
		
		//Screens are the main contexts for the game
		Screens[] screens = Screens.values();
		for(Screens s : screens){
			s.create(this);
		}
		
		//Set the first screen we find ourselves in to the main menu
		setScreen(Screens.MAINMENU.getScreen());
	}
	
	/**
	 * Returns the ship selected in the new game screen.
	 * @return ship selected in new game screen
	 */
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
	
	/**
	 * Prints out a text debug message if text debugging is eneabled
	 * @param message
	 */
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
