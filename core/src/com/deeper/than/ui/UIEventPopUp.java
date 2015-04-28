package com.deeper.than.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deeper.than.DTL;
import com.deeper.than.DTLEvent;

public class UIEventPopUp<T extends Actor> extends UIPopUpWindow<T> {

	private DTLEvent event;
	
	public UIEventPopUp(T actor, DTLEvent event) {
		super(actor);
		Label title = new Label(event.getTitle(), DTL.skin);
		this.add(title).top().left();
		this.row();
	}

}
