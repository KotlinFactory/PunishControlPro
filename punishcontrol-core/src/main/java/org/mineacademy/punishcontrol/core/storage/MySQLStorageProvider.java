package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.Yaml;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
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
      "INSERT INTO {table} (type, target, targetLastName, creator, creatorLastName, reason, lastIp, duration, creation, removed)"
      + " VALUES('{type}', '{target}', '{targetLastName}', '{creator}', '{creatorLastName}', '{reason}', '{lastIp}', {duration}, '{creation}', {removed})";

  /*
"INSERT INTO {table} (type, target, targetLastName, creator, creatorLastName, reason, lastIp, duration, creation, removed)"
 + " VALUES('{type}', '{target}', '{targetLastName}', '{creator}', '{creatorLastName}', '{reason}', '{lastIp}, {duration}', {creation}, {removed})";
*/
  //
  private final ExceptionHandler exceptionHandler;
  private final PlayerProvider playerProvider;
  private final Yaml settings;
  private boolean connected;

  /*
  `id`, `type`, `target`, `targetLastName`, `creator`, `creatorLastName`, `reason`, `lastIp`, `duration`, `creation`, `removed`
   */

  // We need the mysql config here

  @Inject
  public MySQLStorageProvider(
      @NonNull final ExceptionHandler exceptionHandler,
      @NonNull final PlayerProvider playerProvider,
      @NonNull @Named("settings") final Yaml settings) {
    this.exceptionHandler = exceptionHandler;
    this.playerProvider = playerProvider;
    this.settings = settings;
    addVariable("table", "punishment");
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
    boolean needsUpgrade = false;

    try {
      query0("SELECT * FROM punishment WHERE targetLastName = 1 LIMIT 1");
    } catch (final Throwable throwable) {
      needsUpgrade = true;
    }

    update("CREATE TABLE IF NOT EXISTS `punishment` (\n"
           + "  `id` int(11) NOT NULL,\n"
           + "  `type` enum('BAN','MUTE','WARN','KICK') NOT NULL,\n"
           + "  `target` varchar(36) NOT NULL,\n"
           + "  `targetLastName` varchar(40) NOT NULL,\n"
           + "  `creator` varchar(36) NOT NULL,\n"
           + "  `creatorLastName` varchar(40) NOT NULL,\n"
           + "  `reason` varchar(255) NOT NULL,\n"
           + "  `lastIp` varchar(45) NOT NULL,\n"
           + "  `duration` bigint(20) NOT NULL,\n"
           + "  `creation` timestamp NULL DEFAULT NULL,\n"
           + "  `removed` tinyint(1) DEFAULT NULL\n"
           + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;\n");

    if (needsUpgrade && settings.getBoolean("MySQL.Remigrate"))
      onInitialize();

    if (tableExist(connection, "Punishes"))
      onMigrate();

    connected = true;
  }

  private void onMigrate() {
    // Migrating from old data
    System.out.println("Started Migration");
    try {
      ResultSet resultSet = query("SELECT * FROM Punishes");
      if (resultSet == null) {
        return;
      }

      while (resultSet.next())
        saveData(
            PunishType.valueOf(resultSet.getString("Type")),
            UUID.fromString(resultSet.getString("Creator")),
            UUID.fromString(resultSet.getString("Target")),
            resultSet.getString("reason"),
            resultSet.getString("Ip"),
            resultSet.getLong("Duration"),
            resultSet.getLong("Creation"),
            resultSet.getBoolean("Removed"));

    } catch (final Throwable throwable) {
      //
      throwable.printStackTrace();
    } finally {
      settings.set("MySQL.Remigrate", false);
    }
  }

  private void onInitialize() {
    System.out.println("Initialized MySQL");
    update("ALTER TABLE punishment \n"
           + "  ADD PRIMARY KEY (`id`),\n"
           + "  ADD KEY `target` (`target`),\n"
           + "  ADD KEY `creator` (`creator`),\n"
           + "  ADD KEY `type` (`type`),\n"
           + "  ADD KEY `targetLastName` (`targetLastName`),\n"
           + "  ADD KEY `creatorLastName` (`creatorLastName`),\n"
           + "  ADD KEY `duration` (`duration`),\n"
           + "  ADD KEY `lastIp` (`lastIp`),\n"
           + "  ADD KEY `creation` (`creation`)");

    update(
        "ALTER TABLE `punishment` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT = 3");

  }

  public boolean connected() {
    return connected;
  }

  // ----------------------------------------------------------------------------------------------------
  // Listing punishments
  // ----------------------------------------------------------------------------------------------------

  @Override
  public List<Ban> listBans() {
    final List<Ban> result = new ArrayList<>();
    try (
        final ResultSet resultSet = query(
            "SELECT * FROM punishment WHERE Type='BAN'")) {
      // No bans found
      if (resultSet == null)
        return result;

      while (resultSet.next())
        result.add(banFromResultSet(resultSet));
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListBans");
    }
    return result;
  }

  @Override
  public List<Mute> listMutes() {
    final List<Mute> result = new ArrayList<>();
    try (
        final ResultSet resultSet = query(
            "SELECT * FROM punishment WHERE Type='MUTE'")) {
      // No bans found
      if (resultSet == null)
        return result;

      while (resultSet.next())
        result.add(muteFromResultSet(resultSet));
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListMutes");
    }
    return result;
  }

  @Override
  public List<Warn> listWarns() {
    final List<Warn> result = new ArrayList<>();
    try (
        final ResultSet resultSet = query(
            "SELECT * FROM punishment WHERE TYPE='WARN'")) {
      // No bans found
      if (resultSet == null)
        return result;

      while (resultSet.next())
        result.add(warnFromResultSet(resultSet));
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListWarns");
    }
    return result;
  }

  @Override
  public List<Ban> listBans(@NonNull final UUID uuid) {
    final List<Ban> result = new ArrayList<>();
    try (
        final ResultSet resultSet =
            query("SELECT * FROM punishment WHERE Type='BAN' AND Target='" + uuid
                  + "'")) {
      // No bans found
      if (resultSet == null)
        return result;

      while (resultSet.next())
        result.add(banFromResultSet(resultSet));
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListBans-UUID");
    }
    return result;
  }

  @Override
  public List<Mute> listMutes(@NonNull final UUID uuid) {
    final List<Mute> result = new ArrayList<>();
    try (
        final ResultSet resultSet =
            query("SELECT * FROM punishment WHERE Type='MUTE' AND Target='" + uuid
                  + "'")) {
      // No bans found
      if (resultSet == null)
        return result;

      while (resultSet.next())
        result.add(muteFromResultSet(resultSet));
    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListMutes-UUID");
    }
    return result;
  }

  @Override
  public List<Warn> listWarns(@NonNull final UUID uuid) {
    final List<Warn> result = new ArrayList<>();
    try (
        final ResultSet resultSet =
            query("SELECT * FROM punishment WHERE Type='WARN' AND Target='" + uuid
                  + "'")) {
      // No bans found
      if (resultSet == null)
        return result;
      while (resultSet.next())
        result.add(warnFromResultSet(resultSet));

    } catch (final SQLException ex) {
      handleMySQLException(ex, "ListWarns-UUID");
    }
    return result;
  }

  // ----------------------------------------------------------------------------------------------------
  // Saving punishments
  // ----------------------------------------------------------------------------------------------------

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

  // ----------------------------------------------------------------------------------------------------
  // Removing punishments
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void removeBan(final @NonNull Ban ban) {
    update("UPDATE punishment SET removed=true WHERE creation="
           + "'" + new Timestamp(ban.creation()).toString() + "'"
           + " AND Type='BAN'");
  }

  @Override
  public void removeMute(final @NonNull Mute mute) {
    update(
        "UPDATE punishment SET removed=true WHERE creation="
        + "'" + new Timestamp(mute.creation()).toString() + "'"
        + " AND Type='MUTE'");
  }

  @Override
  public void removeWarn(final @NonNull Warn warn) {
    update(
        "UPDATE punishment SET removed=true WHERE creation="
        + "'" + new Timestamp(warn.creation()).toString() + "'"
        + " AND Type='WARN'");
  }

  // ----------------------------------------------------------------------------------------------------
  // Internal helper methods
  // ----------------------------------------------------------------------------------------------------

  private Ban banFromResultSet(final ResultSet resultSet) throws SQLException {
    return Ban.of(
        resultSet.getString("target"),
        resultSet.getString("creator"),
        PunishDuration.of(resultSet.getLong("Duration")))
        .ip(resultSet.getString("lastIp"))
        .creation(resultSet.getTimestamp("Creation"))
        .removed(resultSet.getBoolean("Removed"))
        .reason(resultSet.getString("Reason"));
  }

  private Mute muteFromResultSet(final ResultSet resultSet) throws SQLException {
    return Mute.of(
        resultSet.getString("target"),
        resultSet.getString("creator"),
        PunishDuration.of(resultSet.getLong("Duration")))
        .ip(resultSet.getString("lastIp"))
        .creation(resultSet.getTimestamp("Creation"))
        .removed(resultSet.getBoolean("Removed"))
        .reason(resultSet.getString("Reason"));
  }

  private Warn warnFromResultSet(final ResultSet resultSet) throws SQLException {
    return Warn.of(
        resultSet.getString("target"),
        resultSet.getString("creator"),
        PunishDuration.of(resultSet.getLong("Duration")))
        .ip(resultSet.getString("lastIp"))
        .creation(resultSet.getTimestamp("Creation"))
        .removed(resultSet.getBoolean("Removed"))
        .reason(resultSet.getString("Reason"));
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
            .replace(
                "{targetLastName}",
                playerProvider.findName(target).orElse("unknown"))
            .replace("{creator}", creator.toString())
            .replace(
                "{creatorLastName}",
                playerProvider.findName(creator).orElse("unknown"))
            .replace("{reason}", reason)
            .replace("{lastIp}", ip)
            .replace("{duration}", duration + "")
            .replace("{creation}", new Timestamp(creation).toString())
            .replace("{removed}", (removed ? 1 + "" : 0 + "")));
  }

  private static boolean tableExist(final Connection connection, final String tableName) {
    try {
      return tableExist0(connection, tableName);
    } catch (final SQLException sqlException) {
      return false;
    }
  }

  private static boolean tableExist0(
      Connection conn,
      String tableName) throws SQLException {
    try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
      while (rs.next()) {
        String tName = rs.getString("TABLE_NAME");
        if (tName != null && tName.equals(tableName))
          return true;
      }
      return false;
    }
  }
}
