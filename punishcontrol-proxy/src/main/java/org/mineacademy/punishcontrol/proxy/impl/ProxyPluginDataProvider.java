package org.mineacademy.punishcontrol.proxy.impl;

import java.io.File;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPluginDataProvider implements PluginDataProvider {

  public static ProxyPluginDataProvider create() {
    return new ProxyPluginDataProvider();
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
