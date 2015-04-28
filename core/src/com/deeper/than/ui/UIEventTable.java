package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.deeper.than.DTL;
import com.deeper.than.DTLEvent;
import com.deeper.than.Response;

public class UIEventTable extends Table {
	private DTLEvent event;
	Json json;
	public UIEventTable(DTLEvent event){
		this.setFillParent(true);
		this.setDebug(true);
			
		Label title = new Label(event.getTitle(), DTL.skin);
		this.add(title).left().top();
		this.row();
		Table innerTable = new Table();
		innerTable.setDebug(true);
		innerTable.pad(35);
		innerTable.add().prefHeight(DTL.VHEIGHT / 5);
		innerTable.row();
		innerTable.add( new Label(event.getText(), DTL.skin)).left().expand();
		innerTable.row();
		innerTable.add().prefHeight(DTL.VHEIGHT);
		innerTable.row();
		int i = 0;
		for( Response resp: event.getInputs()){
			i++;
			innerTable.add(new TextButton(i + ": " + resp.getInputText(), DTL.skin));
			innerTable.row();
		}
		this.add(innerTable).expand().fill();

	}
}
