/**
 * 
 */
package com.deeper.than.weapons;

import java.util.Random;

/**
 * @author zach
 *
 */
public enum WeaponMakers {
	BUY_N_LARGE("BNL"),
	THREE_TEK("ThreeTek"),
	LUK_CO("LukCo"),
	XYL("Xyl"),
	BOOM_N_ZOOM("Boom N Zoom");
	
	private String prefix;
	
	private WeaponMakers(String prefix){
		this.prefix = prefix;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	private static Random ran = new Random();
	
	public void modifyWeaponParams(WeaponParams params){
		if(this == BUY_N_LARGE){
			params.baseMonetaryCost *= .8;
		}else if(this == THREE_TEK){
			params.accuracy *= .9;
			params.rechargeSpeed *= 1.2;
		}else if(this == LUK_CO){
			params.critChance *= 1.2;
		}else if(this == XYL){
			params.baseDamage *= 1.2;
		}else if(this == BOOM_N_ZOOM){
			params.critChance = 0;
			params.accuracy = 1;
		}
	}
	
	public static WeaponMakers getRandomMaker(){
		return WeaponMakers.values()[ran.nextInt(WeaponMakers.values().length)];
	}
}
