package org.mineacademy.punishcontrol.spigot.commands;

import java.util.List;
import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.DaggerCoreComponent;
import org.mineacademy.punishcontrol.core.setting.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public final class MainCommand extends SimpleCommand {

  private MainCommand(@NonNull final StrictList<String> labels) {
    super(labels);
    setDescription("Main-Command of PunishControlPro");
    setPermission("punishcontrol.command.main");
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

    if (args.length == 1) {
      if ("reload".equalsIgnoreCase(args[0])) {
        Settings.resetSettingsCall();
        Localization.resetLocalizationCall();

        YamlStaticConfig.loadAll(Settings.class, Localization.class);
        DaggerCoreComponent.create().itemSettings().load();

        tell("&aSuccessfully reloaded &6" + SimplePlugin.getNamed());
        return;
      }
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
    tell("&7" + SimplePlugin.getNamed() + " v." + SimplePlugin.getVersion());
    tell("&7© MineAcademy 2020");
    tell(" ");
    for (final SimpleCommand command : SimpleCommand.getRegisteredCommands()) {
      tell("&e/" + command.getLabel() + " &8* &7" + command.getDescription());
    }
    tell("&8" + Common.chatLineSmooth());
  }
}
