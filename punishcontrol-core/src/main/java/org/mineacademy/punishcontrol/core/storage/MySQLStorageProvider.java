package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.provider.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MySQLStorageProvider extends SimpleDatabase implements StorageProvider {

  //

  private static final String INSERT_QUERY =
      "INSERT INTO {table} (Type, Target, Creator, Reason, IP, Duration, Creation, Removed) VALUES({type}, {target}, {creator}, {reason}, {ip}, {duration}, {creation}, {removed})";
  private final ExceptionHandler exceptionHandler;

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
        ex, "Exception while updating mysql. Have you altered the database? (): ", name);
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
  }

  @Override
  public List<Ban> listBans() {
    final List<Ban> result = new ArrayList<>();
    try (final ResultSet resultSet = query("SELECT * FROM {table} WHERE Type='BAN'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(
            Ban.of(
                resultSet.getString("Target"),
                resultSet.getString("Creator"),
                resultSet.getLong("Duration")));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListBans");
    }
    return result;
  }

  @Override
  public List<Mute> listMutes() {
    final List<Mute> result = new ArrayList<>();
    try (final ResultSet resultSet = query("SELECT * FROM {table} WHERE Type='MUTE'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(
            Mute.of(
                resultSet.getString("Target"),
                resultSet.getString("Creator"),
                resultSet.getLong("Duration")));
      }
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListMutes");
    }
    return result;
  }

  @Override
  public List<Warn> listWarns() {
    final List<Warn> result = new ArrayList<>();
    try (final ResultSet resultSet = query("SELECT * FROM {table} WHERE PUNISHTYPE='WARN'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(
            Warn.of(
                resultSet.getString("Target"),
                resultSet.getString("Creator"),
                resultSet.getLong("Duration")));
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
        query("SELECT * FROM {table} WHERE Type='BAN' AND Target='" + uuid + "'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(
            Ban.of(
                resultSet.getString("Target"),
                resultSet.getString("Creator"),
                resultSet.getLong("Duration")));
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
        query("SELECT * FROM {table} WHERE Type='MUTE' AND Target='" + uuid + "'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(
            Mute.of(
                resultSet.getString("Target"),
                resultSet.getString("Creator"),
                resultSet.getLong("Duration")));
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
        query("SELECT * FROM {table} WHERE Type='WARN' AND Target='" + uuid + "'")) {
      // No bans found
      if (resultSet == null) {
        return result;
      }

      while (resultSet.next()) {
        result.add(
            Warn.of(
                resultSet.getString("Target"),
                resultSet.getString("Creator"),
                resultSet.getLong("Duration")));
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
    update("UPDATE {table} SET removed=true WHERE Creation=" + ban.creation() + " AND Type='BAN'");
  }

  @Override
  public void removeMute(final @NonNull Mute mute) {
    update("UPDATE {table} SET removed=true WHERE Creation=" + mute.creation() + " AND Type='MUTE'");
  }

  @Override
  public void removeWarn(final @NonNull Warn warn) {
    update("UPDATE {table} SET removed=true WHERE Creation=" + warn.creation() + " AND Type='WARN'");
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
