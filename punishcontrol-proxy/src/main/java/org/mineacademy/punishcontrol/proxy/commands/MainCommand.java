package org.mineacademy.punishcontrol.proxy.commands;

import java.util.List;
import lombok.NonNull;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public final class MainCommand extends SimpleCommand {

  private MainCommand(@NonNull final StrictList<String> labels) {
    super(labels);
    setDescription("Main-Command of PunishControlPro");
    setPermission("punishcontrol.command.main");
    setUsage("[?]");
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

    if(args.length == 1){
      doHelp();
      return;
    }

    if (args.length != 0) {
      returnInvalidArgs();
    }

    MainMenu.showTo(getPlayer());
  }

  private void doHelp() {
    tell(Common.chatLineSmooth());
    tell("&7"+ SimplePlugin.getNamed() + " v." + SimplePlugin.getVersion());
    tell("&7© MineAcademy 2020");
    tell(" ");
    for (final SimpleCommand command : SimpleCommand.getRegisteredCommands()) {
      tell("&7/" + command.getLabel() + " &8* &7" + command.getDescription());
    }
    tell(Common.chatLineSmooth());
  }
}
