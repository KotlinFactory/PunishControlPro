package org.mineacademy.punishcontrol.proxy.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.mineacademy.bfo.Common;
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
  public void login(final PostLoginEvent playerPostLoginEvent) {
    final JoinEvent joinEvent = Events.call(
        JoinEvent.create(
            playerPostLoginEvent.getPlayer().getUniqueId(),
            playerPostLoginEvent.getPlayer().getName(),
            playerPostLoginEvent.getPlayer().getAddress().getAddress()));

    if (!joinEvent.canceled()) {
      return;
    }

    if (joinEvent.canceled()) {
      playerPostLoginEvent.getPlayer()
          .disconnect(Common.colorize(joinEvent.cancelReason()));
    }
  }

  @EventHandler
  public void chat(final net.md_5.bungee.api.event.ChatEvent playerChatEvent) {
    final ProxiedPlayer sender = (ProxiedPlayer) playerChatEvent.getSender();
    final ChatEvent chatEvent = Events.call(
        ChatEvent.create(
            sender.getUniqueId(),
            sender.getAddress().getAddress(),
            playerChatEvent.getMessage())
    );

    playerChatEvent.setCancelled(chatEvent.canceled());
    playerChatEvent.setMessage(Common.colorize(chatEvent.message()));
  }

  @EventHandler
  public void quit(final PlayerDisconnectEvent playerDisconnectEvent) {
    final QuitEvent quitEvent =
        Events.call(
            QuitEvent.create(playerDisconnectEvent.getPlayer().getUniqueId())
        );
  }
}
