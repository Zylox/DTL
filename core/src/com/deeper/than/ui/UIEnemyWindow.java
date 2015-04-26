package com.deeper.than.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.EnemyShip;
import com.deeper.than.FloorTile;

public class UIEnemyWindow extends WidgetGroup{
	private static NinePatch backgroundNinePatch;
	
	private static final int TOP_BAR_HEIGHT = 10;
	private static final float BACKGROUND_PADDING = 3;
	
	private EnemyShip ship;
	private Table table;
	private UIReactorRow shipReactors;
	
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
		
		float width = this.getWidth();
		float height = this.getHeight();
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
		
		ship.setBounds(this.getWidth()/2 - x/2, this.getHeight()/2 - y/2, x, y);
		this.addActor(ship);
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		backgroundNinePatch.draw(batch, this.getX()-BACKGROUND_PADDING, this.getY()-BACKGROUND_PADDING, this.getWidth()+BACKGROUND_PADDING*2, this.getHeight()+BACKGROUND_PADDING*2);
		super.draw(batch, parentAlpha);
	}
	
	
	public static void loadAssets(){
		backgroundNinePatch = new NinePatch(new Texture("enemyBackground.png"),10,2,22,2);
	}
	
}
