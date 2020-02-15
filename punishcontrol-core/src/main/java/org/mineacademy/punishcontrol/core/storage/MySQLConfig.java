package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.internal.exception.LightningValidationException;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.mineacademy.punishcontrol.core.provider.providers.WorkingDirectoryProvider;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

	/*

	Table: 'PunishControl'
	Host: ''
	Port: 3306
	User: ''
	Password: ''

	 */

public final class MySQLConfig {
	private final File file;
	private List<String> lines = new ArrayList<>();
	private long lastReloaded;

	public static MySQLConfig newInstance(@NonNull final WorkingDirectoryProvider provider) {
		return new MySQLConfig(provider);
	}

	@Inject
	public MySQLConfig(@NonNull final WorkingDirectoryProvider provider) {
		this.file = FileUtils.getAndMake(new File(provider.getDataFolder(), "MySQL.cnf"));
	}

	@SneakyThrows
	public void read() {
		if (!FileUtils.hasChanged(file, lastReloaded)) {
			return;
		}

		lastReloaded = System.currentTimeMillis();
		lines = Files.readAllLines(file.toPath());
	}

	public String getHost() {
		return get("Host");
	}

	public String getDataBase() {
		return get("Database");
	}

	public String getUser() {
		return get("User");
	}

	public String getPassword() {
		return get("Password");
	}

	public int getPort() {
		return Integer.parseInt(get("Port"));
	}

	public void setHost(final String value) {
		set("Host", value);
	}

	public void setDataBase(final String value) {
		set("Database", value);
	}

	public void setUser(final String value) {
		set("User", value);
	}

	public void setPassword(final String value) {
		set("Password", value);
	}

	public void setPort(final int port) {
		set("Port", port);
	}

	@SneakyThrows
	private void set(final String key, final Object value) {
		read();

		final List<String> result = new ArrayList<>();

		for (final String line : lines) {
			if (line.startsWith("#")) {
				continue;
			}

			final String[] splitted = line.split(":");

			Valid.checkBoolean(splitted.length == 2, "Invalid syntax. ", line);

			if (!splitted[0].equals(key)) {
				result.add(line);
			}

			splitted[1] = value.toString();

			result.add(splitted[0] + splitted[1]);
		}

		lines = result;
		Files.write(file.toPath(), lines);
	}

	private String get(final String key) {
		read();
		for (final String line : lines) {
			final String[] splitted = line.split(":");

			Valid.checkBoolean(splitted.length == 2, "Invalid syntax. ", line);

			if (splitted[0].startsWith("Host")) {
				return splitted[1];
			}
		}
		throw new LightningValidationException("Couldn't find key '" + key + "'");
	}
}

