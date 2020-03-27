package org.mineacademy.punishcontrol.proxy.commands;

import org.mineacademy.punishcontrol.proxy.command.AbstractPunishCommand;

public final class CommandUnMute extends AbstractPunishCommand {

  private CommandUnMute() {
    super("unmute");
  }

  public static CommandUnMute newInstance() {
    return new CommandUnMute();
  }
}
