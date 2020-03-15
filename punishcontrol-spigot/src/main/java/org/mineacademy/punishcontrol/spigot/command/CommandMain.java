package org.mineacademy.punishcontrol.spigot.command;

import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.spigot.gui.MenuMain;

public final class CommandMain extends SimpleCommand {

  private CommandMain(@NonNull final StrictList<String> labels) {
    super(labels);
  }

  public static CommandMain newInstance(@NonNull final StrictList<String> labels) {
    return new CommandMain(labels);
  }

  @Override
  protected void onCommand() {
    checkConsole();

    if (args.length != 0) {
      doHelp();
      return;
    }

    // Open GUI
    MenuMain.showTo(getPlayer());
  }

  private void doHelp() {

    tell("");

    for (final AbstractPunishCommand registeredCommand :
        AbstractPunishCommand.REGISTERED_COMMANDS) {}

    for (final AbstractUnPunishCommand registeredCommand :
        AbstractUnPunishCommand.REGISTERED_COMMANDS) {}
  }
}
