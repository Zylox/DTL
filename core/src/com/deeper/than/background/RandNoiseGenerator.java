/**
 * 
 */
package com.deeper.than.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author zach
 *
 */
public class RandNoiseGenerator {

	private float[][] map;
	
	private float highestValue;
	private float lowestValue;
	private Pixmap pix;
	private Image img;
	
	private float simplexDepth=0;
	private long seed;
	
	public RandNoiseGenerator(int width, int height, long seed){
		this.seed = seed;
		map = new float[height/4][width/4];
	}
	
	public Image getNextImage(float delta){
		simplexDepth+=delta;
		map = getOpenSimplexHeighMap(map, seed, simplexDepth);
		generateNewPicture();
		return img;
	}
	
	public void generateNewPicture(){
		pix = genGreyscaleMap(map);
		img = new Image(new Texture(pix));
		pix.dispose();
	}
	
//	private void transformMap(){
// 		map = blur(1);
//		//map = mapgen.scale(0, 1);
//		generateNewPicture();
//	}
	
	private static final Format FORMAT = Pixmap.Format.RGBA8888;
	private Pixmap genGreyscaleMap(float[][] map){
		Pixmap pix = new Pixmap(map[0].length, map.length, FORMAT);
		float mapValue = 0;
		float hold;
		for(int j = 0; j < map.length; j++){
			for(int i =0; i < map[0].length; i++){
				mapValue = map[j][i];				
				//water
				hold = mapValue*mapValue/1f;
				pix.setColor(new Color(hold,hold,hold, 1));
				

				pix.drawPixel(i, j);
			}
		}
		return pix;
	}
	
	
	public float[][] getOpenSimplexHeighMap(float[][] map, long seed){
		OpenSimplexNoise simplex = new OpenSimplexNoise(seed);
		this.map = map;
		highestValue = -100000;
		lowestValue = 100000;
		for(int j = 0; j<map.length; j++){
			for(int i = 0; i< map[0].length; i++){
				float value = (float)simplex.eval(((double)(i))/15, ((double)(j))/15);
				highestValue = Math.max(highestValue, value);
				lowestValue = Math.min(lowestValue, value);
				map[j][i] = (float)Math.tan(value);
			}
		}
		return map;
	}
	
	public float[][] getOpenSimplexHeighMap(float[][] map, long seed, double z){
		OpenSimplexNoise simplex = new OpenSimplexNoise(seed);
		this.map = map;
		highestValue = -100000;
		lowestValue = 100000;
		for(int j = 0; j<map.length; j++){
			for(int i = 0; i< map[0].length; i++){
				float value = (float)simplex.eval(((double)(i))/100, ((double)(j))/100, z);
				highestValue = Math.max(highestValue, value);
				lowestValue = Math.min(lowestValue, value);
				map[j][i] = value*value;
			}
		}
		map = scale(0,1);
		return map;
	}
	
	public float[][] scale(float goalLow, float goalHigh){
	float[][] newArray = copyMap(map);
	float avgValue = 0;
	float lowAdjust = goalLow-lowestValue;
	float scaleValue = goalHigh/(highestValue+lowAdjust);
	
	highestValue = -10000000;
	lowestValue = 10000000;
	
	for(int j = 0; j < map.length; j++){
		for(int i = 0; i < map[0].length;i++){
			avgValue = map[j][i];
			avgValue += lowAdjust;
			avgValue *= scaleValue;
			newArray[j][i] = avgValue;
			highestValue = Math.max(highestValue, avgValue);
			lowestValue = Math.min(lowestValue, avgValue);
		}
	}
	map = newArray;
	return newArray;		
}
	
	public float[][] copyMap(float[][] oldMap){
	float newArray[][] = new float[oldMap.length][oldMap[0].length];
	for(int j = oldMap.length-1; j>=0; j--){
		for(int i = oldMap[0].length-1; i>=0;i--){
			newArray[j][i] = oldMap[j][i];
		}
	}
	return newArray;
}
	
}
