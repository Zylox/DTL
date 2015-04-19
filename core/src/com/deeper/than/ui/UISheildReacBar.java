package com.deeper.than.ui;

import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SheildModule;

public class UISheildReacBar extends UIMainModuleReactorBar{

	public UISheildReacBar(int powered, ReactorBar mainPower, SheildModule module) {
		super(powered, Modules.getIcon(SheildModule.class.getCanonicalName()), mainPower, module);
	}

}
