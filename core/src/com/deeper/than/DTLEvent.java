package com.deeper.than;

import java.util.ArrayList;
/* Class:	Event object
 * Author:	Nick
 */

public class DTLEvent {
	private String	title;
	private String	text;
	private ArrayList<Response> responses;
	
	public Event(String title, String text, ArrayList<Response> responses){
		setTitle(title);
		setText(text);
		setResponses(responses);
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void setResponses(ArrayList<Response> responses){
		this.responses = responses;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getText(){
		return this.text;
	}
	
	public ArrayList<Response> getInputs(){
		return this.responses;
	}
}
