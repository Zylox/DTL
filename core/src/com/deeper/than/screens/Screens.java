/**
 * Contains references to the screens taht are active
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.screens;

import com.deeper.than.DTL;

/**
 * All Active Screens
 * @author zach
 *
 */
public enum Screens{
	MAINMENU(new MainMenuScreen()),
	OPTIONS(new OptionScreen()),
	TUTORIAL(new TutorialScreen()),
	GAMEPLAY(new GameplayScreen()),
	NEWGAME(new NewGameScreen());
	
	private final EnumerableScreen screen;
	
	private Screens(EnumerableScreen s){
		screen = s;
	}
	
	public void create(DTL game){
		screen.create(game);
	}
	
	public EnumerableScreen getScreen(){
		return screen;
	}
	
}
