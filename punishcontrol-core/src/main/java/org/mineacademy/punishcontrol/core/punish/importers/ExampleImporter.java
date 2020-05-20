package org.mineacademy.punishcontrol.core.punish.importers;

import java.util.List;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.importer.AbstractImporter;

public final class ExampleImporter extends AbstractImporter {

  public static ExampleImporter create() {
    return new ExampleImporter();
  }

  private ExampleImporter() {
    super("example");
  }

  @Override
  public List<Punish> listPunishesToImport() {
    return null;
  }

  @Override
  public Notification notificationOnSuccess() {
    return null;
  }
}
