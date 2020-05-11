package org.mineacademy.punishcontrol.proxy.commands;

import javax.inject.Inject;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.proxy.command.AbstractSimplePunishControlCommand;

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

    final ProxiedPlayer target = findPlayer(args[0], Localization.Player.NOT_ONLINE);

    final StringBuilder reasonBuilder = new StringBuilder();

    for (int i = 0; i < args.length; i++) {
      if (i == 0) {
        continue;
      }
      reasonBuilder.append(args[i]);
    }

    target.disconnect(Punishes.formKickedMessage(reasonBuilder.toString()));

    tell("&aSuccessfully&7 kicked {target}"
        .replace("{target}", target.getName()));

    Debugger.debug("Kick",
        "Kicking '" + args[0] + "' for '" + reasonBuilder.toString() + "'");
  }
}
