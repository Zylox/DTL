package com.deeper.than.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.crew.CrewSkills;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.EngineModule;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SheildModule;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.ui.UICrewSkillsProgressBar.CrewSkillsTypes;

public class UICrewSkillsPlate extends WidgetGroup {

	public static final float minIconSize = 15;
	
	private static Image background = null;
	private static Image bridge = null;
	private static Image engines = null;
	private static Image shields = null;
	private static Image weapons = null;
	private static Image repair = null;
	private static Image combat = null;
	
	public UICrewSkillsPlate(CrewSkills crewSkills){
		Stack stack = new Stack();
		stack.add(new Image(background.getDrawable()));
		float padLeft = 15;
		float padRight = 15;
		Table table = new Table();
		table.padLeft(padLeft);
		table.padRight(padRight);
		table.padBottom(5);
		table.padTop(5);
		table.add(new Image(bridge.getDrawable())).minSize(minIconSize);
		table.add(new UICrewSkillsProgressBar(crewSkills, CrewSkillsTypes.BRIDGE)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(new Image(engines.getDrawable())).minSize(minIconSize).padRight(2);
		table.add(new UICrewSkillsProgressBar(crewSkills, CrewSkillsTypes.ENGINES)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(new Image(shields.getDrawable())).minSize(minIconSize);
		table.add(new UICrewSkillsProgressBar(crewSkills, CrewSkillsTypes.SHIELDS)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(new Image(weapons.getDrawable())).minSize(minIconSize);
		table.add(new UICrewSkillsProgressBar(crewSkills, CrewSkillsTypes.WEAPONS)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(new Image(repair.getDrawable())).minSize(minIconSize);
		table.add(new UICrewSkillsProgressBar(crewSkills, CrewSkillsTypes.REPAIR)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(new Image(combat.getDrawable())).minSize(minIconSize);
		table.add(new UICrewSkillsProgressBar(crewSkills, CrewSkillsTypes.COMBAT)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		
		stack.add(table);
		stack.setBounds(getX(), getY(), padLeft+padRight+ UICrewSkillsProgressBar.LENGTH_SUGGESTION, minIconSize*6+10);
		this.addActor(stack);
	}
	
	public static void loadAssets(){
		background = new Image(new Texture(Gdx.files.internal("crewSkillsBackground2.png")));
		bridge = new Image(Modules.getIcon(BridgeModule.class.getCanonicalName()));
		engines = new Image(Modules.getIcon(EngineModule.class.getCanonicalName()));
		shields = new Image(Modules.getIcon(SheildModule.class.getCanonicalName()));
		weapons = new Image(Modules.getIcon(WeaponsModule.class.getCanonicalName()));
		repair = new Image(new Texture(Gdx.files.internal("repairIcon.png")));
		combat = new Image(new Texture(Gdx.files.internal("combat.png")));
	}
	
}
