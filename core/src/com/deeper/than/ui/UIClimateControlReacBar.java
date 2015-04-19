package com.deeper.than.ui;

import com.deeper.than.modules.ClimateControlModule;
import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.Modules;

public class UIClimateControlReacBar extends UIMainModuleReactorBar {

	public UIClimateControlReacBar(int powered,
			ReactorBar mainPower, MainModule module) {
		super(powered, Modules.getIcon(ClimateControlModule.class.getCanonicalName()), mainPower, module);
	}

}
