package com.deeper.than.ui;

import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.MedbayModule;
import com.deeper.than.modules.Modules;

public class UIMedbayReacBar extends UIModuleReactorBar{

	public UIMedbayReacBar(int powered, ReactorBar mainPower, MainModule module) {
		super(powered, Modules.getIcon(MedbayModule.class.getCanonicalName()), mainPower, module);
		// TODO Auto-generated constructor stub
	}

}
