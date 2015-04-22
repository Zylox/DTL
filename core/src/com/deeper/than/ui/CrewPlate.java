package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deeper.than.DTL;
import com.deeper.than.crew.Crew;
import com.deeper.than.screens.GameplayScreen;
import com.deeper.than.screens.Screens;

public class CrewPlate extends WidgetGroup {
	private Crew crew;
	private Image bar;
	private Image crewImg;
	private Table table;
	private UICrewSkillsPlate skillsPlate;
	
	public CrewPlate(Crew crew){
		this.crew = crew;
		this.skillsPlate = new UICrewSkillsPlate(crew.getSkills());
		table = new Table(DTL.skin);
		table.background(new TextureRegionDrawable(new TextureRegion(GameplayScreen.highlight)));
		table.setColor(Color.GRAY);
		table.setFillParent(true);
		bar = new Image(GameplayScreen.highlight);
		
		crewImg = new Image(crew.getIcon());
		
		
		table.add(crewImg).prefWidth(Crew.CREW_HEIGHT/Crew.SCALE).prefHeight(Crew.CREW_HEIGHT/Crew.SCALE).left();
		Table innerTable = new Table();
		
		innerTable.add(new Label(crew.getName(), DTL.skin)).fill().top().expandX().padBottom(5);
		innerTable.row();
		innerTable.add(bar).fill();
		//table.add(new CrewHealthBar(crew)).left().expand();
		//table.add().expand();
		table.add(innerTable).expand().fill();
		table.row();
		this.addActor(table);
		skillsPlate.setX(80);
		skillsPlate.setY(getY()-UICrewSkillsPlate.minIconSize*6+20);
		skillsPlate.setHeight(20);
		skillsPlate.setVisible(false);
		this.addActor(skillsPlate);
		addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setAsSelected();
				return true;
		    }
			
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				setSkillsVisible(true);
			}
			
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
				setSkillsVisible(false);
			}
			
		});
		
		
		
	}
	
	private void setSkillsVisible(boolean visible){
		System.out.println("SkillsPlate: " + visible);
		skillsPlate.setVisible(visible);
	}
	
	private void setAsSelected(){
		((GameplayScreen)Screens.GAMEPLAY.getScreen()).setSelectedCrew(crew);
		crew.setSelected(true);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
////		Texture back = GameplayScreen.highlight;
		Color color = batch.getColor().cpy();

////		//batch.draw(back, getX(), getY(), getWidth(), getHeight());
		bar.setColor(Color.GREEN);
		bar.setBounds(bar.getX(), bar.getY(),(getWidth()-crewImg.getWidth())*(crew.getHealth()/crew.getRace().getHealth()), 5);
		
		if(crew.isSelected()){
			table.setColor(Color.DARK_GRAY);
		}else{
			table.setColor(Color.GRAY);
		}
		super.draw(batch, parentAlpha);
		batch.setColor(color);
////		TextureRegion icon = crew.getIcon();
////		batch.draw(icon, getX(), getY(),getWidth(), getHeight());
	}
	
	
}
