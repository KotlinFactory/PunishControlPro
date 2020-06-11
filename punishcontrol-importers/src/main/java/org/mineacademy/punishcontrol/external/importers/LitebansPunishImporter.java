package org.mineacademy.punishcontrol.external.importers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import litebans.api.Database;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.importer.AbstractPunishImporter;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class LitebansPunishImporter extends AbstractPunishImporter {

  @Inject
  public LitebansPunishImporter(
      @NonNull final StorageProvider storageProvider,
      @NonNull PluginManager pluginManager) {
    super(storageProvider, pluginManager, "LiteBans");
  }

  @Override
  public List<Punish> listPunishesToImport() {
    final Database database = Database.get();

    return new ArrayList<>();
  }

  @Override
  public Notification notificationOnSuccess() {
    return null;
  }

  @Override
  public String[] description() {
    return new String[]{
        "Import punishments from LiteBans"
    };
  }

  @Override
  public String itemString() {
    return "SEA_LANTERN";
  }

  @Override
  public boolean applicable() {
    try {
      Class.forName("litebans.api.Database");
      return true;
    } catch (final Throwable throwable) {
      return false;
    }
  }
}
