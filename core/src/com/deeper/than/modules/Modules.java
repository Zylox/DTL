package com.deeper.than.modules;

import java.lang.reflect.Method;

public enum Modules {
	ClimateControl(ClimateControlModule.class.getCanonicalName());
	
	private String name;
	private Modules(String name){
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	private void loadAssets(){
		try{
			Class<Module> c = (Class<Module>) Class.forName(name);
			Method method = c.getMethod("loadAssets",String.class);
			method.invoke(null, c.getDeclaredField("imagePath").get(null));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void loadAllModuleAssets(){
		for(Modules m : values()){
			m.loadAssets();
		}
	}
	
}
