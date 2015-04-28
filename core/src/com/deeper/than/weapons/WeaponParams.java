/**
 * 
 */
package com.deeper.than.weapons;

/**
 * @author zach
 *
 */

public class WeaponParams{
	public float accuracy;
	public float baseDamage;
	public float critDamage;
	public float critChance;
	public float rechargeSpeed;
	public float baseMonetaryCost;
	public int powerCost;
	public WeaponQualities quality;
	public WeaponMakers maker;
	
	public String getParamString(){
		String paramString = Float.toString(accuracy) + "\n" +
							 Float.toString(baseDamage) + "\n" +
							 Float.toString(critDamage) + "\n" +
							 Float.toString(critChance) + "\n" +
							 Float.toString(rechargeSpeed) + "\n" +
							 Float.toString(baseMonetaryCost) + "\n" +
							 Float.toString(powerCost);
		return paramString;
	}
}

