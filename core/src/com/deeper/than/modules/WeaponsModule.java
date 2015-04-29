package com.deeper.than.modules;

import java.util.ArrayList;

import com.deeper.than.Room;
import com.deeper.than.Ship;
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

	public boolean equipWeapon(Weapon weapon){
		if(equippedWeapons.size() < this.getShip().getMaxWeapons()){
			equippedWeapons.add(weapon);
			return true;
		}
		return false;
	}
	
	public void unEquipWeapon(Weapon weapon){
		equippedWeapons.remove(weapon);
	}

}
