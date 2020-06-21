package org.mineacademy.punishcontrol.spigot;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.inject.Named;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.spigot.menus.settings.SettingTypes;

@Module
public class SpigotModule {

  private List<Class<?>> classes;

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

  /**
   * List's the classes used in our plugin. Heavyweight operation! --> Use async only!
   */
  @Provides
  public List<Class<?>> classes() {
    if (classes != null) {
      return classes;
    }

    return classes = new ArrayList<>(ReflectionUtil.getClasses(simplePlugin()));
  }
}
