package org.mineacademy.punishcontrol.spigot.commands;

import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.spigot.menus.MenuMain;

public final class CommandMain extends SimpleCommand {

  private CommandMain(@NonNull final StrictList<String> labels) {
    super(labels);
  }

  public static CommandMain create(@NonNull final StrictList<String> labels) {
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

  }
}
