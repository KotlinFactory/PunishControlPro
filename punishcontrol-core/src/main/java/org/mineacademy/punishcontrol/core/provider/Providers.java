package org.mineacademy.punishcontrol.core.provider;

import dagger.Module;
import dagger.Provides;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Named;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.localization.Localizable;
import org.mineacademy.punishcontrol.core.localization.Localizables;
import org.mineacademy.punishcontrol.core.providers.*;
import org.mineacademy.punishcontrol.core.punish.importer.PunishImporter;
import org.mineacademy.punishcontrol.core.punish.importer.PunishImporters;
import org.mineacademy.punishcontrol.core.setting.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

/**
 * -- Heart of PunishControl --
 *
 * <p>Central provider for dependencies used in PunishControlPro.
 *
 * <p>It is also used by Dagger
 */
@Module
@Accessors(fluent = true)
public final class Providers {

  @Setter
  @NonNull
  private static PlayerProvider playerProvider;

  @Setter
  @NonNull
  private static TextureProvider textureProvider;

  @Setter
  @NonNull
  private static PluginDataProvider pluginDataProvider;

  @Setter
  @NonNull
  private static PunishProvider punishProvider;

  @Setter
  @NonNull
  private static ExceptionHandler exceptionHandler;
  @Setter
  @NonNull
  private static StorageProvider storageProvider;

  @Setter
  @NonNull
  private static PluginManager pluginManager;

  @Setter
  @NonNull
  private static Yaml settings;

  @NonNull @Setter
  private static List<Yaml> localizableFiles;

  @Setter
  @NonNull
  private static Yaml localization;

  @Provides
  public static PlayerProvider playerProvider() {
    Valid.notNull(playerProvider, "PlayerProvider not yet set.");

    return playerProvider;
  }

  @Provides
  public static TextureProvider textureProvider() {
    Valid.notNull(textureProvider, "TextureProvider not yet set.");

    return textureProvider;
  }

  @Provides
  public static PluginDataProvider pluginDataProvider() {
    Valid.notNull(pluginDataProvider, "PluginDataProvider not yet set");

    return pluginDataProvider;
  }

  @Provides
  public static PunishProvider punishProvider() {
    Valid.notNull(punishProvider, "PunishProvider not yet set");

    return punishProvider;
  }

  @Provides
  public static ExceptionHandler exceptionHandler() {
    Valid.notNull(exceptionHandler, "ExceptionHandler not yet set");

    return exceptionHandler;
  }

  @Provides
  public static PluginManager pluginManager() {
    Valid.notNull(pluginManager, "PluginManager not yet set");

    return pluginManager;
  }

  @Named("settings")
  @Provides
  public static Yaml settings() {
    if (settings != null)
      return settings;
    return settings = LightningBuilder
        .fromPath("settings.yml", pluginDataProvider.getDataFolder().getAbsolutePath())
        .addInputStreamFromResource("settings.yml")
        .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        .setDataType(DataType.SORTED)
        .createConfig()
        .addDefaultsFromInputStream();
  }

  @Named("localization")
  @Provides
  public static Yaml localization() {
    if (localization != null)
      return localization;

    return localization = createLocalizationYaml(
        pluginDataProvider(),
        SimpleSettings.LOCALE_PREFIX);
  }

  public static Yaml createLocalizationYaml(@NonNull final String localePrefix) {
    return createLocalizationYaml(pluginDataProvider(), localePrefix);
  }

  private static Yaml createLocalizationYaml(
      @NonNull final PluginDataProvider pluginDataProvider,
      @NonNull final String localePrefix) {

    final String name = "messages_" + localePrefix;

    return LightningBuilder
        .fromPath(name, pluginDataProvider.getDataFolder().getAbsolutePath() +
                        "/localization/")
        .addInputStreamFromResource("localization/" + name + ".yml")
        .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        .setDataType(DataType.SORTED)
        .createConfig()
        .addDefaultsFromInputStream();

  }

  // ----------------------------------------------------------------------------------------------------
  // Dagger only
  // ----------------------------------------------------------------------------------------------------

  /**
   * Heavy-weight operation! Loads a lot of files! Be mindful with using this. --> Should
   * be used asynchronously only!
   */
  @Provides
  public static Collection<Yaml> localizationFiles() {

    if (localizableFiles != null) {
      return localizableFiles;
    }
    return localizableFiles = FileUtils.listFiles(new File(
        pluginDataProvider().getDataFolder() + "/localization"), ".yml")
        .stream()
        .map(Yaml::new)
        .collect(Collectors.toList());
  }

  @Provides
  @Named("localizables")
  public static Collection<Localizable> localizables() {
    return Localizables.localizables();
  }

  @Provides
  @Named("offline-players")
  public static Collection<UUID> offlinePlayers() {
    return playerProvider().offlinePlayers();
  }

  @Provides
  @Named("importers")
  public static Collection<PunishImporter> importers() {
    return PunishImporters.importers();
  }

  //Lazy loading our storage provider & allowing to reset it
  @Provides
  public static StorageProvider storageProvider() {
    return storageProvider == null
        ? storageProvider = PunishControlManager.storageType().getStorageProvider()
        : storageProvider;
  }
}
