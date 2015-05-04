/**
 * Generates random noise for the background. uses OpenSimplexNoise designed and implemented by Kurt Spencer
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
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
	private Texture tex;
	
	private float simplexDepth=0;
	private Color color;
	private OpenSimplexNoise simplex;
	
	public RandNoiseGenerator(int width, int height, long seed){
		map = new float[height/4][width/4];
		color = new Color();
		pix = new Pixmap(map[0].length, map.length, FORMAT);
		tex = new Texture(pix);
		img = new Image(tex);
		simplex = new OpenSimplexNoise(seed);
	}
	
	/**
	 * Generates the next transform, jumping by delta
	 * @param delta
	 * @return
	 */
	public Image getNextImage(float delta){
		simplexDepth+=delta;
		getOpenSimplexHeighMap(simplexDepth);
		generateNewPicture();
		return img;
	}
	
	/**
	 * Creates picture based on noise data
	 */
	public void generateNewPicture(){
		genGreyscaleMap(map);
		tex.draw(pix, 0,0);
	}
	

	
	private static final Format FORMAT = Pixmap.Format.RGBA8888;
	/**
	 * colors and sets pixmap
	 * @param map
	 * @return
	 */
	private Pixmap genGreyscaleMap(float[][] map){
		float mapValue = 0;
		float hold;
		for(int j = 0; j < map.length; j++){
			for(int i =0; i < map[0].length; i++){
				mapValue = map[j][i];				
				//water
				hold = mapValue*mapValue;
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
	
	/**
	 * Generates 2d noise slice form 3d opensimplex cube
	 * @param z
	 */
	public void getOpenSimplexHeighMap(double z){
		highestValue = -100000;
		lowestValue = 100000;
		for(int j = 0; j<map.length; j++){
			for(int i = 0; i< map[0].length; i++){
				float value = (float)simplex.eval(((double)(i))/100, ((double)(j))/100, z);
				highestValue = Math.max(highestValue, value);
				lowestValue = Math.min(lowestValue, value);
				map[j][i] = value;
			}
		}
		scale(.1f,.9f);
	}
	
	/**
	 * scales all values to the wanted range
	 * @param goalLow
	 * @param goalHigh
	 */
	public void scale(float goalLow, float goalHigh){
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
				map[j][i] = avgValue;
				highestValue = Math.max(highestValue, avgValue);
				lowestValue = Math.min(lowestValue, avgValue);
			}
		}
	
	}
	
}
