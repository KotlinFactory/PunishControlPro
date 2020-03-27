package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.punishcontrol.spigot.command.AbstractPunishCommand;

public final class CommandWarn extends AbstractPunishCommand {

  private CommandWarn() {
    super("warn");
    setDescription("Warn a player using a sleek gui.");
  }

  public static CommandWarn newInstance() {
    return new CommandWarn();
  }
}
