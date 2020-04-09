package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.commands.BanCommand;
import org.mineacademy.punishcontrol.spigot.commands.KickCommand;
import org.mineacademy.punishcontrol.spigot.commands.MuteCommand;
import org.mineacademy.punishcontrol.spigot.commands.PlayerInfoCommand;
import org.mineacademy.punishcontrol.spigot.commands.UnBanCommand;
import org.mineacademy.punishcontrol.spigot.commands.UnMuteCommand;
import org.mineacademy.punishcontrol.spigot.commands.UnWarnCommand;
import org.mineacademy.punishcontrol.spigot.commands.WarnCommand;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotDataSetter;
import org.mineacademy.punishcontrol.spigot.menus.DurationChooserMenu;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.PunishChooserMenu;
import org.mineacademy.punishcontrol.spigot.menus.PunishCreatorMenu;
import org.mineacademy.punishcontrol.spigot.menus.browser.PlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browser.PunishBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.ChooseSettingsMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.LanguageSettingsMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.StorageSettingsMenu;

/**
 * Interface implemented by dagger
 */
@Component(modules = {Providers.class, SpigotModule.class})
public interface SpigotComponent {

  StorageSettingsMenu mysqlModule();

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------

  SpigotDataSetter spigotDataSetter();

  // ----------------------------------------------------------------------------------------------------
  // Commands
  // ----------------------------------------------------------------------------------------------------

  KickCommand commandKick();

  BanCommand commandBan();

  MuteCommand commandMute();

  WarnCommand commandWarn();

  UnBanCommand commandUnBan();

  UnMuteCommand commandUnMute();

  UnWarnCommand commandUnWarn();

  PlayerInfoCommand commandPlayerInfo();

  // ----------------------------------------------------------------------------------------------------
  // Menus
  // ----------------------------------------------------------------------------------------------------

  DurationChooserMenu durationChooserMenu();

  PunishChooserMenu punishChooserMenu();

  MainMenu menuMain();

  PlayerBrowser playerBrowserMenu();

  PunishBrowser punishBrowserMenu();

  PunishCreatorMenu punishCreatorMenu();

 //Settings - Menus

  ChooseSettingsMenu chooseSettingsMenu();

  StorageSettingsMenu mySqlSettingsMenu();

  LanguageSettingsMenu languageSettingsMenu();
}
