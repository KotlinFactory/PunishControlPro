package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
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

	private final MySQLConfig config;

	//We need the mysql config here


	@Override protected void onConnected() {
		update("CREATE TABLE IF NOT EXISTS Bans(" +
			"Target varchar(64), " +
			"Creator varchar(64), " +
			"Reason varchar(64), " +
			"IP varchar(64), " +
			"Duration bigint, " +
			"Creation bigint, " +
			"Removed boolean, PRIMARY KEY (Creation))");

	}

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
	public MySQLStorageProvider(final MySQLConfig config) {
		super();
		this.config = config;
	}

	@Override public List<Ban> listBans() {
		try (final ResultSet resultSet = query("SELECT * FROM {table} WHERE PUNISHTYPE='BAN'")) {

		} catch (final SQLException ex) {

		}
		return null;
	}

	@Override public List<Mute> listMutes() {
		return new ArrayList<>();
	}

	@Override public List<Warn> listWarns() {
		return new ArrayList<>();
	}

	@Override public List<Ban> listBans(@NonNull final UUID uuid) {
		return null;
	}

	@Override public List<Mute> listMutes(@NonNull final UUID uuid) {
		return null;
	}

	@Override public List<Warn> listWarns(@NonNull final UUID uuid) {
		return null;
	}

	@Override public void saveBan(@NonNull final Ban ban) {

	}

	@Override public void saveMute(@NonNull final Mute mute) {

	}

	@Override public void saveWarn(@NonNull final Warn warn) {
	}

	@Override public void removeBan(final @NonNull Ban ban) {

	}

	@Override public void removeMute(final @NonNull Mute mute) {

	}

	@Override public void removeWarn(final @NonNull Warn warn) {

	}
}
