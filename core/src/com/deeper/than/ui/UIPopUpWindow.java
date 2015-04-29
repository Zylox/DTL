
/**
 * 
 */
package com.deeper.than.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * @author zach
 *
 */
public class UIPopUpWindow<T extends Actor> extends Table {

	private Container<T> cont;
	private NinePatchDrawable bg;
	
	public UIPopUpWindow(T actor) {
		this.setFillParent(true);
		cont = new Container<T>(actor);
		cont.fill();
		bg = new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch);
		cont.background(bg);
		addContToTable();
	}
	
	public void loadNewChild(T t){
		cont = new Container<T>(t);
		cont.fill();
		cont.background(bg);
		this.clear();
		addContToTable();
	
	}
	
	private void addContToTable(){
		this.add(cont).fill().expand();
		this.pad(100);
	}
}
