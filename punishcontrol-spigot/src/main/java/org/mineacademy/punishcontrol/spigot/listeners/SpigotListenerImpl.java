package org.mineacademy.punishcontrol.spigot.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.event.Events;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.events.JoinEvent;
import org.mineacademy.punishcontrol.core.events.QuitEvent;

public final class SpigotListenerImpl implements Listener {

  @Inject
  public SpigotListenerImpl(SimplePlugin simplePlugin) {
    addProtocollibListenerIfLoaded(simplePlugin);
  }

  public void addProtocollibListenerIfLoaded(@NonNull SimplePlugin simplePlugin) {
    if (HookManager.isProtocolLibLoaded())
      HookManager
          .addPacketListener(new PacketAdapter(simplePlugin, ListenerPriority.NORMAL,
              PacketType.Play.Client.CHAT) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
              PacketContainer packet = event.getPacket();
              if (event.isCancelled())
                return;

              try {
                ChatEvent chatEvent = Events.call(ChatEvent
                    .create(
                        event.getPlayer().getUniqueId(),
                        event.getPlayer().getAddress().getAddress(),
                        packet.getStrings().read(0)));
                event.setCancelled(chatEvent.canceled());
              } catch (Throwable throwable) {
                Debugger.saveError(
                    throwable,
                    "Exception while calling chat-event using protocollib!");
              }
            }
          });
  }

  @EventHandler
  public void login(AsyncPlayerPreLoginEvent playerPreLoginEvent) {

    LagCatcher.start("async-join");
    JoinEvent joinEvent = Events.call(
        JoinEvent
            .create(playerPreLoginEvent.getUniqueId(), playerPreLoginEvent.getName(),
                playerPreLoginEvent.getAddress()));

    if (!joinEvent.canceled()) {
      LagCatcher.end("async-join");
      return;
    }

    playerPreLoginEvent.setLoginResult(Result.KICK_BANNED);
    playerPreLoginEvent.setKickMessage(Common.colorize(joinEvent.cancelReason()));
    LagCatcher.end("async-join");
  }

  @EventHandler
  public void chat(AsyncPlayerChatEvent asyncPlayerChatEvent) {
    if (asyncPlayerChatEvent.isCancelled())
      return;
    LagCatcher.start("async-chat");
    try {
      ChatEvent chatEvent = Events.call(ChatEvent
          .create(
              asyncPlayerChatEvent.getPlayer().getUniqueId(),
              asyncPlayerChatEvent.getPlayer().getAddress().getAddress(),
              asyncPlayerChatEvent.getMessage()));
      asyncPlayerChatEvent.setCancelled(chatEvent.canceled());
      asyncPlayerChatEvent.setMessage(chatEvent.message());
    } catch (Throwable throwable) {
      Debugger.saveError(throwable, "Exception while calling chat-event!");
    }

    LagCatcher.start("async-chat");
  }

  @EventHandler
  public void chat2(PlayerCommandPreprocessEvent playerCommandPreprocessEvent) {
    if (playerCommandPreprocessEvent.isCancelled())
      return;
    try {
      LagCatcher.start("command-chat");
      ChatEvent chatEvent = Events.call(ChatEvent
          .create(
              playerCommandPreprocessEvent.getPlayer().getUniqueId(),
              playerCommandPreprocessEvent.getPlayer().getAddress().getAddress(),
              playerCommandPreprocessEvent.getMessage()));
      playerCommandPreprocessEvent.setCancelled(chatEvent.canceled());
      playerCommandPreprocessEvent.setMessage(chatEvent.message());
      LagCatcher.start("command-chat");
    } catch (Throwable throwable) {
      Debugger.saveError(
          throwable,
          "Exception while calling command-preprocess-event!"
      );
    }
  }

  @EventHandler
  public void quit(PlayerQuitEvent playerQuitEvent) {
    QuitEvent quitEvent = Events
        .call(QuitEvent.create(playerQuitEvent.getPlayer().getUniqueId()));

  }
}
