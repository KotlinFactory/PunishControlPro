package org.mineacademy.punishcontrol.core.punish.importer;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.providers.PluginManager;

@Getter
@Accessors(fluent = true)
public abstract class AbstractImporter implements PunishImporter {

  private final PluginManager pluginManager;
  private final String name;

  /**
   * @param pluginManager Implementation of our {@link PluginManager}
   * @param pluginName    Name of the plugin we want to import punishes from
   */
  protected AbstractImporter(
      @NonNull final PluginManager pluginManager,
      @NonNull final String pluginName) {
    this.pluginManager = pluginManager;
    this.name = pluginName;
    PunishImporters.register(this);
  }

  /**
   * Tells us whether our PunishImporter is applicable (the plugin needed is installed) or
   * not
   */
  @Override
  public final boolean isApplicable() {
    return pluginManager.isEnabled(name);
  }
}
