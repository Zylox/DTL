package com.deeper.than;

public class Response {
	private String inputText; //text that shows up for the user to choose this response
	private String triggersEvent; //the title of the next event this response triggers (or null)
	
	public Response(String inputText, String triggersEvent){
		setInputText(inputText);
		setTriggersEvent(triggersEvent);
	}
	
	public String getInputText() {
		return inputText;
	}
	
	public String getTriggersEvent(){
		return triggersEvent;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}
	
	public void setTriggersEvent(String triggersEvent){
		this.triggersEvent = triggersEvent;
	}
}
