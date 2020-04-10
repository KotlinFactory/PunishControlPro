package org.mineacademy.punishcontrol.spigot.commands;

import java.util.List;
import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public final class MainCommand extends SimpleCommand {

  private MainCommand(@NonNull final StrictList<String> labels) {
    super(labels);
  }

  public static MainCommand create(@NonNull final StrictList<String> labels) {
    return new MainCommand(labels);
  }


  public static MainCommand create(final List<String> labels) {
    return create(new StrictList<>(labels));
  }
  @Override
  protected void onCommand() {
    checkConsole();

    if (args.length != 0) {
      doHelp();
      return;
    }

    MainMenu.showTo(getPlayer());
  }

  private void doHelp() {

    tell("");

  }
}
