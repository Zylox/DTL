package com.deeper.than.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ObjectMap;
import com.deeper.than.Room;
import com.deeper.than.Ship;

/**
 * A list of all module types. Holds logic to initialize all module assets.
 * All modules must be added to this enumeration.
 * @author zach
 *
 */
public enum Modules {
	ClimateControl(ClimateControlModule.class.getCanonicalName(), "ClimateControlModule", "climateControl.png"),
	Engine(EngineModule.class.getCanonicalName(), "EngineModule", "engine.png"),
	Bridge(BridgeModule.class.getCanonicalName(), "BridgeModule", "bridge.png"),
	HatchControl(HatchControlModule.class.getCanonicalName(), "HatchControlModule", "hatchControl.png"),
	Sensors(SensorsModule.class.getCanonicalName(), "SensorsModule", "sensors.png"),
	Medbay(MedbayModule.class.getCanonicalName(), "MedbayModule", "medbay.png"),
	Docking(DockingModule.class.getCanonicalName(), "DockingModule", null);
	
	private String name;
	private String stringRep;
	private String imagePath;
	private Sprite icon;
	
	
	/**
	 * Static array of all module icons since only one instance of each is needed.
	 */
	private static ObjectMap<String, Sprite> icons = new ObjectMap<String, Sprite>();
	
	private Modules(String name, String stringRep,String imagePath){
		this.name = name;
		this.stringRep = stringRep;
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
	
	public String getStringRep(){
		return stringRep;
	}
	
	public Module instantiateModule(Ship ship){
		return instantiateModule(ship.getHighestModuleID(), null, ship);
	}
	
	public Module instantiateModule(Ship ship, Room room){
		return instantiateModule(ship.getHighestModuleID(), room, ship);
	}
	
	public Module instantiateModule(int id, Room room, Ship ship){
		if(this == ClimateControl){
			return new ClimateControlModule(id, room, ship);
		}
		if(this == Engine){
			return new EngineModule(id, room, ship);
		}
		if(this == Bridge){
			return new BridgeModule(id, room, ship);
		}
		if(this == HatchControl){
			return new HatchControlModule(id, room, ship);
		}
		if(this == Sensors){
			return new SensorsModule(id, room, ship);
		}
		if(this == Docking){
			return new DockingModule(id, room, ship);
		}
	
		return null;
	}

	public static String stringRep(Module module){
		String stringRep = "";
		
		if(module instanceof ClimateControlModule){
			stringRep = ClimateControl.getStringRep();
		}else if(module instanceof EngineModule){
			stringRep = Engine.getStringRep();
		}else if(module instanceof BridgeModule){
			stringRep = Bridge.getStringRep();
		}else if(module instanceof HatchControlModule){
			stringRep = HatchControl.getStringRep();
		}else if(module instanceof SensorsModule){
			stringRep = Sensors.getStringRep();
		}else if(module instanceof DockingModule){
			stringRep = Docking.getStringRep();
		}
		
		return stringRep;
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
