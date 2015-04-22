package com.deeper.than.crew;

public class CrewSkills {
	private final static float BRIDGE_EXP_PER_LEVEL = 15;
	private final static float ENGINES_EXP_PER_LEVEL = 15;
	private final static float SHIELDS_EXP_PER_LEVEL = 55;
	private final static float WEAPONS_EXP_PER_LEVEL = 65;
	private final static float REPAIR_EXP_PER_LEVEL = 18;
	private final static float COMBAT_EXP_PER_LEVEL = 8;
	
	public static final int MAX_LEVELS = 3;
	
	private float bridgeExp;
	private float enginesExp;
	private float shieldsExp;
	private float repairExp;
	private float combatExp;
	private float weaponsExp;
	
	public CrewSkills(){
		this(0,0,0,0,0,0);
	}
	
	public CrewSkills(float bridgeExp, float enginesExp, float shieldsExp, float repairExp, float combatExp, float weaponsExp){
		this.bridgeExp = bridgeExp;
		this.enginesExp = enginesExp;
		this.shieldsExp = shieldsExp;
		this.repairExp = repairExp;
		this.combatExp = combatExp;
		this.weaponsExp = weaponsExp;
	}
	
	public float getBridgeExp() {
		return bridgeExp;
	}
	public void setBridgeExp(float bridgeExp) {
		if(bridgeExp > BRIDGE_EXP_PER_LEVEL * MAX_LEVELS){
			bridgeExp = BRIDGE_EXP_PER_LEVEL * MAX_LEVELS;
		}
		this.bridgeExp = bridgeExp;
	}
	public float getEnginesExp() {
		return enginesExp;
	}
	public void setEnginesExp(float enginesExp) {
		if(enginesExp > ENGINES_EXP_PER_LEVEL * MAX_LEVELS){
			enginesExp = ENGINES_EXP_PER_LEVEL * MAX_LEVELS;
		}
		this.enginesExp = enginesExp;
	}
	public float getSheildsExp() {
		return shieldsExp;
	}
	public void setShieldsExp(float shieldsExp) {
		if(shieldsExp > SHIELDS_EXP_PER_LEVEL * MAX_LEVELS){
			shieldsExp = SHIELDS_EXP_PER_LEVEL * MAX_LEVELS;
		}
		this.shieldsExp = shieldsExp;
	}
	public float getRepairExp() {
		return repairExp;
	}
	public void setRepairExp(float repairExp) {
		if(repairExp > REPAIR_EXP_PER_LEVEL * MAX_LEVELS){
			repairExp = REPAIR_EXP_PER_LEVEL * MAX_LEVELS;
		}
		this.repairExp = repairExp;
	}
	public float getCombatExp() {
		return combatExp;
	}
	public void setCombatExp(float combatExp) {
		if(combatExp > COMBAT_EXP_PER_LEVEL * MAX_LEVELS){
			combatExp = COMBAT_EXP_PER_LEVEL * MAX_LEVELS;
		}
		this.combatExp = combatExp;
	}
	public float getWeaponsExp() {
		return weaponsExp;
	}
	public void setWeaponsExp(float weaponsExp) {
		if(weaponsExp > WEAPONS_EXP_PER_LEVEL * MAX_LEVELS){
			weaponsExp = WEAPONS_EXP_PER_LEVEL * MAX_LEVELS;
		}
		this.weaponsExp = weaponsExp;
	}
	
	public int getBrigeLevel(){
		return (int)bridgeExp / (int)BRIDGE_EXP_PER_LEVEL + 1;
	}
	public int getEnginesLevel(){
		return (int)enginesExp / (int)ENGINES_EXP_PER_LEVEL + 1;
	}
	public int getShieldsLevel(){
		return (int)shieldsExp / (int)SHIELDS_EXP_PER_LEVEL + 1;
	}
	public int getRepairLevel(){
		return (int)repairExp / (int)REPAIR_EXP_PER_LEVEL + 1 ;
	}
	public int getCombatLevel(){
		return (int)combatExp / (int)COMBAT_EXP_PER_LEVEL + 1;
	}
	public int getWeaponsLevel(){
		return (int)weaponsExp / (int)WEAPONS_EXP_PER_LEVEL + 1;
	}
	
	public float getBridgeLvlPercent(){
		return (bridgeExp % BRIDGE_EXP_PER_LEVEL) / BRIDGE_EXP_PER_LEVEL; 
	}
	public float getEnginesLvlPercent(){
		return (enginesExp % ENGINES_EXP_PER_LEVEL) / ENGINES_EXP_PER_LEVEL; 
	}
	public float getShieldsLvlPercent(){
		return (shieldsExp % SHIELDS_EXP_PER_LEVEL) / SHIELDS_EXP_PER_LEVEL;
	}
	public float getRepairLvlPercent(){
		return (repairExp % REPAIR_EXP_PER_LEVEL) / REPAIR_EXP_PER_LEVEL; 
	}
	public float getCombatLvlPercent(){
		return (combatExp % COMBAT_EXP_PER_LEVEL) / COMBAT_EXP_PER_LEVEL;
	}
	public float getWeaponsLvlPercent(){
		return (weaponsExp % WEAPONS_EXP_PER_LEVEL) / WEAPONS_EXP_PER_LEVEL; 
	}
	
}
