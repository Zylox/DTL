package com.deeper.than.ui;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deeper.than.modules.SubModule;

public class UISubModuleReactorBar extends UIIconReactorBar implements UIModuleSyncable{
	private SubModule module;
	
	public UISubModuleReactorBar(Sprite icon, SubModule module) {
		super(module.getLevel(), module.getLevel() , icon);
		this.module = module;
	}
	
	public void checkforSectionsChange(){
		if(module.getLevel() != getSections()){
			this.adjustSegments(module.getLevel(), module.getLevel());
			addIconToTable();
		}
	}
	
	

}
