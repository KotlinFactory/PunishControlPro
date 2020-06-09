package org.mineacademy.punishcontrol.spigot.commands;

import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleComponent;
import org.mineacademy.punishcontrol.core.Searcher.SearchResult;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public final class SearchCommand
    extends AbstractSimplePunishControlCommand
    implements Schedulable {

  private final StorageProvider storageProvider;

  @Inject
  public SearchCommand(
      @NonNull final StorageProvider storageProvider,
      @NonNull final PlayerProvider playerProvider) {
    super(playerProvider, "search");
    this.storageProvider = storageProvider;
    setPermission("punishcontrol.command.search");
    setUsage("[?]");
  }

  @Override
  protected void onCommand() {
    if (args.length != 2) {
      tell(getMultilineUsageMessage());
      return;
    }

    final String data = args[1];

    if ("ip".equalsIgnoreCase(args[0])) {
      final UUID target = findTarget(data);
      tell("&7Ip of " + data + " &6is " + playerProvider.ip(target).orElse("unknown"));

      return;
    }

    if ("name".equalsIgnoreCase(args[0])) {
      try {
        final String name =
            playerProvider.findName(UUID.fromString(data)).orElse("unknown");
        SimpleComponent
            .of("&7Name: &6")
            .append(name)
            .onHover("&7Click to copy")
            .onClickSuggestCmd(name)
            .send(getSender());
      } catch (final Throwable throwable) {
        tell("&cInvalid UUID: " + data);
      }
      return;
    }

    if ("uuid".equalsIgnoreCase(args[0])) {
      final UUID result = findTarget(data);
      SimpleComponent
          .of("&7UUID: &6")
          .append(result.toString())
          .onHover("&7Click to copy")
          .onClickSuggestCmd(result.toString())
          .send(getSender());
      return;
    }

    // More heavy-weight operations
    async(() -> {
      if ("player".equalsIgnoreCase(args[0])) {
        //Ip
        if (isValidInet4Address(data)) {

          tell("&7" + Common.chatLineSmooth());
          tell("&7Players with ip: &6" + data);

          for (final UUID uuid : playerProvider.searchForUUIDsOfIp(data)) {
            final String banned = storageProvider.isBanned(uuid)
                ? "&cbanned"
                : "&7not banned";

            final String name = playerProvider.findName(uuid).orElse("unknown");
            SimpleComponent
                .of("&8" + name + "&7")
                .onHover("&7Click to copy")
                .onClickSuggestCmd(data)
                .append(" ")
                .append("&7[" + banned + "&7]")
                .append("&7[&6action&7]")
                .onHover("&7Click to choose an action")
                .onClickRunCmd("/chooseaction " + name)
                .send(getSender());
          }

          tell("&7" + Common.chatLineSmooth());
          // partial name
        } else {
          tell("&7" + Common.chatLineSmooth());
          tell("&7Similar names");
          for (final SearchResult result : playerProvider.search(data)) {
            SimpleComponent
                .of("&7[&8" + Math.round(result.similarity() * 100.0) / 100.0 + "&7]")
                .onHover("&7The higher the number the more similar the name")
                .append(" ")
                .append("&6" + result.result())
                .onHover("&7Click to copy")
                .onClickSuggestCmd(result.result())
                .append(" ")
                .append("&7[&6action&7]")
                .onHover("&7Click to choose an action")
                .onClickRunCmd("/chooseaction " + result.result())
                .send(getSender());
          }
          tell("&7" + Common.chatLineSmooth());
        }
      } else if ("alts".equalsIgnoreCase(args[0])) {
        tell("&7" + Common.chatLineSmooth());

        final UUID target = findTarget(data);

        final String ip = playerProvider.ip(target).orElse("unknown");

        tell("&7Players with ip: &6" + ip);

        for (final UUID uuid : playerProvider.searchForUUIDsOfIp(ip)) {

          final String banned = storageProvider.isBanned(uuid)
              ? "&cbanned"
              : "&7not banned";

          final String name = playerProvider.findName(uuid).orElse("unknown");
          SimpleComponent
              .of("&8" + name + "&7")
              .onHover("&7Click to copy")
              .onClickSuggestCmd(data)
              .append(" ")
              .append("&7[" + banned + "&7]")
              .append(" ")
              .append("&7[&6action&7]")
              .onHover("&7Click to choose an action")
              .onClickRunCmd("/chooseaction " + name)
              .send(getSender());
        }
      }
    });
  }

  @Override
  protected String[] getMultilineUsageMessage() {
    return new String[]{
        "&8" + Common.chatLineSmooth(),
        "&eSearch-Command",
        "&7/" + getLabel() + " uuid <player>  &8*&7 get a player's UUID",
        "&7/" + getLabel() + " name <uuid>  &8*&7 get a player's name from UUID",
        "&7/" + getLabel() + " ip <player>  &8*&7 get a player's IP",
        "&7/" + getLabel() + " player <partial-name> &8* &7search for an "
            + "player by its partial name",
        "&7" + getLabel() + " player <ip> &8*&7 search for players by ip",
        "&7/" + getLabel() + " alts <player> &8*&7 search for alts of a player",
        "&8" + Common.chatLineSmooth()
    };
  }

  private static boolean isValidInet4Address(@NonNull final String ip) {
    return ip.contains(".");
  }
}