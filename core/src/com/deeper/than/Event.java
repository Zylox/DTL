package com.deeper.than;

import java.util.ArrayList;
/* Class:	Event object
 * Author:	Nick
 */

public class Event {
	private String	title;
	private String	text;
	private ArrayList<String> inputs;
	
	public Event(String title, String text, ArrayList<String> inputs){
		setTitle(title);
		setText(text);
		setInputs(inputs);
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void setInputs(ArrayList<String> inputs){
		this.inputs = inputs;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getText(){
		return this.text;
	}
	
	public ArrayList<String> getInputs(){
		return this.inputs;
	}
}
