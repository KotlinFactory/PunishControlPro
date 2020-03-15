package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

public final class CommandUnBan extends AbstractPunishCommand {

  private CommandUnBan() {
    super("unban");
    setUsage("[player]");
    setDescription("Unban a player using a sleek gui");
  }

  public static CommandUnBan newInstance() {
    return new CommandUnBan();
  }
}
