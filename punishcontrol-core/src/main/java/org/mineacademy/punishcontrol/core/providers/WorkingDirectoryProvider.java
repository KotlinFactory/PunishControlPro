package org.mineacademy.punishcontrol.core.providers;

import java.io.File;

public interface WorkingDirectoryProvider {
  File getDataFolder();

  File getSource();
}
