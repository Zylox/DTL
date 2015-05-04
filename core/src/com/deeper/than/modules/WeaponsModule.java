/**
 * Moduel that controls weapons
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.modules;

import java.util.ArrayList;

import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.CrewSkills.CrewSkillsTypes;
import com.deeper.than.weapons.Weapon;

public class WeaponsModule extends MainModule {

	private ArrayList<Weapon> equippedWeapons;
	
	public WeaponsModule(int id, int maxLevel, Room room, Ship ship) {
		super(id, maxLevel, 1, room, ship);
		equippedWeapons = new ArrayList<Weapon>();
		manable = true;
	}
	
	public WeaponsModule(int id, int maxLevel, int level, Room room, Ship ship) {
		super(id, maxLevel, level, room, ship);
		equippedWeapons = new ArrayList<Weapon>();
		manable = true;
	}
	
	public ArrayList<Weapon> getEquippedWeapons(){
		return equippedWeapons;
	}

	/**
	 * Tries to equip weapon
	 * @param weapon
	 * @return success or failure
	 */
	public boolean equipWeapon(Weapon weapon){
		//wotn equip if list is full
		if(equippedWeapons.size() < this.getShip().getMaxWeapons()){
			equippedWeapons.add(weapon);
			return true;
		}
		return false;
	}
	
	public void unEquipWeapon(Weapon weapon){
		equippedWeapons.remove(weapon);
	}
	
	/**
	 * Gives exp to crew mannign weapons if it exists
	 */
	public void giveShotExp(){
		if(this.isManned()){
			this.getManning().gainExp(1, CrewSkillsTypes.WEAPONS);
		}
	}

}
