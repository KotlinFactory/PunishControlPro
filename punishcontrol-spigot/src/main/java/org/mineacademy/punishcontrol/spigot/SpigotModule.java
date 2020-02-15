package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.command.CommandUnBan;
import org.mineacademy.punishcontrol.spigot.command.CommandUnMute;
import org.mineacademy.punishcontrol.spigot.command.CommandUnWarn;
import org.mineacademy.punishcontrol.spigot.gui.MenuMySQL;
import org.mineacademy.punishcontrol.spigot.listener.SpigotDataSetter;
import org.mineacademy.punishcontrol.spigot.listener.SpigotJoinHandler;

/**
 * Interface implemented by dagger
 */

@Component(modules = Providers.class)
public interface SpigotModule {


	MenuMySQL mysqlModule();

	SpigotDataSetter spigotDataSetter();

	SpigotJoinHandler spigotJoinHandler();

	CommandUnWarn commandUnWarn();

	CommandUnBan commandUnBan();

	CommandUnMute commandUnMute();

}