/**
 * Action for a projectile that determines what happens upon hit
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.weapons;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.deeper.than.Room;
import com.deeper.than.modules.Cooldown;

/**
 * @author zach
 *
 */
public class WeaponHitAction extends Action {

	private static final float TIMEOUT_SECS = 10;
	
	private Weapon weapon;
	protected boolean hit;
	private Room target;
	private Cooldown cooldown;
	
	public WeaponHitAction(Weapon weapon, boolean hit, Room target) {
		this.weapon = weapon;
		this.hit = hit;
		this.target = target;
		this.cooldown = new Cooldown(TIMEOUT_SECS);
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Action#act(float)
	 */
	@Override
	public boolean act(float delta) {
		if(hit){
			weapon.onhit(target);
		}else{
			target.getShip().shotEvaded();
		}
		return true;
	}
}
