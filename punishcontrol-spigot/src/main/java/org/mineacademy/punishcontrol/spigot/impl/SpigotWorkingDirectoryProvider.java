package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.provider.providers.WorkingDirectoryProvider;

import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotWorkingDirectoryProvider implements WorkingDirectoryProvider {

  public static SpigotWorkingDirectoryProvider newInstance() {
    return new SpigotWorkingDirectoryProvider();
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
