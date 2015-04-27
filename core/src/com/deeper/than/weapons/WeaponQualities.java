package com.deeper.than.weapons;

import java.util.Random;

public enum WeaponQualities { //accuracy, damageModifier, critdamage, chance, rechargeSpeed, cost, power
	EXCEPTIONAL(new Range(.75f, 1f), new Range(1,2),new Range(2,2),new Range(.25f,.50f),new Range(.7f,1),new Range(3,4),new Range(1,3)),
	PRISTINE(new Range(.60f, .75f), new Range(1,1),new Range(2,2),new Range(.25f,.40f),new Range(.8f,1),new Range(2,3),new Range(1,2)),
	LIKENEW(new Range(.50f, .65f), new Range(1,1),new Range(1.5f,2),new Range(.20f,30f),new Range(1,1),new Range(1,1.5f),new Range(0,2)),
	WORN(new Range(.30f, .60f), new Range(.5f,1),new Range(1,1),new Range(0,0),new Range(1,1.5f),new Range(.7f,1),new Range(0,1)),
	DAMAGED(new Range(.1f, .3f), new Range(.5f,.5f),new Range(1,1),new Range(0,0),new Range(1.3f,2),new Range(.5f,.8f),new Range(0,0));
	
	private Range accuracy;
	private Range damageModifier;
	private Range critDamage;
	private Range critChance;
	private Range rechargeSpeed;
	private Range cost;
	private Range power;
	
	private static Random ran = new Random();
	
	private WeaponQualities(Range accuracy, Range damageModifier , Range critDamage, Range critChance,
			Range rechargeSpeed, Range cost, Range power){
		this.accuracy = accuracy;
		this.damageModifier = damageModifier;
		this.critDamage = critDamage;
		this.critChance = critChance;
		this.rechargeSpeed = rechargeSpeed;
		this.cost = cost;
		this.power = power;
	}
	
}
