package org.mineacademy.punishcontrol.proxy;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.proxy.commands.BanCommand;
import org.mineacademy.punishcontrol.proxy.commands.KickCommand;
import org.mineacademy.punishcontrol.proxy.commands.MuteCommand;
import org.mineacademy.punishcontrol.proxy.commands.PlayerInfoCommand;
import org.mineacademy.punishcontrol.proxy.commands.UnBanCommand;
import org.mineacademy.punishcontrol.proxy.commands.UnMuteCommand;
import org.mineacademy.punishcontrol.proxy.commands.UnWarnCommand;
import org.mineacademy.punishcontrol.proxy.commands.WarnCommand;
import org.mineacademy.punishcontrol.proxy.listeners.ProxyDataSetter;

/** Interface implemented by dagger */
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
}
