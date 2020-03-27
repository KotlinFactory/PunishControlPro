package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.commands.CommandUnBan;
import org.mineacademy.punishcontrol.spigot.commands.CommandUnMute;
import org.mineacademy.punishcontrol.spigot.commands.CommandUnWarn;
import org.mineacademy.punishcontrol.spigot.impl.handlers.SpigotJoinHandler;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotDataSetter;
import org.mineacademy.punishcontrol.spigot.menus.MenuMySQL;

/** Interface implemented by dagger */
@Component(modules = {Providers.class, SpigotModule.class})
public interface SpigotComponent {

  MenuMySQL mysqlModule();

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------

  SpigotDataSetter spigotDataSetter();

  SpigotJoinHandler spigotJoinHandler();

  // ----------------------------------------------------------------------------------------------------
  // Commands
  // ----------------------------------------------------------------------------------------------------

  CommandUnWarn commandUnWarn();

  CommandUnBan commandUnBan();

  CommandUnMute commandUnMute();
}
