package org.mineacademy.punishcontrol.core.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;

@Getter
@Accessors(fluent = true)
public final class MySQLStorageProvider
    extends SimpleDatabase
    implements StorageProvider {

  //

  private static final String INSERT_QUERY =
      "INSERT INTO {table} (Type, Target, Creator, Reason, IP, Duration, Creation, Removed) VALUES('{type}', '{target}', '{creator}', '{reason}', '{ip}', {duration}, {creation}, {removed})";
  private final ExceptionHandler exceptionHandler;
  private boolean connected;

  // We need the mysql config here

  /*
  "warns": {
      "UUID": {
        "MS": {
          "creator": "UUID",
          "target-name": "NAME",
          "reason": "REASON",
          "duration": 39393033093393033093039,

          "removed": "false"
        }
  */

  @Inject
  public MySQLStorageProvider(final ExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
    addVariable("table", "Punishes");
  }

  private void handleMySQLException(final SQLException ex, final String name) {
    exceptionHandler.saveError(
        ex,
        "Exception while updating mysql. Have you altered the database? (): ",
        name);
  }


  @Override
  public void setup() {
    connect();
  }

  @Override
  public boolean isSetup() {
    return isConnected();
  }

  // jdbc:mysql://localhost:3306/hybrisdb?characterEncoding=latin1
  public void connect() {
    connect(
        "jdbc:mysql://" + MySQL.HOST + ":" + MySQL.PORT + "/" + MySQL.DATABASE
            + "?characterEncoding=latin1",
        MySQL.USER,
        MySQL.PASSWORD);
  }

  @Override
  protected void onConnected() {
    update(
        "CREATE TABLE IF NOT EXISTS Punishes("
            + "Type varchar(64), "
            + "Target varchar(64), "
            + "Creator varchar(64), "
            + "Reason varchar(64), "
            + "IP varchar(64), "
            + "Duration bigint, "
            + "Creation bigint, "
            + "Removed boolean, PRIMARY KEY (Creation))");

    connected = true;
  }

  public boolean connected() {
    return connected;
  }

  private Ban banFromResultSet(final ResultSet resultSet) throws SQLException {
    return Ban.of(
        resultSet.getString("Target"),
        resultSet.getString("Creator"),
        PunishDuration.of(resultSet.getLong("Duration")))
        .ip(resultSet.getString("IP"))
        .creation(resultSet.getLong("Creation"))
        .removed(resultSet.getBoolean("Removed"))
        .reason(resultSet.getString("Reason"));
  }

  private Mute muteFromResultSet(final ResultSet resultSet) throws SQLException {
    return Mute.of(
        resultSet.getString("Target"),
        resultSet.getString("Creator"),
        PunishDuration.of(resultSet.getLong("Duration")))
        .ip(resultSet.getString("IP"))
        .creation(resultSet.getLong("Creation"))
        .removed(resultSet.getBoolean("Removed"))
        .reason(resultSet.getString("Reason"));
  }

  private Warn warnFromResultSet(final ResultSet resultSet) throws SQLException {
    return Warn.of(
        resultSet.getString("Target"),
        resultSet.getString("Creator"),
        PunishDuration.of(resultSet.getLong("Duration")))
        .ip(resultSet.getString("IP"))
        .creation(resultSet.getLong("Creation"))
        .removed(resultSet.getBoolean("Removed"))
        .reason(resultSet.getString("Reason"));
  }

  @Override
  public List<Ban> listBans() {
    final List<Ban> result = new ArrayList<>();
    try (final ResultSet resultSet = query(
        "SELECT * FROM Punishes WHERE Type='BAN'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(banFromResultSet(resultSet));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListBans");
    }
    return result;
  }

  @Override
  public List<Mute> listMutes() {
    final List<Mute> result = new ArrayList<>();
    try (final ResultSet resultSet = query(
        "SELECT * FROM Punishes WHERE Type='MUTE'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(muteFromResultSet(resultSet));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListMutes");
    }
    return result;
  }

  @Override
  public List<Warn> listWarns() {
    final List<Warn> result = new ArrayList<>();
    try (final ResultSet resultSet = query(
        "SELECT * FROM Punishes WHERE TYPE='WARN'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(warnFromResultSet(resultSet));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListWarns");
    }
    return result;
  }

  @Override
  public List<Ban> listBans(@NonNull final UUID uuid) {
    final List<Ban> result = new ArrayList<>();
    try (final ResultSet resultSet =
        query("SELECT * FROM Punishes WHERE Type='BAN' AND Target='" + uuid
            + "'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(banFromResultSet(resultSet));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListBans-UUID");
    }
    return result;
  }

  @Override
  public List<Mute> listMutes(@NonNull final UUID uuid) {
    final List<Mute> result = new ArrayList<>();
    try (final ResultSet resultSet =
        query("SELECT * FROM Punishes WHERE Type='MUTE' AND Target='" + uuid
            + "'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(muteFromResultSet(resultSet));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListMutes-UUID");
    }
    return result;
  }

  @Override
  public List<Warn> listWarns(@NonNull final UUID uuid) {
    final List<Warn> result = new ArrayList<>();
    try (final ResultSet resultSet =
        query("SELECT * FROM Punishes WHERE Type='WARN' AND Target='" + uuid
            + "'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }
      while (resultSet.next()) {
        result.add(warnFromResultSet(resultSet));
      }

    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListWarns-UUID");
    }
    return result;
  }

  private void savePunishData(@NonNull final Punish punish) {
    saveData(
        punish.punishType(),
        punish.target(),
        punish.creator(),
        punish.reason(),
        punish.ip().orElse("Unknown"),
        punish.punishDuration().toMs(),
        punish.creation(),
        punish.removed());
  }

  @Override
  public void saveBan(@NonNull final Ban ban) {
    savePunishData(ban);
  }

  @Override
  public void saveMute(@NonNull final Mute mute) {
    savePunishData(mute);
  }

  @Override
  public void saveWarn(@NonNull final Warn warn) {
    savePunishData(warn);
  }

  @Override
  public void removeBan(final @NonNull Ban ban) {
    update("UPDATE Punishes SET removed=true WHERE Creation=" + ban.creation()
        + " AND Type='BAN'");
  }

  @Override
  public void removeMute(final @NonNull Mute mute) {
    update(
        "UPDATE Punishes SET removed=true WHERE Creation=" + mute.creation()
            + " AND Type='MUTE'");
  }

  @Override
  public void removeWarn(final @NonNull Warn warn) {
    update(
        "UPDATE Punishes SET removed=true WHERE Creation=" + warn.creation()
            + " AND Type='WARN'");
  }

  private void saveData(
      @NonNull final PunishType punishType,
      @NonNull final UUID target,
      @NonNull final UUID creator,
      @NonNull final String reason,
      @NonNull final String ip,
      final long duration,
      final long creation,
      final boolean removed) {
    update(
        INSERT_QUERY
            .replace("{type}", punishType.toString())
            .replace("{target}", target.toString())
            .replace("{creator}", creator.toString())
            .replace("{reason}", reason)
            .replace("{ip}", ip)
            .replace("{duration}", duration + "")
            .replace("{creation}", creation + "")
            .replace("{removed}", removed + ""));
  }
}
