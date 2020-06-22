package org.mineacademy.punishcontrol.core.listeners;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.settings.Settings.Punish;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class MuteListener implements Listener<ChatEvent> {

  private final StorageProvider storageProvider;
  private final PlayerProvider playerProvider;

  @Inject
  public MuteListener(
      final StorageProvider storageProvider,
      final PlayerProvider playerProvider) {
    this.storageProvider = storageProvider;
    this.playerProvider = playerProvider;
  }

  @Override
  public Class<ChatEvent> getClazz() {
    return ChatEvent.class;
  }

  @Override
  public void handleEvent(final ChatEvent event) {
    if (!Punish.Mute.ENABLED) {
      return;
    }

    final UUID target = event.targetUUID();
    final Optional<Mute> optionalMute = storageProvider.currentMute(target);
    optionalMute.ifPresent((
        mute -> {
          //TODO format
          if (Punish.Mute.DISABLED_COMMANDS.contains("*")) {
            return;
          }

          //Is a allowed commands
          if (canByPass(event.message())) {
            return;
          }

          event.canceled(true);
          event.cancelReason(Punishes.formPunishedMessage(mute));
          playerProvider.sendIfOnline(
              event.targetUUID(), Punishes.formPunishedMessage(mute)
          );
        }));
  }

  private boolean canByPass(final String message) {
    //Handling commands
    if (!message.startsWith("/")) {
      return false;
    }
    if (Punish.Mute.DISABLED_COMMANDS.contains("*")) {
      return false;
    }

    for (final String disallowed : Settings.Punish.Mute.DISABLED_COMMANDS) {
      if (message.startsWith(disallowed)) {
        return false;
      }
    }
    return true;
  }
}
