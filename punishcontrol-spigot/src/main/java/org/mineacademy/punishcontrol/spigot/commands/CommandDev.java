package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.fo.command.SimpleCommand;


/**
 * Command to test stuff
 */
public final class CommandDev extends SimpleCommand {


  protected CommandDev() {
    super("dev");
  }

  public static CommandDev create() {
    return new CommandDev();
  }

  /**
   * Executed when the command is run. You can get the variables sender and args
   * directly, and use convenience checks in the simple command class.
   */
  @Override
  protected void onCommand() {

  }
}
