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
	public UIEventTable(String eventString){
		this.setFillParent(true);
		this.setDebug(true);
		try{
			json = new Json();
			FileHandle fh = Gdx.files.internal("events/"+eventString+".event");
			String jsonString = fh.readString();
			JsonValue root = new JsonReader().parse(jsonString);
			String eventTitle = root.getString("title");
			String eventText = root.getString("text");
			
			//event = json.fromJson(DTLEvent.class, jsonString);
			ArrayList<Response> testList = new ArrayList<Response>();
			testList.add(new Response("Okay", null));
			testList.add(new Response("Noooo waaayyyy", null));
			//event = new DTLEvent("test", "This is a test, don't worry", testList);
			//event.setResponses(testList);
			//json.fromJson(event.getClass(), jsonString);
			event = new DTLEvent(eventTitle, eventText, testList);
			
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
		} catch (Exception e){
			System.out.println("Failed to find event file");
			e.printStackTrace();
		} finally {
			
		}
	}
}
