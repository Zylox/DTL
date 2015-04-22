package com.deeper.than.modules;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deeper.than.DTL;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.Crew;
import com.deeper.than.crew.Crew.CrewState;

public abstract class Module {
	
	private final String name = "baseModule";

	public static final float IONIC_COOLDOWN_PER_SEC = 50;
	public static final float IONIC_CHARGE_COOLDOWN_PER_CHARGE = 100;
	public static final float BASE_REPAIR_PER_SEC = 50;
	
	private int id;
	private Ship ship;
	private Room room;
	private int minRoomSize;
	private int level;
	private int maxLevel;
	private int ionicCharges;
	private int damage;
	private Cooldown ionicCooldown;
	private Cooldown repairCooldown;
	private int powerLevel;
	private Crew manning;
	private Crew repairing;
	protected boolean manable;
	protected boolean isOnLockdown;
	
	public Module(int id, int maxLevel,Room room, Ship ship){
		this(id, 1, maxLevel, room, ship);
	}
	
	public Module(int id, int level, int maxLevel, Room room, Ship ship){
		this.id = id;
		this.maxLevel = maxLevel;
		this.level = level;
		this.room = room;
		this.ship = ship;
		ionicCharges = 0;
		damage = 0;
		ionicCooldown = new Cooldown();
		repairCooldown = new Cooldown();
		powerLevel = 0;
		manning = null;
		manable = false;
		repairing = null;
		isOnLockdown = false;
	}
	
	public void update(){
		ionicCooldown.setCooldownLimit(Math.max(1,ionicCharges) * IONIC_CHARGE_COOLDOWN_PER_CHARGE);
		if(ionicCooldown.isOnCooldown()){
			ionicCooldown.advanceCooldown(IONIC_COOLDOWN_PER_SEC);
			if(!ionicCooldown.isOnCooldown()){
				ionicCharges = 0;
				isOnLockdown = false;
			}
		}
		if(repairCooldown.isOnCooldown()){
			repairCooldown.advanceCooldown(getRepairSpeed());
			if(!repairCooldown.isOnCooldown()){
				damage--;
				if(damage > 0){
					repairCooldown.startCooldown();
				}else{
					repairing.setRepairing(false);
					repairing = null;
				}
			}
		}
		
		
	}

	private float getRepairSpeed(){
		if(repairing != null){
			if(repairing.getState() == CrewState.REPAIRING){
				return repairing.getRepairRatio() * BASE_REPAIR_PER_SEC;
			}
		}
		
		ArrayList<Crew> repairCands = room.getRepairCandidates();
		if(repairCands.size() == 0){
			return 0;
		}
		Crew repairer = null;
		for(Crew c : repairCands){
			if(c.getState() == CrewState.IDLE || c.getState() == CrewState.MANNING){
				if(repairer == null){
					repairer = c;
				}else{
					if(c.getRepairRatio() > repairer.getRepairRatio()){
						repairer = c;
					}
				}
			}
		}
		if(repairer == null){
			return 0;
		}
		repairing = repairer;
		repairing.setRepairing(true);
		return repairer.getRepairRatio() * BASE_REPAIR_PER_SEC;
	}
	
	public void draw(Batch batch){
		Sprite icon = Modules.getIcon(this.getClass().getCanonicalName()); 
		Color color = batch.getColor().cpy();
		if(this.getDamage()>0){
			batch.setColor(Color.RED);
		}
		if(this.getIonicCharges()>0){
			batch.setColor(Color.YELLOW);
		}
		batch.draw(icon, room.getCenterLoc().x-icon.getWidth()/2, room.getCenterLoc().y-icon.getHeight()/2);
		batch.setColor(color);
	}
	
	public void receiveIonicCharge(){
		receiveIonicCharge(1);
	}
	
	public void receiveIonicCharge(int amt){
		
		ionicCharges += amt;
		if(ionicCharges > getLevel()){
			ionicCharges = getLevel();
		}
		ionicCooldown.startCooldown();
		isOnLockdown = true;
		if(manning != null){
			manning = null;
		}
	}
	
	
	public float getIonicCooldownProg() {
		return ionicCooldown.getCooldownProgress();
	}
	
	public float getIonicCooldownMax(){
		return ionicCooldown.getCooldownLimit();
	}

	public boolean isOnLockdown() {
		return isOnLockdown;
	}

	public void recieveDamage(){
		recieveDamage(1);
	}
	
	public void recieveDamage(int amt){
		if(amt < 1){
			return;
		}
		damage += amt;
		if(damage > getLevel()){
			damage = getLevel();
		}
		repairCooldown.startCooldown();
	}

	public int getPowerLevel() {
		return powerLevel;
	}

	public void setPowerLevel(int powerLevel) {
		this.powerLevel = powerLevel;
	}

	public boolean isManned() {
		return manning == null ? false : true;
	}

	public Crew getManning() {
		return manning;
	}

	public void setManning(Crew manning) {
		this.manning = manning;
	}
	
	public boolean isManable(){
		return manable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getMinRoomSize() {
		return minRoomSize;
	}
	
	public String getName(){
		return name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if(level > maxLevel){
			level = maxLevel;
		}
		this.level = level;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getIonicCharges() {
		return ionicCharges;
	}

	public int getDamage() {
		return damage;
	}
	
	
	
}
