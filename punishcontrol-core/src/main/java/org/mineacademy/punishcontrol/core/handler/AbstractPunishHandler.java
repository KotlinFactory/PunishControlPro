package org.mineacademy.punishcontrol.core.handler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

/**
 * Specific handler for punishes
 * Handles when a players joins, when a player chats, etc.
 *
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractPunishHandler<T> extends AbstractHandler<T> {
  // TODO: Use field-injection
  protected final StorageProvider storageProvider;
  protected final SettingsProvider settingsProvider;

  protected abstract void handleInput();
}
