/**
 * Bar on the left that holds all crew plates
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.DTL;
import com.deeper.than.crew.Crew;

/**
 * Crew plate bar that holds all crew members of a ship
 * @author zach
 *
 */
public class UICrewPlateBar extends WidgetGroup{
	
	private ArrayList<CrewPlate> crewPlates;
	private Table table;
	
	public UICrewPlateBar(ArrayList<Crew> crew){
		crewPlates = new ArrayList<CrewPlate>();
		CrewPlate crewPlate;
		
		table = new Table();
		table.setFillParent(true);
		//create plates and add to bar
		for(Crew c : crew){
			crewPlate = new CrewPlate(c);
			crewPlates.add(crewPlate);
			
		}
		layoutTable();
		this.addActor(table);
	}
	
	public void update(){
		Iterator<CrewPlate> iter = crewPlates.iterator();
		CrewPlate cp;
		boolean reLayout = false;
		while(iter.hasNext()){
			cp = iter.next();
			
			if(!cp.getCrew().isAlive()){
				reLayout = true;
				iter.remove();
			}
		}
		
		if(reLayout){
			layoutTable();
		}
		
	}
	
	/**
	 * Lays out hte table 
	 */
	public void layoutTable(){
		table.clear();
		Table crewTable = new Table();
		for(CrewPlate cp : crewPlates){
			crewTable.add(cp).prefWidth((Crew.CREW_HEIGHT/Crew.SCALE)+50+10).minHeight(Crew.CREW_HEIGHT/Crew.SCALE+10).left().pad(1f);
			crewTable.row();
		}
		table.add(crewTable).expand().left().fill();
		table.add().expand().fill().prefWidth(DTL.VWIDTH);
		table.row();
		table.add().prefHeight(DTL.VHEIGHT/2).colspan(2);
	}

}
