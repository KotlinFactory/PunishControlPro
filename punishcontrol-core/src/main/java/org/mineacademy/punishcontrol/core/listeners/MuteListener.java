package org.mineacademy.punishcontrol.core.listeners;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class MuteListener implements Listener<ChatEvent> {

  private final StorageProvider storageProvider;
  private final SettingsProvider settingsProvider;
  private final PlayerProvider playerProvider;

  @Inject
  public MuteListener(
      final StorageProvider storageProvider,
      final PlayerProvider playerProvider,
      final SettingsProvider settingsProvider) {
    this.storageProvider = storageProvider;
    this.playerProvider = playerProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public Class<ChatEvent> getClazz() {
    return ChatEvent.class;
  }

  @Override
  public void handleEvent(final ChatEvent event) {
    final UUID target = event.targetUUID();
    final Optional<Mute> optionalMute = storageProvider.currentMute(target);

    optionalMute.ifPresent((mute -> {
      //TODO format
      if(settingsProvider.allowedCommands().contains("*")) {
        return;
      }
      if (event.message().startsWith("/")) {
        for (final String allowed : settingsProvider.allowedCommands()) {
          if (event.message().startsWith(allowed)) {
            return;
          }
        }
      }
      event.canceled(true);
      event.cancelReason(Punishes.formPunishedMessage(mute));
      playerProvider.sendIfOnline(
          event.targetUUID(), Punishes.formPunishedMessage(mute)
      );
    }));
  }
}
