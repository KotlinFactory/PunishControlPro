package org.mineacademy.punishcontrol.proxy.commands;

import org.mineacademy.punishcontrol.proxy.command.AbstractPunishCommand;

public final class CommandWarn extends AbstractPunishCommand {

  private CommandWarn() {
    super("warn");
    setDescription("Warn a player using a sleek gui.");
  }

  public static CommandWarn newInstance() {
    return new CommandWarn();
  }
}
