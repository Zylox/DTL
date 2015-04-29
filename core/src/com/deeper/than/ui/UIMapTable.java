package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.DTL;
import com.deeper.than.DTLMap;
import com.deeper.than.DTLMapPoint;

public class UIMapTable extends Table {
	UIPopUpWindow<UIMapTable> parent;
	DTLMap map;
	
	public UIMapTable(DTLMap map){
		this.setDebug(DTL.GLOBALDEBUG);
		this.setFillParent(true);
		this.parent = null;
		this.map=map;
	}
	
	public void setParent(UIPopUpWindow<UIMapTable> parent){
		this.parent = parent;
	}
	
	public void setUpMapUI(){
		if(this.parent==null){
			System.out.println("Parent must be set first");
			return;
		}
		this.setFillParent(true);
		
		
		Label title = new Label("Map, level: " + map.getLevel(), DTL.skin);
		this.add(title).left().top();
		this.row();
		TextButton exit = new TextButton("X", DTL.skin);
		
		ClickListener clickListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				parent.setVisible(false);
			}
		};
		exit.addListener(clickListener);
		this.add(exit).right().top();
		this.row();
		
		Table innerTable = new Table();
		innerTable.setDebug(DTL.GLOBALDEBUG);
		innerTable.setFillParent(true);
		innerTable.scaleBy(1f);
		
		
		Texture u_node = UIMapScreen.getUnvisitedNodeTexture();
		Texture v_node = UIMapScreen.getVisitedNodeTexture();
		Texture c_node = UIMapScreen.getCurrentNodeTexture();
		int i=0;
		
		ArrayList<Image> img = new ArrayList<Image>();
		
		for(DTLMapPoint mp: map.getNodes()){
			float x = (float) mp.getX();
			float y = (float) mp.getY();
			
			if(i==map.getCurrentNode()){
				img.add( new Image(c_node));
			} else if (mp.isVisited()) {
				img.add(new Image(v_node));
			} else {
				img.add(new Image(u_node));
			}
			//innerTable.moveBy(x, y);
			img.get(i).setX(x);
			img.get(i).setY(y);
			innerTable.add(img.get(i)).padTop(y/2.5f).padLeft(10);
			//innerTable.add(img).padTop(y/2f).padLeft(x/2f);
			//innerTable.row();
			
			i++;
		}

		
		
		this.add(innerTable).expand().fill();
		this.add().prefHeight(DTL.VHEIGHT);
		this.row();
	}
}
