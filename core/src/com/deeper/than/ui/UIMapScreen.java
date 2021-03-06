package com.deeper.than.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class UIMapScreen extends Table {
	private static Texture visitedNode;
	private static Texture currentNode;
	private static Texture unvisitedNode;
	private static Texture reachableNode;
	
	public static void loadAssets(){
		visitedNode = new Texture(Gdx.files.internal("nodes/v_point.png"));
		currentNode = new Texture(Gdx.files.internal("nodes/c_point.png"));
		unvisitedNode = new Texture(Gdx.files.internal("nodes/u_point.png"));
		reachableNode = new Texture(Gdx.files.internal("nodes/r_point.png"));
	}
	
	public static Texture getVisitedNodeTexture(){
		return visitedNode;
	}
	
	public static Texture getCurrentNodeTexture(){
		return currentNode;
	}
	
	public static Texture getUnvisitedNodeTexture(){
		return unvisitedNode;
	}
	
	public static Texture getReachableNodeTexture(){
		return reachableNode;
	}
}
