package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.deeper.than.DTL;
import com.deeper.than.DTLEvent;
import com.deeper.than.Response;

public class UIEventPopUp<T extends Actor> extends UIPopUpWindow<T> {

	private DTLEvent event;
	Json json;
	
	public UIEventPopUp(T actor, String eventString) {
		super(actor);
		this.setFillParent(true);
		try{
			//json = new Json();
			//event = json.fromJson(DTLEvent.class, Gdx.files.internal("events/" + eventString + ".event"));
			ArrayList<Response> testList = new ArrayList<Response>();
			testList.add(new Response("Okay", null));
			event = new DTLEvent("test", "This is a test, don't worry", testList);
			
			Label title = new Label(event.getTitle(), DTL.skin);
			super.add(title).left().top();
			super.row();
			Table innerTable = new Table();
			innerTable.pad(50);
			innerTable.add( new Label(event.getText(), DTL.skin)).left();
			innerTable.row();
			int i = 0;
			for( Response resp: event.getInputs()){
				i++;
				innerTable.add(new TextButton(i + ": " + resp.getInputText(), DTL.skin));
				innerTable.row();
			}
			super.add(innerTable);
		} catch (Exception e){
			System.out.println("Failed to find event file");
			e.printStackTrace();
		} finally {
			
		}
	}

}
