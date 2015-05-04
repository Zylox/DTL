/**
 * Range object. holds a max and a min float
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.weapons;

/**
 * @author zach
 *
 */
public class Range {
	public float min;
	public float max;
	
	public Range(){
		
	}
	
	public Range(float min, float max){
		this.max = max;
		this.min = min;
	}
	
	public float getRange(){
		return max-min;
	}
}
