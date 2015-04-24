package com.deeper.than.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.deeper.than.crew.CrewSkills;
import com.deeper.than.screens.GameplayScreen;

public class UICrewSkillsProgressBar extends Widget {
	public static final float MIN_HEIGHT_SUGGESTION = 5;
	public static final float LENGTH_SUGGESTION = 50;
	
	
	private CrewSkills.CrewSkillsTypes skillToTrack;
	private CrewSkills crewSkills;
	
	public UICrewSkillsProgressBar(CrewSkills crewSkills, CrewSkills.CrewSkillsTypes skillToTrack){
		this.skillToTrack = skillToTrack;
		this.crewSkills = crewSkills;
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		Color color = batch.getColor().cpy();
		batch.setColor(getColorByLevel());
		batch.draw(GameplayScreen.highlight, getX(), getY(), getLevelProgressPrcnt() * getWidth(), getHeight());
		batch.setColor(Color.DARK_GRAY);
		batch.draw(GameplayScreen.highlight, getX()+ getLevelProgressPrcnt() * getWidth(), getY(), (1-getLevelProgressPrcnt()) * getWidth(), getHeight());
		batch.setColor(color);
	}
	
	public Color getColorByLevel(){
		int level = getLevel();
		if(level == 0){
			return Color.WHITE;
		}else if(level == 1){
			return Color.GREEN;
		}else if(level == 2){
			return Color.YELLOW;
		}
		
		return Color.BLACK;
	}
	
	private float getLevelProgressPrcnt(){
		switch(skillToTrack){
		case BRIDGE:
			return crewSkills.getBridgeLvlPercent();
		case ENGINES:
			return crewSkills.getEnginesLvlPercent();
		case SHIELDS:
			return crewSkills.getShieldsLvlPercent();
		case WEAPONS:
			return crewSkills.getWeaponsLvlPercent();
		case REPAIR:
			return crewSkills.getRepairLvlPercent();
		case COMBAT:
			return crewSkills.getCombatLvlPercent();
		}
		
		return 0;
	}
	
	private int getLevel(){
		switch(skillToTrack){
		case BRIDGE:
			return crewSkills.getBridgeLevel();
		case ENGINES:
			return crewSkills.getEnginesLevel();
		case SHIELDS:
			return crewSkills.getShieldsLevel();
		case WEAPONS:
			return crewSkills.getWeaponsLevel();
		case REPAIR:
			return crewSkills.getRepairLevel();
		case COMBAT:
			return crewSkills.getCombatLevel();
		}
		return 0;
	}
	
	
}
