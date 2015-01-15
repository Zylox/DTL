package com.deeper.than.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;

public enum Modules {
	ClimateControl(ClimateControlModule.class.getCanonicalName(), "climateControl.png"),
	Engine(EngineModule.class.getCanonicalName(), "engine.png"),
	Bridge(BridgeModule.class.getCanonicalName(), "badModule.png"),
	HatchControl(HatchControlModule.class.getCanonicalName(), "badModule.png"),
	Sensors(SensorsModule.class.getCanonicalName(), "badModule.png");
	
	private String name;
	private String imagePath;
	private Sprite icon;
	
	private static ObjectMap<String, Sprite> icons = new ObjectMap<String, Sprite>();
	
	private Modules(String name, String imagePath){
		this.name = name;
		this.imagePath = imagePath;
		icon = null;
	}

	private void loadAssets(){
		icon = new Sprite(new Texture(Gdx.files.internal(imagePath)));
		icons.put(name, icon);
	}
	
	public static void loadAllModuleAssets(){
		for(Modules m : values()){
			m.loadAssets();
		}
	}
	
	public static Sprite getIcon(String moduleName){
		return icons.get(moduleName);
	}
	
}
