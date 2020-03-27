package org.mineacademy.punishcontrol.spigot.impl.handlers;

import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.mineacademy.punishcontrol.core.handlers.AbstractJoinHandler;
import org.mineacademy.punishcontrol.core.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import javax.inject.Inject;
import java.util.List;

public final class SpigotJoinHandler extends AbstractJoinHandler<AsyncPlayerPreLoginEvent> implements Listener {

  @Inject
  public SpigotJoinHandler(
      final @NonNull StorageProvider storageProvider,
      final @NonNull SettingsProvider settingsProvider) {
    super(storageProvider, settingsProvider);
  }

  //

  @Override
  public void cancel() {
    event().setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
  }

  @Override
  public boolean canceled() {
    return event().getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED;
  }

  @Override
  public void cancelReason(final @NonNull List<String> cancelReason) {
    event().setKickMessage(String.join("\n", cancelReason));
  }

  @Override
  public String cancelReason() {
    return event().getKickMessage();
  }

  @EventHandler
  @Override
  public void onEvent(final AsyncPlayerPreLoginEvent event) {
    event(event);
    target(event.getUniqueId());
  }
}
