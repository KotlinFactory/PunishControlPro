package org.mineacademy.punishcontrol.external.importers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import litebans.api.Database;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.importer.AbstractPunishImporter;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class LitebansPunishImporter extends AbstractPunishImporter {

  private final ExceptionHandler exceptionHandler;

  @Inject
  public LitebansPunishImporter(
      @NonNull final StorageProvider storageProvider,
      @NonNull final ExceptionHandler exceptionHandler,
      @NonNull final PluginManager pluginManager) {
    super(storageProvider, pluginManager, "LiteBans");
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  public List<Punish> listPunishesToImport() {
    final Database database = Database.get();
    final List<Punish> out = new ArrayList<>();

    try (final PreparedStatement preparedStatement = database.prepareStatement("SELECT "
        + "* "
        + "FROM {bans}")) {
      try (final ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {

          final long duration = resultSet.getLong("UNTIL") - resultSet.getLong("TIME");
          final Ban ban = Ban
              .of(
                  resultSet.getString("UUID").equalsIgnoreCase("CONSOLE")
                      ? FoConstants.CONSOLE.toString()
                      : resultSet.getString("UUID"),
                  resultSet.getString("BANNED_BY_UUID"),
                  duration)
              .ip(resultSet.getString("IP"))
              .reason(resultSet.getString("REASON"))
              .removed(!resultSet.getBoolean("ACTIVE"))
              .creation(resultSet.getLong("TIME"));

          out.add(ban);
        }
      }

    } catch (final Throwable throwable) {
      exceptionHandler.saveError(
          throwable,
          "Exception while fetching bans from LiteBans");
    }

    try (final PreparedStatement preparedStatement = database.prepareStatement("SELECT "
        + "* "
        + "FROM {mutes}")) {
      try (final ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
          final long duration = resultSet.getLong("UNTIL") - resultSet.getLong("TIME");
          printResultSet(resultSet);
          final Mute ban = Mute
              .of(
                  resultSet.getString("UUID").equalsIgnoreCase("CONSOLE")
                      ? FoConstants.CONSOLE.toString()
                      : resultSet.getString("UUID"),
                  resultSet.getString("BANNED_BY_UUID"),
                  duration)
              .ip(resultSet.getString("IP"))
              .reason(resultSet.getString("REASON"))
              .removed(!resultSet.getBoolean("ACTIVE"))
              .creation(resultSet.getLong("TIME"));

          out.add(ban);
        }
      }

    } catch (final Throwable throwable) {
      exceptionHandler.saveError(
          throwable,
          "Exception while fetching mutes from LiteBans");
    }

    try (final PreparedStatement preparedStatement = database.prepareStatement("SELECT "
        + "* "
        + "FROM {warnings}")) {
      try (final ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
          final long duration = resultSet.getLong("UNTIL") - resultSet.getLong("TIME");
          final Warn ban = Warn
              .of(
                  resultSet.getString("UUID").equalsIgnoreCase("CONSOLE")
                      ? FoConstants.CONSOLE.toString()
                      : resultSet.getString("UUID"),
                  resultSet.getString("BANNED_BY_UUID"),
                  duration)
              .ip(resultSet.getString("IP"))
              .reason(resultSet.getString("REASON"))
              .removed(!resultSet.getBoolean("ACTIVE"))
              .creation(resultSet.getLong("TIME"));

          out.add(ban);
        }
      }

    } catch (final Throwable throwable) {
      exceptionHandler.saveError(
          throwable,
          "Exception while fetching warns from LiteBans");
    }
    return out;
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

  private static void printResultSet(ResultSet rs) throws SQLException {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnsNumber = rsmd.getColumnCount();
//    while (rs.next()) {
    for (int i = 1; i <= columnsNumber; i++) {
      System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
    }
    System.out.println("");
//    }
    System.out.println(" ");
  }
}
