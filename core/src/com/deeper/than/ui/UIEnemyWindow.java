/**
 * UIelement that holds the enemy ship and manages its state
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */

package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.DTL;
import com.deeper.than.EnemyShip;
import com.deeper.than.FloorTile;

/**
 * Window for holding an enemy ship
 * @author zach
 *
 */
public class UIEnemyWindow extends WidgetGroup{
	public static NinePatch backgroundNinePatch;
	
	private static final int TOP_BAR_HEIGHT = 10;
	private static final float BACKGROUND_PADDING = 3;
	
	private EnemyShip ship;
	private Table table;
	private UIReactorRow shipReactors;
	private UIWeaponBottomBar enemyWeaps;
	
	public UIEnemyWindow(EnemyShip ship){
		this.ship = ship;
		this.table = new Table();
		this.addActor(ship);
		
	}
	
	public void update(){
		shipReactors.update();
		ship.update();
	}
	
	public void setUpTable(){
		table.clear();
		
		//set up teh table
		float width = this.getWidth();
		table.setFillParent(true);
		UITopBar topBar = new UITopBar(ship, width, TOP_BAR_HEIGHT, true);
		topBar.add().prefWidth(width);
		table.add(topBar).top().fill();
		table.row();
		table.add().expand();
		table.row();
		shipReactors = new UIReactorRow(ship, false);
		shipReactors.setupReactorBars();
		table.add(shipReactors).expand().fillY().left().bottom();
		
		this.addActor(table);
		
		int x = FloorTile.TILESIZE*ship.getLayout()[0].length;
		int y = FloorTile.TILESIZE*ship.getLayout().length;
		
		//center ship in window
		ship.setBounds(this.getWidth()/2 - x/2, this.getHeight()/2 - y/2, x, y);
		this.addActor(ship);
		
		enemyWeaps = new UIWeaponBottomBar(shipReactors.getWeaponUI());
		//draw off screen and set to non touchable
		enemyWeaps.setBounds(DTL.VWIDTH, 0, 0, 0);
		enemyWeaps.setTouchable(Touchable.disabled);
		this.addActor(enemyWeaps);
		ship.setWeaponsBar(enemyWeaps);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		//draw background then everything else
		backgroundNinePatch.draw(batch, this.getX()-BACKGROUND_PADDING, this.getY()-BACKGROUND_PADDING, this.getWidth()+BACKGROUND_PADDING*2, this.getHeight()+BACKGROUND_PADDING*2);
		super.draw(batch, parentAlpha);
	}
	
	
	public static void loadAssets(){
		backgroundNinePatch = new NinePatch(new Texture("enemyBackground.png"),1,3,1,3);
	}
	
	public EnemyShip getShip(){
		return ship;
	}
}
