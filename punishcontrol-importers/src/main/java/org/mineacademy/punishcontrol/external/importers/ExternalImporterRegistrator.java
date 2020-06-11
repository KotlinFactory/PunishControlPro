package org.mineacademy.punishcontrol.external.importers;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.punish.importer.PunishImporters;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

@UtilityClass
public class ExternalImporterRegistrator {

  public void register(
      @NonNull final StorageProvider storageProvider,
      @NonNull final PluginManager pluginManager) {
    PunishImporters.register(new LitebansPunishImporter(storageProvider, pluginManager));
  }
}
