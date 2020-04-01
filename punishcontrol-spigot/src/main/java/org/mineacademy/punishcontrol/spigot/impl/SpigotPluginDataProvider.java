package org.mineacademy.punishcontrol.spigot.impl;

import java.io.File;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPluginDataProvider implements PluginDataProvider {

  public static SpigotPluginDataProvider create() {
    return new SpigotPluginDataProvider();
  }

  @Override
  public File getDataFolder() {
    return SimplePlugin.getData();
  }

  @Override
  public File getSource() {
    return SimplePlugin.getSource();
  }

  @Override
  public String getNamed() {
    return SimplePlugin.getNamed();
  }

  @Override
  public String getVersion() {
    return SimplePlugin.getVersion();
  }
}
