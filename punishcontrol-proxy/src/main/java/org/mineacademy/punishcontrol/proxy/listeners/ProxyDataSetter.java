package org.mineacademy.punishcontrol.proxy.listeners;

import de.exceptionflug.mccommons.inventories.proxy.utils.Schedulable;
import java.util.UUID;
import javax.inject.Inject;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.provider.UUIDNameProvider;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.settings.Settings.Advanced;

public final class ProxyDataSetter implements Listener<JoinEvent>, Schedulable {

  private final UUIDNameProvider uuidNameProvider;
  private final TextureProvider textureProvider;
  private final PlayerProvider playerProvider;

  @Inject
  public ProxyDataSetter(
      final UUIDNameProvider uuidNameProvider,
      final TextureProvider textureProvider,
      final PlayerProvider playerProvider) {
    this.uuidNameProvider = uuidNameProvider;
    this.textureProvider = textureProvider;
    this.playerProvider = playerProvider;
  }

  @Override
  public Class<JoinEvent> getClazz() {
    return JoinEvent.class;
  }

  @Override
  public void handleEvent(final JoinEvent event) {
    final UUID uuid = event.targetUUID();
    final String name = event.name();
    final String ip = event.targetAddress().getHostAddress();
    async(() -> {
      try {
        playerProvider.saveData(uuid, name, ip);
      } catch (final Throwable throwable) {
        Debugger.saveError(throwable, "Exception while saving UUID");
      }

      try {
        if (Advanced.ONLINE_MODE)
          textureProvider.saveSkinTexture(uuid);
      } catch (final Throwable throwable) {
        Debugger.saveError(throwable, "Exception while saving Textures");
      }
    });
  }
}

