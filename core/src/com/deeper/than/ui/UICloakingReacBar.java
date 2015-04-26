package com.deeper.than.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deeper.than.modules.CloakingModule;
import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.Modules;

public class UICloakingReacBar extends UIModuleReactorBar {

	public UICloakingReacBar(int powered, ReactorBar mainPower,
			MainModule module) {
		super(powered, Modules.getIcon(CloakingModule.class.getCanonicalName()), mainPower, module);
		// TODO Auto-generated constructor stub
	}

}
