package org.mineacademy.punishcontrol.core.listeners;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class BanListener implements Listener<JoinEvent> {

  private final StorageProvider storageProvider;

  @Inject
  public BanListener(final StorageProvider storageProvider) {
    this.storageProvider = storageProvider;
  }

  @Override
  public Class<JoinEvent> getClazz() {
    return JoinEvent.class;
  }

  @Override
  public void handleEvent(final JoinEvent event) {
    final UUID target = event.targetUUID();
    final Optional<Ban> optionalBan = storageProvider.currentBan(target);

    final String reason = optionalBan.map(Punish::reason).orElseGet(
        () -> {
          if (!Settings.Punish.Ban.applyOnIp) {
            return null;
          }

          if (!storageProvider.isBanned(event.targetInetAddress())) {
            return null;
          }

          return "&cBanned with other ip";
        });

    if (reason != null) {
      event.canceled(true);
      //TODO format
      event.cancelReason(reason);
    }
  }
}
