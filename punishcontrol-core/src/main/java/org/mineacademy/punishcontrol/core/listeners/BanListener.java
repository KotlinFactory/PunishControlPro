package org.mineacademy.punishcontrol.core.listeners;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.punishes.Ban;
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
    if (!storageProvider.isBanned(target)) {
      return;
    }

    event.canceled(true);

    final Optional<Ban> optionalBan = storageProvider.currentBan(target);
    if (!optionalBan.isPresent()) {
      return;
    }

    final Ban ban = optionalBan.get();

    /*
     * You have been banned
     */
    //TODO
//    event.cancelReason(settingsProvider.getJoinMessageForBannedPlayer(ban));
  }
}
