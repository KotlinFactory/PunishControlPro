package org.mineacademy.punishcontrol.proxy.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.mineacademy.punishcontrol.core.providers.PluginManager;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPluginManager implements PluginManager {

  public static ProxyPluginManager create() {
    return new ProxyPluginManager();
  }

  @Override
  public List<String> listEnabled() {
    return ProxyServer.getInstance().getPluginManager().getPlugins()
        .stream()
        .map(plugin -> plugin.getDescription().getName())
        .collect(Collectors.toList());
  }

  @Override
  public boolean isEnabled(@NonNull String pluginName) {
    return ProxyServer.getInstance().getPluginManager().getPlugin(pluginName) != null;
  }

  @Override
  public void enable(@NonNull String pluginName) {
    final Plugin plugin = ProxyServer
        .getInstance()
        .getPluginManager()
        .getPlugin(pluginName);

    if (plugin == null) {
      return;
    }


    plugin.onEnable();
  }

  @Override
  public void disable(@NonNull String pluginName) {
    final Plugin plugin = ProxyServer
        .getInstance()
        .getPluginManager()
        .getPlugin(pluginName);

    if (plugin == null) {
      return;
    }


    plugin.onDisable();
  }
}
