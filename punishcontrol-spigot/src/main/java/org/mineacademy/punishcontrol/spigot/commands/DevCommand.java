package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.fo.command.SimpleCommand;


/**
 * Command to test stuff
 */
public final class DevCommand extends SimpleCommand {


  protected DevCommand() {
    super("dev");
  }

  public static DevCommand create() {
    return new DevCommand();
  }

  /**
   * Executed when the command is run. You can get the variables sender and args
   * directly, and use convenience checks in the simple command class.
   */
  @Override
  protected void onCommand() {

  }
}
