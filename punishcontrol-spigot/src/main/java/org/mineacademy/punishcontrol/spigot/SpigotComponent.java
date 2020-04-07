package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.commands.CommandMute;
import org.mineacademy.punishcontrol.spigot.commands.CommandUnBan;
import org.mineacademy.punishcontrol.spigot.commands.CommandUnMute;
import org.mineacademy.punishcontrol.spigot.commands.CommandUnWarn;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotDataSetter;
import org.mineacademy.punishcontrol.spigot.menus.MenuMySQL;
import org.mineacademy.punishcontrol.spigot.commands.CommandBan;
import org.mineacademy.punishcontrol.spigot.commands.CommandKick;
import org.mineacademy.punishcontrol.spigot.commands.CommandWarn;

/** Interface implemented by dagger */
@Component(modules = {Providers.class, SpigotModule.class})
public interface SpigotComponent {

  MenuMySQL mysqlModule();

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------

  SpigotDataSetter spigotDataSetter();

  // ----------------------------------------------------------------------------------------------------
  // Commands
  // ----------------------------------------------------------------------------------------------------

  CommandKick commandKick();

  CommandBan commandBan();

  CommandMute commandMute();

  CommandWarn commandWarn();

  CommandUnBan commandUnBan();

  CommandUnMute commandUnMute();

  CommandUnWarn commandUnWarn();
}
