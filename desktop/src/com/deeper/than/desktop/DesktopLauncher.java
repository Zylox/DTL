package com.deeper.than.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deeper.than.DTL;

public class DesktopLauncher {
	
	private static final boolean MAXFRAMESGO = false;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		if(MAXFRAMESGO){
			config.vSyncEnabled = false; // Setting to false disables vertical sync
			config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
			config.backgroundFPS = 0; // Setting to 0 disables background fps throttling
		}
		new LwjglApplication(new DTL(), config);
	}
}
