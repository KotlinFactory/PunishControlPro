package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.gui.MenuMySQL;

/**
 * Interface implemented by dagger
 */
@Component(modules = Providers.class)
public interface SpigotModule {
	MenuMySQL buildMenu();


}