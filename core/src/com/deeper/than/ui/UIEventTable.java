package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.deeper.than.DTL;
import com.deeper.than.DTLEvent;
import com.deeper.than.Response;

public class UIEventTable extends Table {
	private DTLEvent event;
	Json json;
	private Table table = this;
	UIPopUpWindow<UIEventTable> parent;
	
	
	public UIEventTable(String eventString){
		this(getDTLEventFromFile(eventString));
	}
	
	public UIEventTable(DTLEvent event){
		this.setFillParent(true);
		this.setDebug(DTL.GLOBALDEBUG);
		this.event = event;
	}
	
	public void setUpUI(){
		parent.setVisible(true);
		Label title = new Label(event.getTitle(), DTL.skin);
		this.add(title).left().top();
		this.row();
		Table innerTable = new Table();
		innerTable.setDebug(DTL.GLOBALDEBUG);
		innerTable.pad(35);
		innerTable.add().prefHeight(DTL.VHEIGHT / 5);
		innerTable.row();
		Label label  = new Label(event.getText(), DTL.skin);
		label.setWrap(true);
//		label.setWidth(100);
		innerTable.add(label).left().expand().width(DTL.VWIDTH/2);
		innerTable.row();
		innerTable.add().prefHeight(DTL.VHEIGHT);
		innerTable.row();
		int i = 0;
		for( final Response resp: event.getInputs()){
			i++;
			TextButton button = new TextButton(i + ": " + resp.getInputText(), DTL.skin);
			//TODO add listeners to the buttons here, make them do things??
			ClickListener clickListener=null;
			if(resp.getTriggersEvent()==null){
				clickListener = new ClickListener(){
					public void clicked(InputEvent event, float x, float y){
						parent.setVisible(false);
					} 
				};
			} else if(resp.getTriggersEvent().endsWith(".event")){
				clickListener = new ClickListener(){
					public void clicked(InputEvent event, float x, float y){
						int length = resp.getTriggersEvent().length();
						UIEventTable newPopUp = new UIEventTable(resp.getTriggersEvent().substring(0, length-6));
						newPopUp.setParent(parent);
						newPopUp.setUpUI();
						parent.loadNewChild((newPopUp));
					}
				};
			}
			button.addListener(clickListener);
			innerTable.add(button);
			innerTable.row();
		}
		this.add(innerTable).expand().fill();

	}
	
	public static DTLEvent getDTLEventFromFile(String eventString){
		
		try{
			FileHandle fh = Gdx.files.internal("events/"+eventString+".event");
			String jsonString = fh.readString();
			JsonValue root = new JsonReader().parse(jsonString);
			String eventTitle = root.getString("title");
			String eventText = root.getString("text");
			ArrayList<Response> eventResponseList = new ArrayList<Response>();
			JsonValue jv = root.get("responses");
			for(JsonValue resp : jv){
				String respText = resp.getString("inputText");
				String respTrig = resp.getString("triggersEvent");
				eventResponseList.add(new Response(respText, respTrig));
			}
			return new DTLEvent(eventTitle, eventText, eventResponseList);
		} catch (Exception e){
			System.out.println("Failed to get event from JSON");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setParent(UIPopUpWindow<UIEventTable> parent){
		this.parent = parent;
	}
}
