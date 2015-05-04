/**
 * Plate that pops up when hovering over crew plate
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;
import com.deeper.than.crew.CrewSkills;
import com.deeper.than.crew.CrewSkills.CrewSkillsTypes;
import com.deeper.than.modules.BridgeModule;
import com.deeper.than.modules.EngineModule;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SheildModule;
import com.deeper.than.modules.WeaponsModule;

/**
 * Plate to hold crew stats
 * @author zach
 *
 */
public class UICrewSkillsPlate extends WidgetGroup {

	public static final float minIconSize = 15;
	
	private static Image background = null;
	private static Image bridge = null;
	private static Image engines = null;
	private static Image shields = null;
	private static Image weapons = null;
	private static Image repair = null;
	private static Image combat = null;
	
	private CrewSkills crewSkills;
	private Array<Image> icons;
	private Array<UICrewSkillsProgressBar> progBars;
	
	public UICrewSkillsPlate(CrewSkills crewSkills){
		this.crewSkills = crewSkills;
		this.icons = new Array<Image>(6);
		this.progBars = new Array<UICrewSkillsProgressBar>(6);
		//new images need to be made for each instance
		//new drawables are not being instantiated so nothing is being leaked or loaded from file again
		icons.add(new Image(bridge.getDrawable()));
		icons.add(new Image(engines.getDrawable()));
		icons.add(new Image(shields.getDrawable()));
		icons.add(new Image(weapons.getDrawable()));
		icons.add(new Image(repair.getDrawable()));
		icons.add(new Image(combat.getDrawable()));
		
		progBars.add(new UICrewSkillsProgressBar(crewSkills, CrewSkills.CrewSkillsTypes.BRIDGE));
		progBars.add(new UICrewSkillsProgressBar(crewSkills, CrewSkills.CrewSkillsTypes.ENGINES));
		progBars.add(new UICrewSkillsProgressBar(crewSkills, CrewSkills.CrewSkillsTypes.SHIELDS));
		progBars.add(new UICrewSkillsProgressBar(crewSkills, CrewSkills.CrewSkillsTypes.WEAPONS));
		progBars.add(new UICrewSkillsProgressBar(crewSkills, CrewSkills.CrewSkillsTypes.REPAIR));
		progBars.add(new UICrewSkillsProgressBar(crewSkills, CrewSkills.CrewSkillsTypes.COMBAT));
		
		Stack stack = new Stack();
		stack.add(new Image(background.getDrawable()));
		float padLeft = 15;
		float padRight = 15;
		Table table = new Table();
		table.padLeft(padLeft);
		table.padRight(padRight);
		table.padBottom(5);
		table.padTop(5);
		//This looks stupid and like it should be done in a loop, but if something needs teasking, like the engine icon,
		//loops become too complicated to justify, so im leaving it like this to save me a headache later.
		table.add(icons.get(0)).minSize(minIconSize);
		table.add(progBars.get(0)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(icons.get(1)).minSize(minIconSize).padRight(2);
		table.add(progBars.get(1)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(icons.get(2)).minSize(minIconSize);
		table.add(progBars.get(2)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(icons.get(3)).minSize(minIconSize);
		table.add(progBars.get(3)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(icons.get(4)).minSize(minIconSize);
		table.add(progBars.get(4)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		table.add(icons.get(5)).minSize(minIconSize);
		table.add(progBars.get(5)).minWidth(UICrewSkillsProgressBar.LENGTH_SUGGESTION).minHeight(UICrewSkillsProgressBar.MIN_HEIGHT_SUGGESTION);
		table.row();
		
		stack.add(table);
		stack.setBounds(getX(), getY(), padLeft+padRight+ UICrewSkillsProgressBar.LENGTH_SUGGESTION, minIconSize*6+10);
		this.addActor(stack);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		//color icon by level
		for(int i = 0; i < icons.size; i++){
			icons.get(i).setColor(progBars.get(i).getColorByLevel());
		}
		super.draw(batch, parentAlpha);
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
