package org.mineacademy.punishcontrol.external.importers;

import java.util.List;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.importer.AbstractPunishImporter;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class AdvancedBansPunishmentImporter extends AbstractPunishImporter {

  /**
   * @param storageProvider
   * @param pluginManager   Implementation of our {@link PluginManager}.
   * @param pluginName      Name of the plugin we want to import punishes from. If null: The
   *                        PunishImporter will always be treated as if the plugin it needs
   */
  protected AdvancedBansPunishmentImporter(
      @NonNull StorageProvider storageProvider,
      @NonNull PluginManager pluginManager,
      @Nullable String pluginName) {
    super(storageProvider, pluginManager, pluginName);
  }

  @Override
  public List<Punish> listPunishesToImport() {
//    for (Punishment punishment : PunishmentManager.get().getLoadedHistory()) {
//      punishment.
//    }

    return null;
  }

  @Override
  public Notification notificationOnSuccess() {
    return null;
  }

  @Override
  public String[] description() {
    return new String[0];
  }

  @Override
  public String itemString() {
    return null;
  }
}
