package org.mineacademy.punishcontrol.proxy;

import dagger.Module;
import dagger.Provides;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

@Module
public class ProxyModule {
  @Provides
  public ProxyServer proxyServer() {return ProxyServer.getInstance();}

  @Provides
  public List<ProxiedPlayer> onlinePlayers() {
    return new ArrayList<>(proxyServer().getPlayers());
  }
}

