package org.mineacademy.punishcontrol.spigot;

import dagger.Module;
import dagger.Provides;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

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
}
