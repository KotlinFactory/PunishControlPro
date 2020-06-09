package org.mineacademy.punishcontrol.external.importers;

import java.util.List;
import javax.inject.Inject;
import litebans.api.Database;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.importer.AbstractPunishImporter;

public final class LitebansPunishImporter extends AbstractPunishImporter {


  @Inject
  public LitebansPunishImporter(@NonNull PluginManager pluginManager) {
    super(pluginManager, "LiteBans");
  }

  @Override
  public List<Punish> listPunishesToImport() {
    final Database database = Database.get();

    

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
