package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.deeper.than.DTL;
import com.deeper.than.DTLMap;
import com.deeper.than.DTLMapPoint;

public class UIMapTable extends Table {
	UIPopUpWindow<UIMapTable> parent;
	DTLMap map;
	ArrayList<Integer> reachableNodes;
	
	public UIMapTable(DTLMap map){
		this.setDebug(DTL.GLOBALDEBUG);
		this.setFillParent(true);
		this.parent = null;
		this.map=map;
		this.reachableNodes = map.getReachableNodes();
	}
	//this method sets the parent, which is necessary to destroy the pop up window
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
		Texture r_node = UIMapScreen.getReachableNodeTexture();
		int i=0;
		
		ArrayList<Image> img = new ArrayList<Image>();
		ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
		
		for(DTLMapPoint mp: map.getNodes()){
			Circle test = new Circle();
			float x = (float) mp.getX();
			float y = (float) mp.getY();
			test.radius=5;
			test.x=x;
			test.y=y;
			ImageButton button = null;
			
			
			
			if(i==map.getCurrentNode()){
				img.add( new Image(c_node));
			} else if (reachableNodes.contains(i)){
				img.add(new Image(r_node));
			} else if (mp.isVisited()) {
				img.add(new Image(v_node));
				button = new ImageButton((Drawable) img.get(i));
				ClickListener clickListener2 = new ClickListener(){
					public void clicked(InputEvent event, float x, float y){
						parent.setVisible(false);
					} 
				};
				button.addListener(clickListener2);
				buttons.add(button);
			} else {
				img.add(new Image(u_node));
			}
			innerTable.setX(x);
			innerTable.setY(y);
			innerTable.add(img.get(i)).expand().padTop((y/1f)-300);
			if(button != null)
				innerTable.add(button);
			
			i++;
		}
		
		this.add(innerTable).expand().fill();
		this.add().prefHeight(DTL.VHEIGHT);
		this.row();
	}
}
