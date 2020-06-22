package org.mineacademy.punishcontrol.proxy;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.inject.Named;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.ReflectionUtil;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.provider.Providers;
import org.mineacademy.burst.provider.TextureProvider;
import org.mineacademy.burst.provider.UUIDNameProvider;
import org.mineacademy.punishcontrol.proxy.menus.settings.SettingTypes;

@Module
public final class ProxyModule {

  private List<Class<?>> classes;

  @Provides
  public ProxyServer proxyServer() {
    return ProxyServer.getInstance();
  }

  @Provides
  public List<ProxiedPlayer> onlinePlayers() {
    return new ArrayList<>(proxyServer().getPlayers());
  }

  @Provides
  public UUIDNameProvider uuidNameProvider() {
    return Providers.getUuidNameProvider();
  }

  @Provides
  public TextureProvider textureProvider() {
    return Providers.getTextureProvider();
  }

  @Provides
  public SimplePlugin simplePlugin() {
    return SimplePlugin.getInstance();
  }

  /**
   * List's the classes used in our plugin. Heavyweight operation! --> Use async only!
   */
  @Provides
  public List<Class<?>> classes() {
    if (classes != null) {
      return classes;
    }

    return classes = new ArrayList<>(ReflectionUtil.getClasses(simplePlugin()));
  }

  @Provides
  @Named("settings")
  public Collection<SettingTypes> settings() {
    return Arrays.asList(SettingTypes.values());
  }
}

