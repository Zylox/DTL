/**
 * Data for weapon qualities
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.weapons;

import java.util.Random;

/**
 * 
 * @author zach
 *
 */
public enum WeaponQualities { //accuracy, damageModifier, critdamage, chance, rechargeSpeed, cost, power
	EXCEPTIONAL("Exceptional",new Range(.95f, 1f), new Range(1,2),new Range(2,2),new Range(.25f,.50f),new Range(.7f,1),new Range(3,4),new Range(1,3)),
	PRISTINE("Pristine",new Range(.90f, 1f), new Range(1,1),new Range(2,2),new Range(.25f,.40f),new Range(.8f,1),new Range(2,3),new Range(1,2)),
	LIKENEW("Like New",new Range(.80f, .95f), new Range(0,1),new Range(1.5f,2),new Range(.20f,30f),new Range(1,1),new Range(1,1.5f),new Range(1,2)),
	WORN("Worn",new Range(.60f, .90f), new Range(0,0),new Range(1,1),new Range(0,0),new Range(1,1.5f),new Range(.7f,1),new Range(0,1)),
	DAMAGED("Damaged",new Range(.5f, .7f), new Range(0,0),new Range(1,1),new Range(0,0),new Range(1.3f,2),new Range(.5f,.8f),new Range(0,0));
	
	private String prefix;
	private Range accuracy;
	private Range damageModifier;
	private Range critDamage;
	private Range critChance;
	private Range rechargeSpeed;
	private Range cost;
	private Range power;
	
	private static Random ran = new Random();
	
	private WeaponQualities(String prefix, Range accuracy, Range damageModifier , Range critDamage, Range critChance,
			Range rechargeSpeed, Range cost, Range power){
		this.prefix = prefix;
		this.accuracy = accuracy;
		this.damageModifier = damageModifier;
		this.critDamage = critDamage;
		this.critChance = critChance;
		this.rechargeSpeed = rechargeSpeed;
		this.cost = cost;
		this.power = power;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public WeaponParams getRandomParamMods(){
		WeaponParams params = new WeaponParams();
		params.accuracy = getValueInRange(accuracy);
		params.baseDamage = getIntValueInRange((int)damageModifier.min,(int)damageModifier.max);
		params.critDamage = getValueInRange(critDamage);
		params.critChance = getValueInRange(critChance);
		params.rechargeSpeed = getValueInRange(rechargeSpeed);
		params.baseMonetaryCost = getValueInRange(cost);
		params.powerCost = getIntValueInRange((int)power.min,(int)power.max+1);
		return params;
	}
	
	/**
	 * Gets value in range, exclusively on the max
	 * @param min
	 * @param max
	 * @return
	 */
	private float getValueInRange(Range range){
		return getValueInRange(range.min, range.max);
	}
	
	/**
	 * Gets value in range, exclusively on the max
	 * @param min
	 * @param max
	 * @return
	 */
	private int getIntValueInRange(int min, int max){
		int range = max - min;
		int value = min;
		if(range>0){
			value += ran.nextInt(range);
		}
		return value;
	}
	
	/**
	 * Gets value in range, exclusively on the max
	 * @param min
	 * @param max
	 * @return
	 */
	private float getValueInRange(float min, float max){
		float range = max-min;
		float value = ran.nextFloat();
		value *= range;
		value += min;
		return value;
	}
}
