package org.mineacademy.punishcontrol.core.handlers;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.handler.AbstractPunishHandler;
import org.mineacademy.punishcontrol.core.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import java.util.Optional;

public abstract class AbstractJoinHandler<T> extends AbstractPunishHandler<T> {
  protected final SettingsProvider settingsProvider;

  protected AbstractJoinHandler(
      @NonNull final StorageProvider storageProvider,
      @NonNull final SettingsProvider settingsProvider) {
    super(storageProvider, settingsProvider);
    this.settingsProvider = settingsProvider;
  }

  @Override protected final void handleInput() {
    if (!storageProvider.isBanned(target())) {
      return;
    }

    cancel();

    final Optional<Ban> optionalBan = storageProvider.currentBan(target());
    if (!optionalBan.isPresent()) {
      return;
    }

    final Ban ban = optionalBan.get();

    /*
     * You have been banned
     */
    cancelReason(settingsProvider.getJoinMessageForBannedPlayer(ban));
  }
}
