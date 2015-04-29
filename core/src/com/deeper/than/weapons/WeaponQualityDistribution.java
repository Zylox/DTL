package com.deeper.than.weapons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class WeaponQualityDistribution {
	private ArrayList<Range> ranges;
	private int percents[] = {20,20,20,20,20};
	private int storedPercents[] = Arrays.copyOf(percents, percents.length);
	private boolean rangesChanged;
	private Random ran;
	
	public WeaponQualityDistribution(){
		calculateRanges();
		ran = new Random();
		rangesChanged = false;
	}
	
	public WeaponQualityDistribution(long seed){
		calculateRanges();
		ran = new Random(seed);
		rangesChanged = false;
	}
	
	
	public void storeDistribution(){
		for(int i = 0; i<percents.length;i++){
			storedPercents[i] = percents[i];
		}
	}
	
	public void restoreDistribution(){
		for(int i = 0; i<percents.length;i++){
			percents[i] = storedPercents[i];
		}
		rangesChanged = true;
	}
	
	public void clearDistribution(){
		for(int i = 0; i<percents.length;i++){
			percents[i] = 0;
		}
		rangesChanged = true;
	}
	public void setEvenDistribution(){
		for(int i = 0; i<percents.length;i++){
			percents[i] = 20;
		}
		rangesChanged = true;
	}
	
	private void calculateRanges(){
		if(ranges == null){
			ranges = new ArrayList<Range>();
			for(int i = 0; i<percents.length;i++){
				ranges.add(new Range());
			}
		}
		int acc = 0;
		for(int i = 0; i < ranges.size(); i++){
			ranges.get(i).min = acc;
			acc += percents[i];
			ranges.get(i).max = acc;
		}
		
		rangesChanged = false;
	}
	
	public WeaponQualities getRandomQualityByDist(){
		return this.getRandomQualityByDist(ran.nextInt(100));
	}
	
	public WeaponQualities getRandomQualityByDist(int percentRoll){
		if(rangesChanged){
			calculateRanges();
		}
		
		Range r;
		for(int i = 0; i<ranges.size(); i++){
			r = ranges.get(i);
			if(percentRoll >= r.min && percentRoll < r.max){
				return getQualityByIndex(i);
			}
		}
		return getQualityByIndex(0);
	}
	
	public int getPercent(WeaponQualities quality){
		return percents[getIndexByQuality(quality)];
	}
	
	public void setPercent(int percent, WeaponQualities quality){
		percents[getIndexByQuality(quality)] = percent;
		rangesChanged = true;
	}

	private WeaponQualities getQualityByIndex(int index){
		switch(index){
		case 4:
			return WeaponQualities.EXCEPTIONAL;
		case 3:
			return WeaponQualities.PRISTINE;
		case 2:
			return WeaponQualities.LIKENEW;
		case 1:
			return WeaponQualities.WORN;
		case 0:
			return WeaponQualities.DAMAGED;
		}
		return WeaponQualities.DAMAGED;
	}
	
	private int getIndexByQuality(WeaponQualities quality){
		switch(quality){
		case EXCEPTIONAL:
			return 4;
		case PRISTINE:
			return 3;
		case LIKENEW:
			return 2;
		case WORN:
			return 1;
		case DAMAGED:
			return 0;
		}
		return 0;
	}
	
	
	
}
