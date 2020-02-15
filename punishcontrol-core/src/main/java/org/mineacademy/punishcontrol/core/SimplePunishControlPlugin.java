package org.mineacademy.punishcontrol.core;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.storage.StorageType;

import java.util.Random;

/**
 * Class for a unified startup of
 * our Main-Plugin classes
 */

public interface SimplePunishControlPlugin {

	static int getRandomNumberInRange(final int min, final int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		final Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}


	// ----------------------------------------------------------------------------------------------------
	// Methods to be implemented by our two PunishControl classes
	// ----------------------------------------------------------------------------------------------------

	String PREFIX = "§3Punish§bControl§5+ §7┃ ";
	String[] LOGO = new String[]{
			"§3 ____              _     _      ____            _             _ ",
			"§3|  _ \\ _   _ _ __ (_)___| |__  / ___|___  _ __ | |_ _ __ ___ | |",
			"§3| |_) | | | | '_ \\| / __| '_ \\| |   / _ \\| '_ \\| __| '__/ _ \\| |",
			"§5|  __/| |_| | | | | \\__ \\ | | | |__| (_) | | | | |_| | | (_) | |",
			"§5|_|    \\__,_|_| |_|_|___/_| |_|\\____\\___/|_| |_|\\__|_|  \\___/|_|"
	};

	void log(@NonNull String... message);

	default void log() {
		log(" ");
	}

	void registerCommands();

	void registerListener();

	String chooseLanguage();

	StorageType chooseStorageProvider();

	void saveError(@NonNull Throwable t);

	default void downloadDependencies() {
	}

	default String[] getStartupFinishedMessages() {
		return new String[]{"Top notch!", "Ban-hammer is swinging", "We're live!", "MineAcademy rules!"};
	}

	default void onPunishControlPluginStart() {
		log("§7*---------------- §3PunishControl-Pro - 2020  §7---------------*");
		log(" ");

		log(LOGO);

		log(" ");

		//Settings

		try {
			final String language = chooseLanguage();

			PunishControlManager.setLanguage(language);

			log("Language: " + language);

		} catch (final Throwable throwable) {
			log("Couldn't choose language StorageProvider");
			saveError(throwable);
		}

		try {
			final StorageType storageType = chooseStorageProvider();

			PunishControlManager.setStorageType(storageType);

			log("Storage: " + storageType.name());
		} catch (final Throwable throwable) {
			log("Couldn't choose StorageProvider");
			saveError(throwable);
		}

		log(" ");

		//Startup
		try {
			downloadDependencies();
			log("Dependencies §l§a✔");
		} catch (final Throwable throwable) {
			log("Dependencies §l§c✘");
			saveError(throwable);
		}

		try {
			registerCommands();
			log("Commands §l§a✔");
		} catch (final Throwable throwable) {
			log("Commands §l§c✘");
			saveError(throwable);
		}

		try {
			registerListener();
			log("Listener §l§a✔");
		} catch (final Throwable throwable) {
			log("Commands §l§c✘");
			saveError(throwable);
		}

		log();

		//Logging an random message
		final int index = getRandomNumberInRange(0, getStartupFinishedMessages().length - 1);

		log(getStartupFinishedMessages()[index]);

		log("§7*--------------------------------------------------------------*");
	}
}
