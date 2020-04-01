package org.mineacademy.punishcontrol.proxy.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.mineacademy.punishcontrol.core.event.Events;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.events.QuitEvent;

public class ProxyListenerImpl implements Listener {

  public static ProxyListenerImpl create() {
    return new ProxyListenerImpl();
  }

  @EventHandler
  //TODO: TEST! / UNSAFE!
  public void login(final PreLoginEvent playerPreLoginEvent) {
    final JoinEvent joinEvent = Events.call(
        JoinEvent.create(playerPreLoginEvent.getConnection().getUniqueId(),
            playerPreLoginEvent.getConnection().getAddress().getAddress()));

    if (!joinEvent.canceled()) {
      return;
    }
    playerPreLoginEvent.setCancelled(true);
    playerPreLoginEvent.setCancelReason(joinEvent.cancelReason());
  }

  @EventHandler
  public void chat(final net.md_5.bungee.api.event.ChatEvent playerChatEvent) {
    final ProxiedPlayer sender = (ProxiedPlayer) playerChatEvent.getSender();
    final ChatEvent chatEvent = Events.call(ChatEvent
        .create(sender.getUniqueId(), playerChatEvent.getMessage()));

    playerChatEvent.setCancelled(chatEvent.canceled());
    playerChatEvent.setMessage(chatEvent.getMessage());
  }

  @EventHandler
  public void quit(final PlayerDisconnectEvent playerDisconnectEvent) {
    final QuitEvent quitEvent = Events
        .call(QuitEvent.create(playerDisconnectEvent.getPlayer().getUniqueId()));
  }
}
