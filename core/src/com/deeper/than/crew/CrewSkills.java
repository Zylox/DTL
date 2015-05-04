/**
 * Various skills that can be leveled up thorughout use in their area
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.crew;

/**
 * Skills a crew member can have
 * @author zach
 *
 */
public class CrewSkills {
	public enum CrewSkillsTypes{
		BRIDGE,
		ENGINES,
		SHIELDS,
		WEAPONS,
		REPAIR,
		COMBAT;
	}
	private final static float BRIDGE_EXP_PER_LEVEL = 15;
	private final static float ENGINES_EXP_PER_LEVEL = 15;
	private final static float SHIELDS_EXP_PER_LEVEL = 55;
	private final static float WEAPONS_EXP_PER_LEVEL = 65;
	private final static float REPAIR_EXP_PER_LEVEL = 18;
	private final static float COMBAT_EXP_PER_LEVEL = 8;
	
	public static final int MAX_LEVELS = 3;

	private static final float BRIDGE_EVASION_BONUS[] = {.05f,.07f,.1f};
	private static final float ENGINES_EVASION_BONUS[] = {.05f,.07f,.1f};
	private static final float SHIELD_RECHARGE_RATES[] = {.1f,.2f,.3f};
	private static final float WEAPONS_CHARGE_RATES[] = {.1f,.15f,.2f};
	private static final float REPAIR_RATES[] = {0,.1f,.2f};
	private static final float COMBAT_BONUS[] = {0,.1f,.2f};
	

	
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
	
	public float getBonusRate(CrewSkillsTypes skill){
		switch(skill){
		case BRIDGE:
			return BRIDGE_EVASION_BONUS[getBridgeLevel()];
		case ENGINES:
			return ENGINES_EVASION_BONUS[getEnginesLevel()];
		case SHIELDS:
			return SHIELD_RECHARGE_RATES[getShieldsLevel()];
		case WEAPONS:
			return WEAPONS_CHARGE_RATES[getWeaponsLevel()];
		case REPAIR:
			return REPAIR_RATES[getRepairLevel()];
		case COMBAT:
			return COMBAT_BONUS[getCombatLevel()];
		}
		return 0;
	}
	
	public void gainExp(int exp, CrewSkillsTypes skill){
		switch(skill){
		case BRIDGE:
			setBridgeExp(getBridgeExp()+exp);
			break;
		case ENGINES:
			setEnginesExp(getEnginesExp()+exp);
			break;
		case SHIELDS:
			setShieldsExp(getSheildsExp()+exp);
			break;
		case WEAPONS:
			setWeaponsExp(getWeaponsExp()+exp);
			break;
		case REPAIR:
			setRepairExp(getRepairExp()+exp);
			break;
		case COMBAT:
			setCombatExp(getCombatExp()+exp);
			break;
		}
	}
	
	public float getBridgeExp() {
		return bridgeExp;
	}
	public void setBridgeExp(float bridgeExp) {
		if(bridgeExp > BRIDGE_EXP_PER_LEVEL * MAX_LEVELS - 1){
			bridgeExp = BRIDGE_EXP_PER_LEVEL * MAX_LEVELS - 1;
		}
		this.bridgeExp = bridgeExp;
	}
	public float getEnginesExp() {
		return enginesExp;
	}
	public void setEnginesExp(float enginesExp) {
		if(enginesExp > ENGINES_EXP_PER_LEVEL * MAX_LEVELS - 1){
			enginesExp = ENGINES_EXP_PER_LEVEL * MAX_LEVELS - 1;
		}
		this.enginesExp = enginesExp;
	}
	public float getSheildsExp() {
		return shieldsExp;
	}
	public void setShieldsExp(float shieldsExp) {
		if(shieldsExp > SHIELDS_EXP_PER_LEVEL * MAX_LEVELS - 1){
			shieldsExp = SHIELDS_EXP_PER_LEVEL * MAX_LEVELS - 1;
		}
		this.shieldsExp = shieldsExp;
	}
	public float getRepairExp() {
		return repairExp;
	}
	public void setRepairExp(float repairExp) {
		if(repairExp > REPAIR_EXP_PER_LEVEL * MAX_LEVELS - 1){
			repairExp = REPAIR_EXP_PER_LEVEL * MAX_LEVELS - 1;
		}
		this.repairExp = repairExp;
	}
	public float getCombatExp() {
		return combatExp;
	}
	public void setCombatExp(float combatExp) {
		if(combatExp > COMBAT_EXP_PER_LEVEL * MAX_LEVELS - 1){
			combatExp = COMBAT_EXP_PER_LEVEL * MAX_LEVELS - 1;
		}
		this.combatExp = combatExp;
	}
	public float getWeaponsExp() {
		return weaponsExp;
	}
	public void setWeaponsExp(float weaponsExp) {
		if(weaponsExp > WEAPONS_EXP_PER_LEVEL * MAX_LEVELS - 1){
			weaponsExp = WEAPONS_EXP_PER_LEVEL * MAX_LEVELS - 1;
		}
		this.weaponsExp = weaponsExp;
	}
	
	public int getBridgeLevel(){
		return (int)bridgeExp / ((int)BRIDGE_EXP_PER_LEVEL);
	}
	public int getEnginesLevel(){
		return (int)enginesExp / ((int)ENGINES_EXP_PER_LEVEL);
	}
	public int getShieldsLevel(){
		return (int)shieldsExp / ((int)SHIELDS_EXP_PER_LEVEL);
	}
	public int getRepairLevel(){
		return (int)repairExp / (int)(REPAIR_EXP_PER_LEVEL);
	}
	public int getCombatLevel(){
		return (int)combatExp / ((int)COMBAT_EXP_PER_LEVEL);
	}
	public int getWeaponsLevel(){
		return (int)weaponsExp / ((int)WEAPONS_EXP_PER_LEVEL);
	}
	
	public float getBridgeLvlPercent(){
		return (bridgeExp % BRIDGE_EXP_PER_LEVEL) / (BRIDGE_EXP_PER_LEVEL+1); 
	}
	public float getEnginesLvlPercent(){
		return (enginesExp % ENGINES_EXP_PER_LEVEL) / (ENGINES_EXP_PER_LEVEL+1); 
	}
	public float getShieldsLvlPercent(){
		return (shieldsExp % SHIELDS_EXP_PER_LEVEL) / (SHIELDS_EXP_PER_LEVEL+1);
	}
	public float getRepairLvlPercent(){
		return (repairExp % REPAIR_EXP_PER_LEVEL) / (REPAIR_EXP_PER_LEVEL-1); 
	}
	public float getCombatLvlPercent(){
		return (combatExp % COMBAT_EXP_PER_LEVEL) / (COMBAT_EXP_PER_LEVEL+1);
	}
	public float getWeaponsLvlPercent(){
		return (weaponsExp % WEAPONS_EXP_PER_LEVEL) / (WEAPONS_EXP_PER_LEVEL+1); 
	}
	
}
