package org.mineacademy.punishcontrol.spigot.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.mineacademy.punishcontrol.core.providers.PluginManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPluginManager implements PluginManager {

  public static SpigotPluginManager create() {
    return new SpigotPluginManager();
  }

  @Override
  public List<String> listEnabled() {
    return Arrays.stream(Bukkit.getPluginManager().getPlugins())
        .map(Plugin::getName)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isEnabled(@NonNull String pluginName) {
    final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
    return plugin != null && plugin.isEnabled();
  }

  @Override
  public void enable(@NonNull String pluginName) {
    final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
    if (plugin == null) {
      return;
    }
    Bukkit.getPluginManager().enablePlugin(plugin);
  }

  @Override
  public void disable(@NonNull String pluginName) {
    final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
    if (plugin == null) {
      return;
    }
    Bukkit.getPluginManager().disablePlugin(plugin);
  }
}
