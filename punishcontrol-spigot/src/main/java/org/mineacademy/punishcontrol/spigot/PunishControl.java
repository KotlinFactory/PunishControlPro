package org.mineacademy.punishcontrol.spigot;

import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.SimplePunishControlPlugin;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.spigot.commands.DevCommand;
import org.mineacademy.punishcontrol.spigot.commands.MainCommand;
import org.mineacademy.punishcontrol.spigot.impl.SpigotExceptionHandler;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPlayerProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPluginDataProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPunishProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotSettingsProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotTextureProvider;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotListenerImpl;
import org.mineacademy.punishcontrol.spigot.settings.Localization;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

public final class PunishControl extends SimplePlugin implements SimplePunishControlPlugin {
  private final SpigotComponent spigotModule = DaggerSpigotComponent.builder().build();

  @Override
  protected void onPluginStart() {
    Common.ADD_LOG_PREFIX = false;

    onPunishControlPluginStart();

    // Bypass UltraPunishments
    // Common.runLater(40, this::registerCommands);
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to start our plugin.
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void registerCommands() {
    registerCommand(MainCommand.create(Settings.MAIN_COMMAND_ALIASES));
    registerCommand(DevCommand.create());
    registerCommand(spigotModule.commandKick());
    registerCommand(spigotModule.commandBan());
    registerCommand(spigotModule.commandMute());
    registerCommand(spigotModule.commandWarn());

    registerCommand(spigotModule.commandUnBan());
    registerCommand(spigotModule.commandUnMute());
    registerCommand(spigotModule.commandUnWarn());
    registerCommand(spigotModule.commandPlayerInfo());
  }

  @Override
  public void registerListener() {
    registerEvents(spigotModule.spigotDataSetter());
    registerEvents(SpigotListenerImpl.create());
  }

  @Override
  public void registerProviders() {
    Providers.pluginDataProvider(SpigotPluginDataProvider.create());
    Providers.playerProvider(SpigotPlayerProvider.newInstance());
    Providers.textureProvider(SpigotTextureProvider.newInstance());
    Providers.punishProvider(SpigotPunishProvider.newInstance());

    Providers.settingsProvider(SpigotSettingsProvider.newInstance());

    Providers.exceptionHandler(SpigotExceptionHandler.newInstance());
  }

  @Override
  public String chooseLanguage() {
    return Settings.LOCALE_PREFIX;
  }

  @Override
  public StorageType chooseStorageProvider() {
    return Settings.STORAGE_TYPE;
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods overridden from SimplePlugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public List<Class<? extends YamlStaticConfig>> getSettings() {
    return Arrays.asList(Settings.class, Localization.class);
  }

  @Override
  public int getFoundedYear() {
    return 2019; // 31.12.2019
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods overridden from SimplePunishControlPlugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void log(final @NonNull String... message) {
    Common.log(message);
  }

  @Override
  public String getWorkingDirectory() {
    return getData().getAbsolutePath();
  }

  @Override
  public void saveError(@NonNull final Throwable t) {
    Common.error(t);
  }

  @Override
  public V getMinimumVersion() {
    return V.v1_8;
  }
}
