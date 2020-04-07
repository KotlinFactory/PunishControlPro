package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.settings.SimpleLocalization.Player;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;

public final class CommandKick extends AbstractSimplePunishControlCommand {

  @Inject
  public CommandKick(final PlayerProvider playerProvider) {
    super(playerProvider, "kick");
    setUsage("kick <player>");
  }

  @Override
  protected void onCommand() {
    if(args.length == 1 && "?".equalsIgnoreCase(args[0])){
      returnTell("Usage /kick <player>");
    }

    if (args.length < 1) {
      returnInvalidArgs();
    }
    findPlayer(args[0], Player.NOT_ONLINE);

    final StringBuilder reasonBuilder = new StringBuilder();

    for (int i = 0; i < args.length; i++) {
      if (i == 0) {
        continue;
      }
      reasonBuilder.append(args[i]);
    }

    Debugger.debug("Kick",
        "Kicking '" + args[0] + "' for '" + reasonBuilder.toString() + "'");
  }
}
