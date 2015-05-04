/**
 * Interface to sync modules to a power bar
 * Created by: Zach Higginbotham
 * Implementations by: Zach Higginbotham
 */
package com.deeper.than.ui;

/**
 * @author zach
 *
 */
public interface UIModuleSyncable {
	public void checkforSectionsChange();
	public void updateModulePowerLevel();
}
