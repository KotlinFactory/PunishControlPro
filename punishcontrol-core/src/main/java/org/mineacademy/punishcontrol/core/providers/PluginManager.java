package org.mineacademy.punishcontrol.core.providers;

import java.util.List;
import lombok.NonNull;

/**
 * Provider to manage plugins
 */
public interface PluginManager {

  List<String> listEnabled();

  boolean isEnabled(@NonNull final String pluginName);

  void enable(@NonNull final String pluginName);

  void disable(@NonNull final String pluginName);
}
