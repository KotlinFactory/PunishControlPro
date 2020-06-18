package org.mineacademy.punishcontrol.core.providers;

import java.io.File;

public interface PluginDataProvider {

  File getDataFolder();

  File getSource();

  String getNamed();

  String getVersion();
}
