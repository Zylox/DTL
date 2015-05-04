/**
 * A new type of screen that has delayed loading of improtatn things to let graphical context load
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.screens;

import com.badlogic.gdx.Screen;
import com.deeper.than.DTL;
/**
 * 
 * @author zach
 *
 */
public interface EnumerableScreen extends Screen {
	public void create(DTL game);
}
