package org.mineacademy.punishcontrol.proxy;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.provider.Providers;
import org.mineacademy.burst.provider.TextureProvider;
import org.mineacademy.burst.provider.UUIDNameProvider;

@Module
public class ProxyModule {
  @Provides
  public ProxyServer proxyServer() {return ProxyServer.getInstance();}

  @Provides
  public List<ProxiedPlayer> onlinePlayers() {
    return new ArrayList<>(proxyServer().getPlayers());
  }

  @Provides
  public UUIDNameProvider uuidNameProvider(){
    return Providers.getUuidNameProvider();
  }

  @Provides
  public TextureProvider textureProvider() {
    return Providers.getTextureProvider();
  }
}

