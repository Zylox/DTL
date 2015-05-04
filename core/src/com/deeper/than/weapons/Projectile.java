/**
 * 
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
 * @author zach
 *
 */
public class Projectile extends Image {

	private Vector2 destination;
	private Room target;
	
	public Projectile(Texture tex, float width, float height){
		super(tex);
		this.setWidth(width);
		this.setHeight(height);
		destination = new Vector2();
		this.target = null;
	}
	
	public void setColor(Color color){
		super.setColor(color);
	}
	
	public void setStart(Vector2 start){
		this.setX(start.x);
		this.setY(start.y);
	}
	
	public void setDestination(Vector2 destination, WeaponHitAction hitAction, float timeToGetThere){
//		Vector2 testV = new Vector2(300,300);
//		Vector2 endPoint = new Vector2(DTL.VWIDTH-100,DTL.VHEIGHT-100);
//	    test.setBounds(testV.x, testV.y, 10, 3);
//	    test.setColor(Color.RED);
//	    test.setOrigin(Align.center);
//	    test.setRotation(endPoint.cpy().sub(testV).angle());
//	    
//	    test.addAction(Actions.moveTo(endPoint.x, endPoint.y,3));
		Vector2 dirVec = destination.cpy().sub(this.getX(),this.getY());
		this.setRotation(dirVec.angle());
		if(!hitAction.hit){
			dirVec.scl(2);
			timeToGetThere *= 2;
			destination = dirVec.add(this.getX(),this.getY());
		}
		this.destination = destination;
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
