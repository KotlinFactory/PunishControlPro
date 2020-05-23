package org.mineacademy.punishcontrol.spigot;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.PunishControlPluginBootstrap;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.commands.BackupCommand;
import org.mineacademy.punishcontrol.spigot.commands.MainCommand;
import org.mineacademy.punishcontrol.spigot.impl.SpigotExceptionHandler;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPlayerProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPluginDataProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPunishProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotTextureProvider;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotListenerImpl;
import org.mineacademy.punishcontrol.spigot.settings.SimpleSettingsInjector;
import org.spigotmc.SpigotConfig;

public final class PunishControl
    extends SimplePlugin
    implements PunishControlPluginBootstrap {

  private final SpigotComponent spigotModule = DaggerSpigotComponent.create();

  /**
   * Setting providers needed before the start
   */
  @Override
  protected void onPluginPreStart() {
    Providers.pluginDataProvider(SpigotPluginDataProvider.create());

    if (getDataFolder().exists()) {
      return;
    }

    Notifications.register(
        Notification
            .of("&6Take a tour")
            .text(
                "",
                "&7It seems like",
                "&7This is the first-time",
                "&7you use " + SimplePlugin.getNamed(),
                "&7on this server. &7Documentation:",
                "&7github.com/kangarko/punishcontrol/wiki"
            )
            .itemType(CompMaterial.ENCHANTED_GOLDEN_APPLE)
    );

    if (SpigotConfig.bungee) {
      Notifications.register(
          Notification
              .of("&6Use on Bungee")
              .text(
                  "",
                  "&7It seems like",
                  "&7You are running an BungeeCord server",
                  "&7PunishControl works brilliantly on BungeeCord",
                  "&7Make sure to install PunishControl on",
                  "&7the BungeeCord to make use of all its abilities"
              )
              .itemType(CompMaterial.GREEN_STAINED_GLASS)
      );
    }

  }

  @Override
  protected void onPluginStart() {
    Common.ADD_LOG_PREFIX = false;

    //Auto enabling offline-mode
    if (!SpigotConfig.bungee && !Bukkit.getServer().getOnlineMode()) {
      // TODO: find a better way doing this!
      final val cfg = LightningBuilder
          .fromPath("settings.yml",
              Providers.pluginDataProvider().getDataFolder().getAbsolutePath())
          .addInputStreamFromResource("settings.yml")
          .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
          .setDataType(DataType.SORTED)
          .createConfig()
          .addDefaultsFromInputStream();
      cfg.set("Advanced.Online_Mode", false);
      log("[PCP-Prestart] your server is in offline-mode - Changed config");
    }

    onPunishControlPluginStart();
    SimpleSettingsInjector.inject();

//    Common.ADD_TELL_PREFIX = true;
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to start our plugin.
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void registerCommands() {
    registerCommand(MainCommand.create(Settings.MAIN_COMMAND_ALIASES));
    registerCommand(BackupCommand.create());

    registerCommand(spigotModule.commandKick());
    registerCommand(spigotModule.commandBan());
    registerCommand(spigotModule.commandMute());
    registerCommand(spigotModule.commandWarn());

    registerCommand(spigotModule.commandUnBan());
    registerCommand(spigotModule.commandUnMute());
    registerCommand(spigotModule.commandUnWarn());
    registerCommand(spigotModule.commandPlayerInfo());
    registerCommand(spigotModule.searchCommand());
    registerCommand(spigotModule.chooseActionCommand());
  }

  @Override
  public void registerListener() {
    registerEvents(spigotModule.spigotDataSetter());
    registerEvents(SpigotListenerImpl.create());
  }

  @Override
  public void registerProviders() {
    Providers.playerProvider(SpigotPlayerProvider.newInstance());
    Providers.textureProvider(SpigotTextureProvider.newInstance());
    Providers.punishProvider(SpigotPunishProvider.newInstance());

    Providers.exceptionHandler(SpigotExceptionHandler.newInstance());
  }

  @Override
  public String chooseLanguage() {
    return Settings.LOCALE_PREFIX;
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods overridden from SimplePlugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public int getFoundedYear() {
    return 2019; // 31.12.2019
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods overridden from SimplePunishControlPlugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public List<Permission> permissions() {
    final val result = new ArrayList<Permission>();
    for (final SimpleCommand command : SimpleCommand.getRegisteredCommands()) {

      if (command.getPermission() == null) {
        continue;
      }

      result.add(Permission.of(command.getPermission(), command.getDescription()));
    }

    return result;
  }

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