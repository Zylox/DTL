package com.deeper.than.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deeper.than.DTL;

public class DesktopLauncher {
	
	
	private static int fpsTarget = 100;
	
	public static void main (String[] arg) {
		if(fpsTarget > 144){
			DesktopLauncher.fpsTarget = 144;
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "DTL: Deeper Than Light";
		config.vSyncEnabled = false; // Setting to false disables vertical sync
		config.foregroundFPS = fpsTarget; // Setting to 0 disables foreground fps throttling
		config.backgroundFPS = fpsTarget; // Setting to 0 disables background fps throttling
		new LwjglApplication(new DTL(fpsTarget), config);
	}
}
