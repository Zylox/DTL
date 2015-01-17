package com.deeper.than.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * A list of all module types. Holds logic to initialize all module assets.
 * All modules must be added to this enumeration.
 * @author zach
 *
 */
public enum Modules {
	ClimateControl(ClimateControlModule.class.getCanonicalName(), "climateControl.png"),
	Engine(EngineModule.class.getCanonicalName(), "engine.png"),
	Bridge(BridgeModule.class.getCanonicalName(), "bridge.png"),
	HatchControl(HatchControlModule.class.getCanonicalName(), "hatchControl.png"),
	Sensors(SensorsModule.class.getCanonicalName(), "sensors.png"),
	Medbay(MedbayModule.class.getCanonicalName(), "medbay.png"),
	Docking(DockingModule.class.getCanonicalName(), null);
	
	private String name;
	private String imagePath;
	private Sprite icon;
	
	/**
	 * Static array of all module icons since only one instance of each is needed.
	 */
	private static ObjectMap<String, Sprite> icons = new ObjectMap<String, Sprite>();
	
	private Modules(String name, String imagePath){
		this.name = name;
		this.imagePath = imagePath;
		icon = null;
	}

	/**
	 * Loads the asset of the individual module.
	 * If imagepath is null, sets the icon to badModule.png
	 */
	private void loadAssets(){
		if(imagePath == null){
			imagePath = "badModule.png";
		}
		icon = new Sprite(new Texture(Gdx.files.internal(imagePath)));
		icons.put(name, icon);
	}
	
	/**
	 * Initilizes assets for all modules in the enumeration
	 */
	public static void loadAllModuleAssets(){
		for(Modules m : values()){
			m.loadAssets();
		}
	}
	
	/**
	 * Retrieves the icon for the module
	 * ModuleName is the canonical name for the class.
	 * @param moduleName
	 * @return icon for the module
	 */
	public static Sprite getIcon(String moduleName){
		return icons.get(moduleName);
	}
	
}
