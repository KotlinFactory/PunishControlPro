package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.fo.command.SimpleCommand;

public final class CommandKick extends SimpleCommand {

  private CommandKick(final String label) {
    super(label);
  }

  public static CommandKick newInstance() {
    return new CommandKick("kick");
  }

  @Override
  protected void onCommand() {

  }
}
