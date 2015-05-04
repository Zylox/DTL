/**
 * UI element for a pause button
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deeper.than.screens.GameplayScreen;

/**
 * Pause button ui element
 * @author zach
 *
 */
public class UIPauseButton extends Button{
	
	private static TextureRegionDrawable playImg;
	private static TextureRegionDrawable pauseImg;
	private static ButtonStyle bStyle = new ButtonStyle();
	
	public static void loadAssets(){
		playImg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play.png"))));
		pauseImg = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("pause.png"))));
		bStyle.checked = playImg;
		bStyle.up = pauseImg;
	}
	
	private GameplayScreen gameplayScreen;
	
	public UIPauseButton(GameplayScreen gameplayScreen){
		super(bStyle);
		this.gameplayScreen = gameplayScreen;
		//callback to get pause status from gameplay state
		this.addAction(new Action() {
			@Override
			public boolean act(float delta) {
				syncToGameplayScreen();
				return false;
			}
		});
		this.background(new NinePatchDrawable(UIEnemyWindow.backgroundNinePatch));
		//callback to toggle paused
		this.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				togglePaused();
			}
		});
	}

	private void togglePaused(){
		gameplayScreen.setPaused(!gameplayScreen.isPaused());
	}
	
	private void syncToGameplayScreen(){
		if(gameplayScreen.isPaused()){
			setChecked(true);
		}else{
			setChecked(false);
		}
	}
}
