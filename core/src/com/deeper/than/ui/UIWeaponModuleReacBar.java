/**
 * Recator bar for a specific all weapons
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.WeaponsModule;
import com.deeper.than.weapons.Weapon;

/**
 * Tracks power assignments across mulitple weapons
 * @author zach
 *
 */
public class UIWeaponModuleReacBar extends UIModuleReactorBar{

	private ArrayList<WeaponPowerPair> powerAssignments;
	
	/**
	 * @param powered
	 * @param icon
	 * @param mainPower
	 * @param module
	 */
	public UIWeaponModuleReacBar(int powered, UIPowerBar mainPower, WeaponsModule module) {
		super(powered, Modules.getIcon(WeaponsModule.class.getCanonicalName()), mainPower, module);
		powerAssignments = new ArrayList<WeaponPowerPair>();
		//callback to check and update power assignments
		this.addAction(new Action() {
			
			@Override
			public boolean act(float delta) {
				checkPowerAssignments();
				return false;
			}
		});
	}
	
	public void addToQueue(Weapon weapon){
		this.powerAssignments.add(new WeaponPowerPair(weapon, 0));
	}
	
	public void removeFromQueue(Weapon weapon){
		Iterator<WeaponPowerPair> iter = powerAssignments.iterator();
		WeaponPowerPair wpp;
		while(iter.hasNext()){
			wpp = iter.next();
			if(wpp.getWeapon().equals(weapon)){
				wpp.getWeapon().setPower(0);
				iter.remove();
				return;
			}
		}
	}
	
	public void checkPowerAssignments(){
		WeaponPowerPair pair;
		//if weapons doesnt want power anymore take it from it
		for(int i = 0; i< powerAssignments.size(); i++){
			pair = powerAssignments.get(i);
			if(!pair.getWeapon().doesWantPower()){
				stripPower(i);
			}
		}
		
		int avaliable = getAvailablePower();
		//if there is more power divied than avaliable, take some back
		if(avaliable < 0){
			int reclaimedPower = 0;
			for(int i = powerAssignments.size(); i>=0;i--){
				if(avaliable+reclaimedPower >=0){
					break;
				}
				reclaimedPower += stripPower(i);
			}
		}
		
		//if there is available power, give some
		if(avaliable > 0){
			int cost = 0;
			for(WeaponPowerPair wpp : powerAssignments){
				//if it wants power, butisnt
				if(wpp.getWeapon().doesWantPower() && !wpp.getWeapon().isPowered()){
					//and this has enough power to give it, give it power
					if(avaliable >= wpp.getWeapon().getPowerCost()){
						cost = wpp.getWeapon().getPowerCost(); 
						wpp.setPower(cost);
						wpp.getWeapon().setPower(cost);
						avaliable -= cost;
						
					}
				}
			}
		}
	}
	
	public int stripPower(Weapon weapon){
		int strippedAmount = 0;
		for(WeaponPowerPair wpp : powerAssignments){
			if(wpp.getWeapon() == weapon){
				strippedAmount = wpp.getPower();
				wpp.setPower(0);
				weapon.setPower(0);
			}
		}
		return strippedAmount;
	}
	
	private int stripPower(int i){
		int strippedAmount = 0;
		if(i<powerAssignments.size()){
			strippedAmount = powerAssignments.get(i).getPower();
			powerAssignments.get(i).setPower(0);
			powerAssignments.get(i).getWeapon().setPower(0);
		}
		return strippedAmount;
	}
	
	private int getAvailablePower(){
		int count = 0;
		for(WeaponPowerPair wpp : powerAssignments){
			count += wpp.getPower();
		}
		return this.getPowered() - count;
	}
	
	/**
	 * Weapon and the power being tracked
	 * @author zach
	 *
	 */
	private class WeaponPowerPair{
		private Weapon weapon;
		private int power;
		
		public WeaponPowerPair(){
			this.weapon = null;
			this.power = 0;
		}
		
		public WeaponPowerPair(Weapon weapon){
			this.weapon = weapon;
			this.power = 0;
		}
		
		public WeaponPowerPair(Weapon weapon, int power){
			this.weapon = weapon;
			this.power = 0;
		}
		
		public void setPower(int power){
			this.power = power;
		}

		public Weapon getWeapon() {
			return weapon;
		}

		public void setWeapon(Weapon weapon) {
			this.weapon = weapon;
		}

		public int getPower() {
			return power;
		}
		
		
		
	}
}
