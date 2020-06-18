package org.mineacademy.punishcontrol.spigot.listeners;

import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.settings.Settings.Advanced;
import org.mineacademy.punishcontrol.spigot.Scheduler;

public final class SpigotDataSetter implements Listener<JoinEvent> {

  private final TextureProvider textureProvider;
  private final PlayerProvider playerProvider;

  @Inject
  public SpigotDataSetter(
      @NonNull final TextureProvider textureProvider,
      @NonNull final PlayerProvider playerProvider) {
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

    final String ip = event.targetAddress() == null
        ? "unknown"
        : event.targetAddress().getHostAddress();
    Scheduler.runAsync(
        () -> {
          playerProvider.saveData(uuid, name, ip);
          if (Advanced.ONLINE_MODE) {
            textureProvider.saveSkinTexture(uuid);
          }
        });
  }
}
