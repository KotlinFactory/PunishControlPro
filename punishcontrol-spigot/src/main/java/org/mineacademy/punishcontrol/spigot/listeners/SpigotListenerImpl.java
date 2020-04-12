package org.mineacademy.punishcontrol.spigot.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.punishcontrol.core.event.Events;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.events.QuitEvent;

public final class SpigotListenerImpl implements Listener {

  //Used for best performance. We don't want to be synchronous
  private final Set<UUID> toKick = new HashSet<>();

  public static SpigotListenerImpl create() {
    return new SpigotListenerImpl();
  }

  @EventHandler
  public void login(final AsyncPlayerPreLoginEvent playerPreLoginEvent) {

    LagCatcher.start("async-join");
    final JoinEvent joinEvent = Events.call(
        JoinEvent
            .create(playerPreLoginEvent.getUniqueId(),playerPreLoginEvent.getName(), playerPreLoginEvent.getAddress()));

    if (!joinEvent.canceled()) {
      LagCatcher.end("async-join");
      return;
    }


    playerPreLoginEvent.setLoginResult(Result.KICK_BANNED);
    playerPreLoginEvent.setKickMessage(joinEvent.cancelReason());
    LagCatcher.end("async-join");
  }

  @EventHandler
  public void chat(final AsyncPlayerChatEvent asyncPlayerChatEvent) {
    LagCatcher.start("async-chat");
    try {
      final ChatEvent chatEvent = Events.call(ChatEvent
          .create(
              asyncPlayerChatEvent.getPlayer().getUniqueId(),
              asyncPlayerChatEvent.getPlayer().getAddress().getAddress(),
              asyncPlayerChatEvent.getMessage()));
      asyncPlayerChatEvent.setCancelled(chatEvent.canceled());
      asyncPlayerChatEvent.setMessage(chatEvent.message());
    } catch (final Throwable throwable) {
      Debugger.saveError(throwable, "Exception while calling chat-event!");
    }

    LagCatcher.start("async-chat");
  }

  @EventHandler
  public void chat2(final PlayerCommandPreprocessEvent playerCommandPreprocessEvent){
    try {
      LagCatcher.start("command-chat");
      final ChatEvent chatEvent = Events.call(ChatEvent
          .create(
              playerCommandPreprocessEvent.getPlayer().getUniqueId(),
              playerCommandPreprocessEvent.getPlayer().getAddress().getAddress(),
              playerCommandPreprocessEvent.getMessage()));
      playerCommandPreprocessEvent.setCancelled(chatEvent.canceled());
      playerCommandPreprocessEvent.setMessage(chatEvent.message());
      LagCatcher.start("command-chat");
    } catch (final Throwable throwable) {
      Debugger.saveError(
          throwable,
          "Exception while calling command-preprocess-event!"
      );
    }
  }

  @EventHandler
  public void quit(final PlayerQuitEvent playerQuitEvent) {
    final QuitEvent quitEvent = Events
        .call(QuitEvent.create(playerQuitEvent.getPlayer().getUniqueId()));

  }
}
