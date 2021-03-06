/**
 * Module for shield charing and managemetn
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.CrewSkills.CrewSkillsTypes;

/**
 * Sheild module to be used by ship
 * @author zach
 *
 */
public class SheildModule extends MainModule {
	public static final float COOLDOWN_MAX = 100;
	public static final float COOLDOWN_INC_BASE = 50;
	public static Texture sheildTex = null;
	public static final int MAX_SHEILD_SECTIONS = 4;
	
	private int activeCount;
	private Cooldown coolD;
	
	/**
	 * Creates a Sheild module at level 1
	 * @param id module id
	 * @param room room containing the module. Set to null if no room contains it
	 * @param ship ship containing the module. Set to null if no ship owns the module
	 */
	public SheildModule(int id, int maxLevel,Room room, Ship ship) {
		super(id, maxLevel, room, ship);
		activeCount = getSheildLayerCount();
		coolD = new Cooldown();
		manable = true;
	}
	
	/**
	 * Creates a Sheild module at specified level
	 * @param id module id
	 * @param level level of the module
	 * @param room room containing the module. Set to null if no room contains it
	 * @param ship ship containing the module. Set to null if no ship owns the module
	 */
	public SheildModule(int id, int level, int maxLevel, Room room, Ship ship) {
		super(id, level, maxLevel, room, ship);
		activeCount = getSheildLayerCount();
		coolD = new Cooldown(COOLDOWN_MAX);
		manable = true;
	}
	

	public void takeSheildDamage(){
		takeSheildDamage(1);
	}
	
	public void takeSheildDamage(int dmgAmt){
		activeCount -= dmgAmt;
		if(activeCount <0){
			activeCount = 0;
		}
		coolD.startCooldown();
		if(this.isManned()){
			getManning().gainExp(1, CrewSkillsTypes.SHIELDS);
		}
	}
	
	@Override
	public void update(){
		super.update();
		//update sheilds if on cooldown
		
		//if the sheildcoutn is full, end the cycling cooldown
		if(activeCount > getSheildLayerCount()-1){
			activeCount= getSheildLayerCount();
			coolD.endCooldown();
		}else if(activeCount < getSheildLayerCount()){
			if(!coolD.isOnCooldown()){
				coolD.startCooldown();
			}else{
				coolD.advanceCooldown(getSheildCooldownSpeed());
				if(!coolD.isOnCooldown()){
					activeCount++;
					if(activeCount != getSheildLayerCount()){
						//start new cooldown if there are still sheilds to restore
						coolD.startCooldown();
					}
				}
			}
		}
	}
	
	private float getSheildCooldownSpeed(){
		float rate = COOLDOWN_INC_BASE;
		if(isManned()){
			rate *= 1 + this.getManning().getSheildRechargeRatio();
		}
		return rate;
	}
	
	public float getCooldownProgress(){
		if(coolD.isOnCooldown()){
			return coolD.getCooldownProgress();
		}
		return 0;
	}
	
	public int getActiveSheildLayers(){
		return activeCount;
	}
	
	public int getSheildLayerCount(){
		return getPowerLevel()/2;
	}
	
	public Texture getSheildImage(){
		if(sheildTex == null){
			sheildTex = new Texture(Gdx.files.internal("sheild.png"));
		}
		return sheildTex;
	}	
	
}
