package com.deeper.than.crew;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.BinaryHeap.Node;
import com.deeper.than.ScriptParser;

public class TileBinaryNode extends Node {
	
	private Vector2 pos;
	
	
	public TileBinaryNode(float value, Vector2 pos) {
		super(value);
		this.pos = pos;
	}

	public Vector2 getPos() {
		return pos;
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}
	
	

}
