package com.deeper.than.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deeper.than.DTL;
import com.deeper.than.DTLMap;

public class UIMapTable extends Table {
	public UIMapTable(DTLMap map){
		this.setFillParent(true);
		this.setDebug(DTL.GLOBALDEBUG);
		
		Label title = new Label("Map, level: " + map.getLevel(), DTL.skin);
		this.add(title).left().top();
		this.row();
		
		Table innerTable = new Table();
		innerTable.setDebug(DTL.GLOBALDEBUG);
		innerTable.pad(15);

	}
}
