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
public class UIPopUpWindow<T extends Actor> extends Table{
	
	public  UIPopUpWindow(T actor){
		this.setFillParent(true);
		Container<T> cont = new Container<T>(actor);
		cont.fill();
		cont.background(new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch));
		this.add(cont).fill().expand();
		this.pad(100);
	}
}
