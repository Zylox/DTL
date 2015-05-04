package com.deeper.than.screens;

import com.deeper.than.DTL;


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
