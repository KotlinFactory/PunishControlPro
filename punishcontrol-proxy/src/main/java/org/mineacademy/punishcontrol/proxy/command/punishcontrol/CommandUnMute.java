package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

public final class CommandUnMute extends AbstractPunishCommand {

  private CommandUnMute() {
    super("unmute");
  }

  public static CommandUnMute newInstance() {
    return new CommandUnMute();
  }
}
