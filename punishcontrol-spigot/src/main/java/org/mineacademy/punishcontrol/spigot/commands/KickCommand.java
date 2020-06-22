package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import org.bukkit.entity.Player;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;

public final class KickCommand extends AbstractSimplePunishControlCommand {

  @Inject
  public KickCommand(final PlayerProvider playerProvider) {
    super(playerProvider, "kick");
    setUsage("kick <player>");
    setDescription("Kick a player");
    setPermission("punishcontrol.command.kick");
  }

  @Override
  protected void onCommand() {
    if (args.length == 1 && "?".equalsIgnoreCase(args[0])) {
      returnTell("Usage /<player>");
    }

    if (args.length < 1) {
      returnInvalidArgs();
    }

    final Player target = findPlayer(args[0], Localization.Player.NOT_ONLINE);

    final StringBuilder reasonBuilder = new StringBuilder();

    for (int i = 0; i < args.length; i++) {
      if (i == 0) {
        continue;
      }
      reasonBuilder.append(args[i]);
    }

    target.kickPlayer(reasonBuilder.toString());

    tell("&aSuccessfully&7 kicked {target}"
        .replace("{target}", target.getName()));

    Debugger.debug(
        "Kick",
        "Kicking '" + args[0] + "' for '" + reasonBuilder.toString() + "'");
  }
}
