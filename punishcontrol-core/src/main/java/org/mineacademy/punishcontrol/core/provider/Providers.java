package org.mineacademy.punishcontrol.core.provider;

import dagger.Module;
import dagger.Provides;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;
import org.mineacademy.punishcontrol.core.providers.PunishProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.storage.MySQLConfig;
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



  // StorageProvider can't be set.
  public static StorageProvider storageProvider() {
    return PunishControlManager.storageType().getStorageProvider();
  }

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

  // ----------------------------------------------------------------------------------------------------
  // Dagger only
  // ----------------------------------------------------------------------------------------------------

  @Provides
  public static MySQLConfig config(
      @NonNull final PluginDataProvider workingDirectoryProvider) {
    Valid
        .notNull(workingDirectoryProvider, "Working directoryProvider is null");

    return MySQLConfig.newInstance(workingDirectoryProvider);
  }

  @Provides
  public static StorageProvider provider() {
    return PunishControlManager.storageType().getStorageProvider();
  }

  //	@Provides
  //	public static MySQLStorageProvider mySQLStorageProvider() {
  //		return (MySQLStorageProvider) StorageType.MYSQL.getStorageProvider();
  //	}
  //
  //	@Provides
  //	public static JsonStorageProvider jsonStorageProvider() {
  //		return (JsonStorageProvider) StorageType.JSON.getStorageProvider();
  //	}
}
