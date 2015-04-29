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
	private float[][] copyMap;
	
	private float highestValue;
	private float lowestValue;
	private Pixmap pix;
	private Image img;
	private Texture tex;
	
	private float simplexDepth=0;
	private Color color;
	private OpenSimplexNoise simplex;
	
	public RandNoiseGenerator(int width, int height, long seed){
		map = new float[height/4][width/4];
		copyMap = new float[height/4][width/4];;
		color = new Color();
		pix = new Pixmap(map[0].length, map.length, FORMAT);
		tex = new Texture(pix);
		img = new Image(tex);
		simplex = new OpenSimplexNoise(seed);
	}
	
	public Image getNextImage(float delta){
		simplexDepth+=delta;
		getOpenSimplexHeighMap(simplexDepth);
		generateNewPicture();
		return img;
	}
	
	public void generateNewPicture(){
		genGreyscaleMap(map);
		tex.draw(pix, 0,0);
		
	}
	
//	private void transformMap(){
// 		map = blur(1);
//		//map = mapgen.scale(0, 1);
//		generateNewPicture();
//	}
	
	private static final Format FORMAT = Pixmap.Format.RGBA8888;
	private Pixmap genGreyscaleMap(float[][] map){
		
		float mapValue = 0;
		float hold;
		for(int j = 0; j < map.length; j++){
			for(int i =0; i < map[0].length; i++){
				mapValue = map[j][i];				
				//water
				hold = mapValue*mapValue/1f;
				color.a = 1;
				color.r = hold;
				color.g = hold;
				color.b = hold;
				pix.setColor(color);
				

				pix.drawPixel(i, j);
			}
		}
		return pix;
	}
	
	
	public void getOpenSimplexHeighMap(){
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
	}
	
	public void getOpenSimplexHeighMap(double z){
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
		scale(.1f,.9f);
	}
	
	public void scale(float goalLow, float goalHigh){
	copyMap();
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
			copyMap[j][i] = avgValue;
			highestValue = Math.max(highestValue, avgValue);
			lowestValue = Math.min(lowestValue, avgValue);
		}
	}
	
	map = copyMap;
}
	
	public void copyMap(){
		for(int j = map.length-1; j>=0; j--){
			for(int i = map[0].length-1; i>=0;i--){
				copyMap[j][i] = map[j][i];
			}
		}
	}
}
