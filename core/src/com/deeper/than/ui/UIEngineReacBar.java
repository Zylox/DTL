package com.deeper.than.ui;

import com.deeper.than.modules.EngineModule;
import com.deeper.than.modules.Modules;

public class UIEngineReacBar extends UIMainModuleReactorBar{
	
	public UIEngineReacBar(int powered, ReactorBar mainPower, EngineModule module) {
		super(powered, Modules.getIcon(EngineModule.class.getCanonicalName()), mainPower, module);
	}
}
