package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

import lombok.NonNull;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;

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

    checkConsole();
    // Open GUI
    //		new PunishControlGUI().displayTo(getPlayer());
  }

  private void doHelp() {
    tell("{label}", "", "");
  }
}
