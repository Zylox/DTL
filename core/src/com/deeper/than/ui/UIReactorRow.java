/**
 * row that holds all reactor bars
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.deeper.than.DTL;
import com.deeper.than.EnemyShip;
import com.deeper.than.PlayerShip;
import com.deeper.than.Ship;
import com.deeper.than.modules.MainModule;
import com.deeper.than.modules.Module;
import com.deeper.than.modules.Modules;
import com.deeper.than.modules.SheildModule;
import com.deeper.than.modules.SubModule;
import com.deeper.than.modules.WeaponsModule;

/**
 * Ui element for holding all reactor bars for a ship
 * @author zach
 *
 */
public class UIReactorRow extends WidgetGroup{
	private Ship ship;
	//whether or not to split submodules into another table
	private boolean splitSub;
	
	private Table mainReactorBars;
	private Table subReactorBars;
	
	private UIWeaponModuleReacBar wModreac;
	
	private ArrayList<UIModuleSyncable> moduleReactorBars;
	
	public UIReactorRow(Ship ship, boolean splitSub){
		this.ship = ship;
		this.splitSub = splitSub;
		mainReactorBars = new Table();
		subReactorBars = new Table();
		moduleReactorBars = new ArrayList<UIModuleSyncable>();
	}
	
	public void update(){
    	for(UIModuleSyncable mrb : moduleReactorBars){
    		mrb.checkforSectionsChange();
    		mrb.updateModulePowerLevel();
    	}
	}
	
	public void setupReactorBars(){
		fillTables();
	}
	
	private void fillTables(){
		UIPowerBar subBar = new UIPowerBar(1, UIPowerBar.UNLIMITED_POWER);
		//create a reactorbar for the ship
		ReactorBar reactorBar = new ReactorBar(ship);
		if(ship instanceof PlayerShip){
			//for a player ship, display it
			mainReactorBars.add(reactorBar).bottom().left().minWidth(ReactorBar.PREF_WIDTH).fillY();
		}
		Module mod = null;
		UIModuleReactorBar moduleReac = null;
		//cycle through modules adding them
		for(Class<? extends Module> c : Modules.getModuleClasses()){
			mod = ship.getModule(c);
			if(mod != null){
				if(mod instanceof MainModule){
					if(mod instanceof WeaponsModule){
						if(ship instanceof EnemyShip){
							//give enemy weapons full pwoer if possible
							moduleReac = new UIWeaponModuleReacBar(mod.getLevel(), reactorBar, (WeaponsModule)mod);
						}else{
							//give player weapons no power by default
							moduleReac = new UIWeaponModuleReacBar(0, reactorBar, (WeaponsModule)mod);
						}
						wModreac = (UIWeaponModuleReacBar) moduleReac;
					}else{
						//sheild gets 2 bars by default because that is the least useful amount
						//everything else 1
						moduleReac = new UIModuleReactorBar(mod instanceof SheildModule ? 2 : 1, Modules.getIcon(c.getCanonicalName()), reactorBar, mod);
					}
					mainReactorBars.add(moduleReac).spaceLeft(10).bottom().left().minWidth(ReactorBar.PREF_WIDTH).fillY().expandY();
					moduleReactorBars.add(moduleReac);
				}else if(mod instanceof SubModule){
					moduleReac = new UIModuleReactorBar(mod.getLevel(), Modules.getIcon(c.getCanonicalName()), subBar, mod);
					//put into different table if set to split
					if(splitSub){
						subReactorBars.add(moduleReac).spaceLeft(10).bottom().left().minWidth(ReactorBar.PREF_WIDTH).fillY().expandY();
					}else{
						mainReactorBars.add(moduleReac).spaceLeft(10).bottom().left().minWidth(ReactorBar.PREF_WIDTH).fillY().expandY();
					}
					moduleReactorBars.add(moduleReac);
				}
				if(moduleReac != null){
					moduleReac.setPowerVisible(ship instanceof PlayerShip);
				}
			}
		}
		
		Table joiner = new Table();
		joiner.setFillParent(true);
		joiner.add(mainReactorBars).fill().expand().left();
		//if set to split, seperate them to oppisite sides of the screen
		if(splitSub){
			joiner.add().prefWidth(DTL.VWIDTH);
			joiner.add(subReactorBars).fill().expand().left();
		}
		joiner.pad(5);
		this.addActor(joiner);
	}
	
	public UIWeaponModuleReacBar getWeaponUI(){
		return wModreac;
	}
}
