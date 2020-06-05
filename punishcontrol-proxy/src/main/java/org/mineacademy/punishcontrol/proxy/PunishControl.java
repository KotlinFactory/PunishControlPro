package org.mineacademy.punishcontrol.proxy;

import de.exceptionflug.mccommons.commands.proxy.Commands;
import de.exceptionflug.protocolize.items.ItemType;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.bungee.SimpleBungee;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.Burst;
import org.mineacademy.punishcontrol.core.PunishControlPluginBootstrap;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.settings.Settings.Advanced;
import org.mineacademy.punishcontrol.proxy.commands.BackupCommand;
import org.mineacademy.punishcontrol.proxy.commands.MainCommand;
import org.mineacademy.punishcontrol.proxy.impl.ProxyExceptionHandler;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPlayerProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPluginDataProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPluginManager;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPunishProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyTextureProvider;
import org.mineacademy.punishcontrol.proxy.listeners.ProxyListenerImpl;
import org.mineacademy.punishcontrol.proxy.menus.settings.SimpleSettingsInjector;

public final class PunishControl
    extends SimplePlugin
    implements PunishControlPluginBootstrap {

  private final ProxyComponent proxyComponent = DaggerProxyComponent.create();

  /**
   * Setting providers needed before the start
   */
  @Override
  protected void onPluginPreStart() {
    // Need to be set before the main startup
    Providers.pluginDataProvider(ProxyPluginDataProvider.create());
    Providers.exceptionHandler(ProxyExceptionHandler.create());

    if (!getDataFolder().exists()) {
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
              .itemType(ItemType.ENCHANTED_GOLDEN_APPLE)
      );
    }
  }


  @Override
  protected void onPluginStart() {
    if (!ProxyServer.getInstance().getConfig().isOnlineMode()) {
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
      log("[PCP-Prestart] your server is in offline-mode");
    }

    Burst.setPlugin(this); // Set the plugin for our library

    onPunishControlPluginStart();
    // TODO: As long as CoreFoundation wasn't implemented we need to do this
    SimpleSettingsInjector.inject();
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to start our plugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void registerCommands() {
    registerCommand(MainCommand.create(Settings.MAIN_COMMAND_ALIASES));
    if (Advanced.ENABLE_BACKUPS) {
      registerCommand(BackupCommand.create());
    }

    Commands.registerCommand(proxyComponent.commandKick());
    Commands.registerCommand(proxyComponent.commandBan());
    Commands.registerCommand(proxyComponent.commandMute());
    Commands.registerCommand(proxyComponent.commandWarn());

    Commands.registerCommand(proxyComponent.commandUnBan());
    Commands.registerCommand(proxyComponent.commandUnMute());
    Commands.registerCommand(proxyComponent.commandUnWarn());
    Commands.registerCommand(proxyComponent.commandPlayerInfo());
    Commands.registerCommand(proxyComponent.searchCommand());
    Commands.registerCommand(proxyComponent.chooseActionCommand());
  }

  @Override
  public void registerListener() {
    registerEvents(ProxyListenerImpl.create());
    registerEvents(proxyComponent.proxyDataSetter());
  }

  @Override
  public void registerProviders() {

    Providers.playerProvider(ProxyPlayerProvider.newInstance());
    org.mineacademy.burst.provider.Providers
        .setUuidNameProvider(ProxyPlayerProvider.newInstance());

    // TextureProvider
    Providers.textureProvider(ProxyTextureProvider.newInstance());
    org.mineacademy.burst.provider.Providers
        .setTextureProvider(ProxyTextureProvider.newInstance());
    // Broadcaster
    Providers.punishProvider(ProxyPunishProvider.create());

    Providers.pluginManager(ProxyPluginManager.create());
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from SimplePlugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public int getFoundedYear() {
    return 2019; // 31.12.2019
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from SimplePunishControl
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
  public void saveError(@NonNull final Throwable t) {
    Common.error(t);
  }

  @Override
  public SimpleBungee getBungeeCord() {
    return null;
  }

  @Override
  public void log(final @NonNull String... message) {
    Common.log(message);
  }

  @Override
  public String getWorkingDirectory() {
    return getData().getAbsolutePath();
  }
}
