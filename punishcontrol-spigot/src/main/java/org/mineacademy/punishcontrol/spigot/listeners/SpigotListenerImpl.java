package org.mineacademy.punishcontrol.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.punishcontrol.core.event.Events;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.events.QuitEvent;

public final class SpigotListenerImpl implements Listener {

  public static SpigotListenerImpl create() {
    return new SpigotListenerImpl();
  }

  @EventHandler
  public void login(final AsyncPlayerPreLoginEvent playerPreLoginEvent) {
    final JoinEvent joinEvent = Events.call(
        JoinEvent.create(playerPreLoginEvent.getUniqueId(), playerPreLoginEvent.getAddress()));

    if (!joinEvent.canceled()) {
      return;
    }
    playerPreLoginEvent.setLoginResult(Result.KICK_BANNED);
    playerPreLoginEvent.setKickMessage(joinEvent.cancelReason());
  }

  @EventHandler
  public void chat(final AsyncPlayerChatEvent asyncPlayerChatEvent) {
    final ChatEvent chatEvent = Events.call(ChatEvent
        .create(asyncPlayerChatEvent.getPlayer().getUniqueId(), asyncPlayerChatEvent.getMessage()));
    asyncPlayerChatEvent.setCancelled(chatEvent.canceled());
    asyncPlayerChatEvent.setMessage(chatEvent.getMessage());
  }

  @EventHandler
  public void quit(final PlayerQuitEvent playerQuitEvent) {
    final QuitEvent quitEvent = Events
        .call(QuitEvent.create(playerQuitEvent.getPlayer().getUniqueId()));

  }
}
