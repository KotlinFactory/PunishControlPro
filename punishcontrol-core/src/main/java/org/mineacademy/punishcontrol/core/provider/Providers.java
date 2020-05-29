package org.mineacademy.punishcontrol.core.provider;

import dagger.Module;
import dagger.Provides;
import de.leonhard.storage.util.Valid;
import java.util.Collection;
import java.util.UUID;
import javax.inject.Named;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.providers.PunishProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
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

  // ----------------------------------------------------------------------------------------------------
  // Dagger only
  // ----------------------------------------------------------------------------------------------------

  @Provides
  @Named("offline-players")
  public Collection<UUID> offlinePlayers() {
    return playerProvider().offlinePlayers();
  }

  //Lazy loading our storage provider & allowing to reset it
  @Provides
  public static StorageProvider storageProvider() {
    return storageProvider == null
        ? storageProvider = PunishControlManager.storageType().getStorageProvider()
        : storageProvider;
  }
}
