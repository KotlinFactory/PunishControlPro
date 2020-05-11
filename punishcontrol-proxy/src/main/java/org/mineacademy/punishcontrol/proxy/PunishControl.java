package org.mineacademy.punishcontrol.proxy;

import de.exceptionflug.mccommons.commands.proxy.Commands;
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
import org.mineacademy.punishcontrol.core.CoreComponent;
import org.mineacademy.punishcontrol.core.DaggerCoreComponent;
import org.mineacademy.punishcontrol.core.PunishControlPluginBootstrap;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.proxy.commands.BackupCommand;
import org.mineacademy.punishcontrol.proxy.commands.MainCommand;
import org.mineacademy.punishcontrol.proxy.impl.ProxyExceptionHandler;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPlayerProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPluginDataProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPunishProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyTextureProvider;
import org.mineacademy.punishcontrol.proxy.listeners.ProxyListenerImpl;

public final class PunishControl
    extends SimplePlugin
    implements PunishControlPluginBootstrap {

  private final ProxyComponent proxyModule = DaggerProxyComponent.create();
  private final CoreComponent coreModule = DaggerCoreComponent.create();


  /**
   * Setting providers needed before the start
   */
  @Override
  protected void onPluginPreStart() {
    Providers.pluginDataProvider(ProxyPluginDataProvider.create());
  }

  @Override
  protected void onPluginStart() {
    Burst.setPlugin(this); // Set the plugin for our library
    ProxyServer.getInstance().getConfig().isOnlineMode();
    onPunishControlPluginStart();
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to start our plugin
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void registerCommands() {
    registerCommand(MainCommand.create(Settings.MAIN_COMMAND_ALIASES));
    registerCommand(BackupCommand.create());

    Commands.registerCommand(proxyModule.commandKick());
    Commands.registerCommand(proxyModule.commandBan());
    Commands.registerCommand(proxyModule.commandMute());
    Commands.registerCommand(proxyModule.commandWarn());

    Commands.registerCommand(proxyModule.commandUnBan());
    Commands.registerCommand(proxyModule.commandUnMute());
    Commands.registerCommand(proxyModule.commandUnWarn());
    Commands.registerCommand(proxyModule.commandPlayerInfo());
    Commands.registerCommand(proxyModule.searchCommand());
    Commands.registerCommand(proxyModule.chooseActionCommand());
  }

  @Override
  public void registerListener() {
    registerEvents(ProxyListenerImpl.create());
    registerEvents(proxyModule.proxyDataSetter());
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
    Providers.punishProvider(ProxyPunishProvider.newInstance());

    Providers.exceptionHandler(ProxyExceptionHandler.newInstance());
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
  public String chooseLanguage() {
    return "ENG";
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
