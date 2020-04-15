package org.mineacademy.punishcontrol.core;

import de.leonhard.storage.Yaml;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.listener.Listeners;
import org.mineacademy.punishcontrol.core.listeners.PunishQueue;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.setting.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;

/**
 * Class for a unified startup of our Main-Plugin classes
 */
public interface SimplePunishControlPlugin {

  CoreComponent coreModule = DaggerCoreComponent.builder().build();
  String PREFIX = "§3Punish§bControl§5+ §7┃ ";
  String[] LOGO =
      new String[]{
          "§3 ____              _     _      ____            _             _ ",
          "§3|  _ \\ _   _ _ __ (_)___| |__  / ___|___  _ __ | |_ _ __ ___ | |",
          "§3| |_) | | | | '_ \\| / __| '_ \\| |   / _ \\| '_ \\| __| '__/ _ \\| |",
          "§5|  __/| |_| | | | | \\__ \\ | | | |__| (_) | | | | |_| | | (_) | |",
          "§5|_|    \\__,_|_| |_|_|___/_| |_|\\____\\___/|_| |_|\\__|_|  \\___/|_|"
      };

  static int getRandomNumberInRange(final int min, final int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    final Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

  // ----------------------------------------------------------------------------------------------------
  // Default methods
  // ----------------------------------------------------------------------------------------------------

  default void downloadDependencies() {
  }

  default String[] getStartupFinishedMessages() {
    return new String[]{
        "Top notch!",
        "Ban-hammer is swinging",
        "We're live!",
        "MineAcademy rules!",
        "Ready for takeover..."
    };
  }

  default void onPunishControlPluginStart() {
    log("§7*------------------- §3PunishControl-Pro - 2020  §7------------------*");

    log(" ");

    log(LOGO);

    log(" ");

    // Settings

    YamlStaticConfig.loadAll(Settings.class, Localization.class);

    try {
      final String language = chooseLanguage();

      PunishControlManager.setLanguage(language);

      log("Language: " + language);

    } catch (final Throwable throwable) {
      log("Couldn't choose language StorageProvider");
      saveError(throwable);
    }

    try {
      PunishControlManager.setStorageType(Settings.STORAGE_TYPE);

      log("Storage: " + Settings.STORAGE_TYPE.name());
    } catch (final Throwable throwable) {
      log("Couldn't choose StorageProvider");
      saveError(throwable);
    }

    log();

    // Startup
    try {
      downloadDependencies();
      log("Dependencies §l§a✔");
    } catch (final Throwable throwable) {
      log("Dependencies §l§c✘");
      saveError(throwable);
    }

    try {
      registerProviders();
      log("Providers §l§a✔");
    } catch (final Throwable throwable) {
      log("Providers §l§c✘");
      saveError(throwable);
    }

    //Settings! Must be loaded after providers
    try {
      registerCommands();
      log("Commands §l§a✔");
    } catch (final Throwable throwable) {
      log("Commands §l§c✘");
      saveError(throwable);
    }

    try {
      //Our core-listeners
      registerEvents(PunishQueue.create());
      registerEvents(coreModule.banListener());
      registerEvents(coreModule.muteListener());
      registerEvents(coreModule.banIpListener());
      registerEvents(coreModule.muteIpListener());
      //Spigot-Listeners
      registerListener();
      log("Listener §l§a✔");
    } catch (final Throwable throwable) {
      log("Commands §l§c✘");
      saveError(throwable);
    }

    try {
      //Your custom stuff.
      if (permissions() != null) {
        Permissions.registerAll(permissions());
      }

      log("Permissions §l§a✔");
    } catch (final Throwable throwable) {
      log("Permissions §l§c✘");
      saveError(throwable);
    }

    try {
      loadGroups();
      log("Groups §l§a✔");
    } catch (final Throwable throwable) {
      log("Groups §l§c✘");
      saveError(throwable);
    }

    try {
      PunishTemplates
          .load(new File(getWorkingDirectory() + "/templates"));
      log("Templates §l§a✔ ");
    } catch (final Throwable throwable) {
      log("Templates §l§c✘");
      saveError(throwable);
    }

    log();

    // Logging an random message
    final int index = getRandomNumberInRange(0,
        getStartupFinishedMessages().length - 1);

    log(getStartupFinishedMessages()[index]);

    log("§7*----------------------------------------------------------------*");
  }

  default void loadGroups() {
    final Yaml yaml = new Yaml("settings.yml", getWorkingDirectory());

    @SuppressWarnings("unchecked") final Map<String, Object> rawData = (Map<String, Object>) yaml
        .getMap("Groups");

    for (final val entry : rawData.entrySet()) { // Group-Names
      if (!(entry.getValue() instanceof Map)) {
        continue;
      }

      @SuppressWarnings("unchecked") final Map<String, Object> groupRawData = (Map<String, Object>) entry
          .getValue();

      final Group.GroupBuilder builder = Group.builder();

      builder.name(entry.getKey());
      builder.permission(groupRawData.get("Permission").toString());
      builder
          .priority(Integer.parseInt(groupRawData.get("Priority").toString()));

      @SuppressWarnings("unchecked") final Map<String, String> limits = (Map<String, String>) groupRawData
          .get("Limits");

      builder.banLimit(PunishDuration.of(limits.get("Ban")));
      builder.muteLimit(PunishDuration.of(limits.get("Mute")));
      builder.warnLimit(PunishDuration.of(limits.get("Warn")));
      builder.overridePunishes(groupRawData.get("Override_Punishes").toString()
          .equalsIgnoreCase("true"));
      builder.templateByPasses(
          (List<String>) groupRawData.get("Template_Bypasses")
      );

      Groups.registerGroup(builder.build());
    }
  }

  default void registerEvents(final Listener<?> listener) {
    Listeners.register(listener);
  }

  void log(@NonNull String... message);

  default void log() {
    log(" ");
  }

  String getWorkingDirectory();

  // ----------------------------------------------------------------------------------------------------
  // Abstract methods for startup
  // ----------------------------------------------------------------------------------------------------

  void registerCommands();

  void registerListener();

  void registerProviders();

  List<Permission> permissions();

  String chooseLanguage();

  void saveError(@NonNull Throwable t);
}
