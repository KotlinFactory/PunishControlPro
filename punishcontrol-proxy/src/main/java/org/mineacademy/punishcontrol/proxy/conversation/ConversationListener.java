package org.mineacademy.punishcontrol.proxy.conversation;

import de.exceptionflug.mccommons.inventories.proxy.utils.Schedulable;
import java.util.UUID;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@SuppressWarnings("ALL")
public class ConversationListener implements Listener, Schedulable {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(final ChatEvent chatEvent) {
    if (chatEvent.isCancelled()) {
      return;
    }

    if (chatEvent.isCommand()) {
      return;
    }

    if (!(chatEvent.getSender() instanceof ProxiedPlayer)) {
      return;
    }

    final UUID uuid = ((ProxiedPlayer) chatEvent.getSender()).getUniqueId();
    final String message = chatEvent.getMessage();


    async(() -> {
      for (final OneTimeConversation conv : Conversations.conversation()) {
        if (!conv.data().containsKey(uuid)) {
          continue;
        }
        final Object data = conv.data().get(uuid);

        conv.onInput(message, data);
        conv.data().remove(uuid);
      }
    });
  }
}
