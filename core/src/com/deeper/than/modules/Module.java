package com.deeper.than.modules;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.deeper.than.Room;
import com.deeper.than.Ship;
import com.deeper.than.crew.Crew;

public abstract class Module {
	
	private final String name = "baseModule";

	public static final float IONIC_COOLDOWN_PER_SEC = 50;
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
	private boolean manned;
	private Crew manning;
	protected boolean manable;
	
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
		manned = false;
		manning = null;
		manable = false;
	}
	
	public void update(){
		if(ionicCooldown.isOnCooldown()){
			ionicCooldown.advanceCooldown(IONIC_COOLDOWN_PER_SEC);
			if(!ionicCooldown.isOnCooldown()){
				ionicCharges = 0;
			}
		}
		if(repairCooldown.isOnCooldown()){
			//repairCooldown.advanceCooldown(getRepairSpeed());
			if(!ionicCooldown.isOnCooldown()){
				ionicCharges = 0;
			}
		}
		
		
	}

//	private float getRepairSpeed(){
//		if(ship.isCrewInRoom(room)){
//			
//		}
//	}
	
	public void draw(Batch batch){
		Sprite icon = Modules.getIcon(this.getClass().getCanonicalName()); 
		batch.draw(icon, room.getCenterLoc().x-icon.getWidth()/2, room.getCenterLoc().y-icon.getHeight()/2);
	}
	
	public void receiveIonicCharge(){
		receiveIonicCharge(1);
	}
	
	public void receiveIonicCharge(int amt){
		ionicCharges++;
		ionicCooldown.startCooldown();
	}
	
	public void recieveDamage(){
		recieveDamage(1);
	}
	
	public void recieveDamage(int amt){
		
	}

	public int getPowerLevel() {
		return powerLevel;
	}

	public void setPowerLevel(int powerLevel) {
		this.powerLevel = powerLevel;
	}

	public boolean isManned() {
		return manned;
	}

	public void setManned(boolean manned) {
		this.manned = manned;
	}

	public Crew getManning() {
		return manning;
	}

	public void setManning(Crew manning) {
		this.manning = manning;
		if(manning != null){
			manned = true;
		}else{
			manned = false;
		}
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
