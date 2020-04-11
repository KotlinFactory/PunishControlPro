package org.mineacademy.punishcontrol.spigot.listeners;

import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
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
    final String name = playerProvider.getName(uuid);

    System.out.println("Saved textures");
//    final String name = playerProvider.getName(uuid);
    final String ip = event.targetInetAddress() == null ? "unknown" : event.targetInetAddress().getHostName();
    Scheduler.runAsync(
        () -> {
          playerProvider.saveData(uuid, name, ip);
          textureProvider.saveSkinTexture(uuid);
        });
  }
}
