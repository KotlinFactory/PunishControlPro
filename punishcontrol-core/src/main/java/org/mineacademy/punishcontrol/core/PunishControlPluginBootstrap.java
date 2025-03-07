package org.mineacademy.punishcontrol.core;

import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.exceptions.LightningValidationException;
import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.listener.Listeners;
import org.mineacademy.punishcontrol.core.localization.Localizables;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.setting.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.core.util.PunishControlPermissions;

/**
 * Class for a unified startup of our Main-Plugin classes
 */
public interface PunishControlPluginBootstrap {

  CoreComponent coreComponent = DaggerCoreComponent.builder().build();
  String PREFIX = "§3Punish§bControl§5+ §7┃ ";
  List<String> KNOWN_LANGUAGES = Arrays.asList(
      "cz",
      "de",
      "en",
      "es",
      "fr",
      "hu",
      "nl",
      "pt",
      "ru",
      "sk");

  String[] LOGO =
      new String[]{
          "§3 ____              _     _      ____            _             _ ",
          "§3|  _ \\ _   _ _ __ (_)___| |__  / ___|___  _ __ | |_ _ __ ___ | |",
          "§3| |_) | | | | '_ \\| / __| '_ \\| |   / _ \\| '_ \\| __| '__/ _ \\| |",
          "§5|  __/| |_| | | | | \\__ \\ | | | |__| (_) | | | | |_| | | (_) | |",
          "§5|_|    \\__,_|_| |_|_|___/_| |_|\\____\\___/|_| |_|\\__|_|  \\___/|_|"
      };

  static int getRandomNumberInRange(final int min, final int max) {

    if (min >= max)
      throw new IllegalArgumentException("max must be greater than min");

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
    try {
      PunishControlSerializers.register();
    } catch (final Throwable throwable) {
      log("Couldn't register serializable's");
      saveError(throwable);
    }

    YamlStaticConfig.loadAll(Settings.class, Localization.class);
    coreComponent.itemSettings().load();

    try {
      final String language = Settings.LOCALE_PREFIX.toUpperCase();

      PunishControlManager.setLanguage(language);

      log("Language: " + language);

    } catch (final Throwable throwable) {
      log("Couldn't choose language");
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
      log("Dependencies... §l§aOK");
    } catch (final Throwable throwable) {
      log("Dependencies... §l§cfailed");
      saveError(throwable);
    }

    try {
      registerProviders();
      log("Providers... §l§aOK");
    } catch (final Throwable throwable) {
      log("Providers... §l§cfailed");
      saveError(throwable);
    }

    //Settings! Must be loaded after providers
    try {
      registerCommands();
      log("Commands... §l§aOK");
    } catch (final Throwable throwable) {
      log("Commands... §l§cfailed");
      saveError(throwable);
    }

    try {
      //Our core-listeners
      registerEvents(coreComponent.banListener());
      registerEvents(coreComponent.muteListener());
      registerEvents(coreComponent.banIpListener());
      registerEvents(coreComponent.muteIpListener());
      //Spigot-Listeners
      registerListener();
      log("Listener... §l§aOK");
    } catch (final Throwable throwable) {
      log("Listener... §l§cdone");
      saveError(throwable);
    }

    try {
      // Your custom stuff.
      if (permissions() != null)
        Permissions.registerAll(permissions());
      Permissions.addFromClass(PunishControlPermissions.class);
      log("Permissions... §l§aOK");
    } catch (final Throwable throwable) {
      log("Permissions... §l§cfailed");
      saveError(throwable);
    }

    try {
      loadGroups();
      log("Groups... §l§aOK");
    } catch (final Throwable throwable) {
      log("Groups... §l§cfailed");
      saveError(throwable);
    }

    try {
      PunishTemplates
          .load(new File(getWorkingDirectory() + "/templates"));
      log("Templates... §l§aOK");
    } catch (final Throwable throwable) {
      log("Templates... §l§cfailed");
      saveError(throwable);
    }

    log();

    if (Providers.storageProvider() instanceof MySQLStorageProvider)
      try {
        ((MySQLStorageProvider) Providers.storageProvider()).connect();
      } catch (final Throwable throwable) {
        throw new LightningValidationException(
            throwable,
            "Exception while connecting to MySQL!",
            "Likewise this is caused be wrong or missing credentials.");
      }

    try {
      Permissions.writeToFile();
    } catch (final Throwable throwable) {
      saveError(throwable);
    }

    runAsync(() -> {
      try {
        coreComponent.settingsInjector().startInjecting(classes());
        coreComponent.localizationInjector().startInjecting(classes());
      } catch (final Throwable throwable) {
        saveError(throwable);
      } finally {
        debug(
            "PunishControl-Localization",
            "Injected " + Localizables.localizables().size() + " localizable's");
      }
    });

    // Logging an random message
    final int randomIndex = getRandomNumberInRange(
        0,
        getStartupFinishedMessages().length - 1);

    log(getStartupFinishedMessages()[randomIndex]);

    for (final String knownLanguage : KNOWN_LANGUAGES)
      FileUtils.extractResource(
          getWorkingDirectory(),
          "localization/messages_" + knownLanguage + ".yml",
          false);

    log("§7*-----------------------------------------------------------------*");

//    System.out.println(
//        "Injected " + Localizables.localizables().size() + " localizables");

  }

  default void loadGroups() {
    final Yaml yaml = new Yaml("settings.yml", getWorkingDirectory());

    @SuppressWarnings("unchecked") final Map<String, Object> rawData = (Map<String, Object>) yaml
        .getMap("Groups");

    for (final val entry : rawData.entrySet()) { // Group-Names
      if (!(entry.getValue() instanceof Map))
        continue;

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
      builder.templateOnly(
          groupRawData.get("Template_Only").toString().equalsIgnoreCase("true"));
      builder.templateByPasses(
          (List<String>) groupRawData.get("Template_Bypasses")
                              );

      Groups.registerGroup(builder.build());
    }

    for (final Group group : Groups.registeredGroups())
      Permissions.register(
          Permission.of(group.permission(), "The group " + group.name())
                          );
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

  void runAsync(@NonNull final Runnable runnable);

  List<Class<?>> classes();

  void registerCommands();

  void registerListener();

  void registerProviders();

  List<Permission> permissions();

  void saveError(@NonNull Throwable t);

  void debug(String section, String... messages);
}
