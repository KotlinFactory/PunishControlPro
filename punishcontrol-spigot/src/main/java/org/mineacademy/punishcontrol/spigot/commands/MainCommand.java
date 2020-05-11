package org.mineacademy.punishcontrol.spigot.commands;

import java.util.List;
import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

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
    tell("&8" + Common.chatLineSmooth());
    tell("&7"+ SimplePlugin.getNamed() + " v." + SimplePlugin.getVersion());
    tell("&7© MineAcademy 2020");
    tell(" ");
    for (final SimpleCommand command : SimpleCommand.getRegisteredCommands()) {
      tell("&e/" + command.getLabel() + " &8* &7" + command.getDescription());
    }
    tell("&8" + Common.chatLineSmooth());
  }
}
