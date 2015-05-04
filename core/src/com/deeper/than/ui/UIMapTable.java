package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.deeper.than.DTL;
import com.deeper.than.DTLMapPoint;

public class UIMapTable extends Table {
	UIPopUpWindow<UIMapTable> parent;
	ArrayList<Integer> reachableNodes;
	
	public UIMapTable(){
		this.setDebug(DTL.GLOBALDEBUG);
		this.setFillParent(true);
		this.parent = null;
		this.reachableNodes = DTL.MAP.getReachableNodes();
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
		
		//set up top area
		Label title = new Label("Map, level: " + DTL.MAP.getLevel(), DTL.skin);
		this.add(title).left().top();
		this.row();
		TextButton exit = new TextButton("X", DTL.skin);
		
		ClickListener clickListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y){
				parent.setVisible(false);
			}
		};
		exit.addListener(clickListener);
		this.add(exit).expand().right().top();
		this.row();
		
		//set up the actual map part of the pop up
		Table innerTable = new Table();
		innerTable.setDebug(DTL.GLOBALDEBUG);
		innerTable.setFillParent(true);
		innerTable.scaleBy(1f);
		
		//retrieve textures
		Texture u_node = UIMapScreen.getUnvisitedNodeTexture();
		Texture v_node = UIMapScreen.getVisitedNodeTexture();
		Texture c_node = UIMapScreen.getCurrentNodeTexture();
		Texture r_node = UIMapScreen.getReachableNodeTexture();
		int i=0;
		
		
		ArrayList<Image> img = new ArrayList<Image>();
		ArrayList<TextButton> buttons = new ArrayList<TextButton>();
		img.clear();
		buttons.clear();
		
		//loop over all the map nodes
		for(final DTLMapPoint mp: DTL.MAP.getNodes()){
			Circle test = new Circle();
			float x = (float) mp.getX();
			float y = (float) mp.getY();
			test.radius=5;
			test.x=x;
			test.y=y;
			TextButton button = null;
			final int num = i;
			
			
			//decide which texture a point is
			if(i==DTL.MAP.getCurrentNode()){
				img.add( new Image(c_node)); //current: blue
			} else if (reachableNodes.contains(i)){
				img.add(new Image(r_node)); //reachable: white
				//add click-able functionality to the nodes
				button = new TextButton("go", DTL.skin);
				ClickListener clickListener2 = new ClickListener(){
					public void clicked(InputEvent event, float x, float y){
						DTL.MAP.setCurrentNode(num);
						parent.setVisible(false);
						DTL.MAP.setAdvance(true);
					}
				};
				button.addListener(clickListener2);
				buttons.add(button);
			} else if (mp.isVisited()) {
				img.add(new Image(v_node)); //visited nodes: green
				
			} else {
				img.add(new Image(u_node)); //unvisited nodes: red
			}
			innerTable.setX(x);
			innerTable.setY(y);
			this.add(img.get(i)).expand().padTop((y/1f)-300);
			this.add(button).expand().padTop((y/1f)-300);
			
			i++;
		}
		
		this.add(innerTable).expand().fill();
		this.add().prefHeight(DTL.VHEIGHT);
		this.row();
	}
}
