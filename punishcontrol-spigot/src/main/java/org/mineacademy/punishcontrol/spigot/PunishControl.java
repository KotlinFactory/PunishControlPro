package org.mineacademy.punishcontrol.spigot;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.SimplePunishControlPlugin;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.PermissionType;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.util.Permissions;
import org.mineacademy.punishcontrol.spigot.commands.MainCommand;
import org.mineacademy.punishcontrol.spigot.impl.SpigotExceptionHandler;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPlayerProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPluginDataProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPunishProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotTextureProvider;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotListenerImpl;
import org.mineacademy.punishcontrol.spigot.settings.SimpleSettingsInjector;

public final class PunishControl
    extends SimplePlugin
    implements SimplePunishControlPlugin {
  private final SpigotComponent spigotModule = DaggerSpigotComponent.create();

  /**
   * Called before we start loading the plugin, but after {@link
   * #onPluginLoad()}
   */
  @Override
  protected void onPluginPreStart() {
    //
    Providers.pluginDataProvider(SpigotPluginDataProvider.create());
  }

  @Override
  protected void onPluginStart() {
    Common.ADD_LOG_PREFIX = false;

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

    result.add(Permission
        .of(Permissions.PERMISSION_MENU_SETTINGS_STORAGE,
        "Access the storage-settings")
        .type(PermissionType.MENU));

    result.add(Permission
        .of(Permissions.PERMISSION_MENU_SETTINGS_PLAYER)
        .type(PermissionType.MENU));

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
