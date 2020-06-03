package org.mineacademy.punishcontrol.core.punish.importer;

import java.util.List;
import java.util.Optional;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.punish.Punish;

public interface PunishImporter {


  /**
   * Plugin needed to import these punishments from.
   */
  Optional<String> pluginName();

  List<Punish> listPunishesToImport();

  /**
   * Notification that should pop up if the import process is done
   */
  Notification notificationOnSuccess();

  /**
   * Tells us whether our PunishImporter is applicable (the plugin needed is installed) or
   * not
   */
  boolean isApplicable();

  /**
   * If we want to import punishments from our mysql-database this needs to be set to
   * true
   */
  default boolean needsMySql() {
    return false;
  }

  /**
   * Description that will be shown in menus / chat if players browse
   * the available punishment importers
   */
  String[] description();

  /**
   * 1.13+ String representation of the ItemStack the Menu representation of our
   * punishment importer should have
   */
  String itemString();
}
