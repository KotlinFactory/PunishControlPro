package org.mineacademy.punishcontrol.core.punish.importer;

import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.providers.PluginManager;

@Getter
@Accessors(fluent = true)
public abstract class AbstractPunishImporter implements PunishImporter {

  private final PluginManager pluginManager;
  private final String pluginName;

  /**
   * @param pluginManager Implementation of our {@link PluginManager}.
   * @param pluginName    Name of the plugin we want to import punishes from. If null: The
   *                      PunishImporter will always be treated as if the plugin it needs
   *                      is installed
   */
  protected AbstractPunishImporter(
      @NonNull final PluginManager pluginManager,
      @Nullable final String pluginName) {
    this.pluginManager = pluginManager;
    this.pluginName = pluginName;
    PunishImporters.register(this);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from PunishImporter (final)
  // ----------------------------------------------------------------------------------------------------

  @Override
  public final boolean pluginInstalled() {
    if (pluginName == null) {
      return true;
    }
    return pluginManager.isEnabled(pluginName);
  }

  @Override
  public final Optional<String> pluginName() {
    return Optional.ofNullable(pluginName);
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods that might be overridden
  // ----------------------------------------------------------------------------------------------------

  // Change me if needed
  @Override
  public boolean applicable() {
    return pluginInstalled();
  }
}
