package org.mineacademy.punishcontrol.proxy;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.proxy.commands.BanCommand;
import org.mineacademy.punishcontrol.proxy.commands.ChooseActionCommand;
import org.mineacademy.punishcontrol.proxy.commands.KickCommand;
import org.mineacademy.punishcontrol.proxy.commands.MuteCommand;
import org.mineacademy.punishcontrol.proxy.commands.PlayerInfoCommand;
import org.mineacademy.punishcontrol.proxy.commands.SearchCommand;
import org.mineacademy.punishcontrol.proxy.commands.StaffHistoryCommand;
import org.mineacademy.punishcontrol.proxy.commands.StaffRollbackCommand;
import org.mineacademy.punishcontrol.proxy.commands.UnBanCommand;
import org.mineacademy.punishcontrol.proxy.commands.UnMuteCommand;
import org.mineacademy.punishcontrol.proxy.commands.UnWarnCommand;
import org.mineacademy.punishcontrol.proxy.commands.WarnCommand;
import org.mineacademy.punishcontrol.proxy.listeners.ProxyDataSetter;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;
import org.mineacademy.punishcontrol.proxy.menus.browsers.AllPunishesBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.CustomItemBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishTemplateBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishedPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.StaffPunishesBrowser;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.LanguageSettingsMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.StorageSettingsMenu;

/**
 * Interface implemented by dagger
 */
@Component(modules = {Providers.class, ProxyModule.class})
public interface ProxyComponent {

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------
  ProxyDataSetter proxyDataSetter();

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

  SearchCommand searchCommand();

  ChooseActionCommand chooseActionCommand();

  StaffHistoryCommand staffHistoryCommand();

  StaffRollbackCommand staffRollbackCommand();

  // ----------------------------------------------------------------------------------------------------
  // Menus
  // ----------------------------------------------------------------------------------------------------

  MainMenu menuMain();

  PlayerBrowser playerBrowserMenu();

  AllPunishesBrowser punishBrowserMenu();

  PunishTemplateBrowser punishTemplateBrowser();

  PunishCreatorMenu punishCreatorMenu();

  PunishedPlayerBrowser punishedPlayerBrowser();

  StaffPunishesBrowser.Builder staffPunishesBrowserBuilder();

  //Settings - Menus

  SettingsBrowser settingsBrowser();

  StorageSettingsMenu mySqlSettingsMenu();

  LanguageSettingsMenu languageSettingsMenu();

  StorageSettingsMenu storageSettingsMenu();

  CustomItemBrowser customItemBrowser();
}
