package org.mineacademy.punishcontrol.proxy.commands;

import java.util.List;
import lombok.NonNull;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;

public final class CommandMain extends SimpleCommand {

  private CommandMain(@NonNull final StrictList<String> labels) {
    super(labels);
  }

  public static CommandMain create(@NonNull final StrictList<String> labels) {
    return new CommandMain(labels);
  }

  public static CommandMain create(final List<String> labels){
    return create(new StrictList<>(labels));
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
