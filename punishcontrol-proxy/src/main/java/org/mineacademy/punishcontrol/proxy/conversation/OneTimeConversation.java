package org.mineacademy.punishcontrol.proxy.conversation;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class OneTimeConversation<T> {
  private final Map<UUID, T> data = new ConcurrentHashMap<>();

  public OneTimeConversation() {
    Conversations.register(this);
  }

  public abstract String prompt();

  public abstract void onInput(final String message, T data);
}
