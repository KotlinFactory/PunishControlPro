package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.providers.WorkingDirectoryProvider;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyWorkingDirectoryProvider implements WorkingDirectoryProvider {

  public static ProxyWorkingDirectoryProvider newInstance() {
    return new ProxyWorkingDirectoryProvider();
  }

  @Override
  public File getDataFolder() {
    return SimplePlugin.getData();
  }

  @Override
  public File getSource() {
    return SimplePlugin.getSource();
  }
}
