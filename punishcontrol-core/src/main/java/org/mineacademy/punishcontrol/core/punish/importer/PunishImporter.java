package org.mineacademy.punishcontrol.core.punish.importer;

import java.util.List;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.punish.Punish;

public interface PunishImporter {
  
  List<Punish> listPunishesToImport();

  Notification notificationOnSuccess();
}
