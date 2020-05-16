package org.mineacademy.punishcontrol.core.listeners;

import java.net.InetAddress;
import java.util.Optional;
import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public class BanIpListener implements Listener<JoinEvent> {

  private final StorageProvider storageProvider;

  @Inject
  public BanIpListener(final StorageProvider storageProvider) {
    this.storageProvider = storageProvider;
  }

  @Override
  public Class<JoinEvent> getClazz() {
    return JoinEvent.class;
  }

  @Override
  public void handleEvent(final JoinEvent event) {
    if (!Settings.Punish.Ban.APPLY_ON_IP) {
      return;
    }

    final InetAddress targetAddress = event.targetAddress();
    final Optional<Ban> optionalBan = storageProvider
        .currentBan(targetAddress);

    optionalBan.ifPresent((ban -> {
      //TODO format
      event.canceled(true);
      event.cancelReason(Punishes.formOnPunishMessage(ban));
    }));
  }

}
