package org.mineacademy.punishcontrol.spigot;

import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import java.util.Collection;
import javax.inject.Named;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.spigot.menus.settings.SettingTypes;

@Module
public class SpigotModule {
  @Provides
  public Server server() {
    return Bukkit.getServer();
  }

  @Provides
  public BukkitScheduler scheduler() {
    return server().getScheduler();
  }

  @Provides
  @Named("settings")
  public Collection<SettingTypes> settings() {
    return Arrays.asList(SettingTypes.values());
  }

  @Provides
  public SimplePlugin simplePlugin() {
    return SimplePlugin.getInstance();
  }
}
