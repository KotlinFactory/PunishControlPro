package org.mineacademy.punishcontrol.core.listeners;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class MuteListener implements Listener<ChatEvent> {
  private final StorageProvider storageProvider;

  @Inject
  public MuteListener(final StorageProvider storageProvider) {
    this.storageProvider = storageProvider;
  }

  @Override
  public Class<ChatEvent> getClazz() {
    return ChatEvent.class;
  }

  @Override
  public void handleEvent(final ChatEvent event) {

  }
}
