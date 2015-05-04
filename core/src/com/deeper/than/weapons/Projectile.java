/**
 * A projectile fired from a projectile weapon 
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deeper.than.Room;

/**
 * Projectile fired from a projectile weapon
 * @author zach
 *
 */
public class Projectile extends Image {

	
	public Projectile(Texture tex, float width, float height){
		super(tex);
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void setColor(Color color){
		super.setColor(color);
	}
	
	public void setStart(Vector2 start){
		this.setX(start.x);
		this.setY(start.y);
	}
	
	public void setDestination(Vector2 destination, WeaponHitAction hitAction, float timeToGetThere){
		//calculate angle of projectile
		Vector2 dirVec = destination.cpy().sub(this.getX(),this.getY());
		this.setRotation(dirVec.angle());
		//if it didnt hit, make the final target farther away to make it keep going
		if(!hitAction.hit){
			dirVec.scl(2);
			timeToGetThere *= 2;
			destination = dirVec.add(this.getX(),this.getY());
		}
		//setactions to move to the destination then clean itself up
		this.addAction(Actions.sequence(Actions.moveTo(destination.x, destination.y, timeToGetThere),hitAction,new Action() {
			@Override
			public boolean act(float delta) {
				cleanup();
				return true;
			}
		}));

	}
	
	public void cleanup(){
		this.remove();
		this.clear();
	}
}
