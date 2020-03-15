package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.command.SimpleCommand;

public final class CommandKick extends SimpleCommand {

  private CommandKick() {
    super("kick");
  }

  public static CommandKick newInstance() {
    return new CommandKick();
  }

  @Override
  protected void onCommand() {}
}
