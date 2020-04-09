package org.mineacademy.punishcontrol.spigot.commands;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;
import org.mineacademy.punishcontrol.spigot.menus.browser.PlayerBrowser;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

public class PlayerInfoCommand extends AbstractSimplePunishControlCommand {

  private final StorageProvider storageProvider;

  @Inject
  public PlayerInfoCommand(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(playerProvider, new StrictList<>("playerinfo", "pi", "pli"));
    this.storageProvider = storageProvider;
    setMinArguments(1);
    setUsage("[player]");
  }

  @Override
  protected void onCommand() {

    if (args.length == 0) {
      if (!isPlayer()) {
        returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
      }

      PlayerBrowser.showTo(getPlayer());
      return;
    }

    if (args.length != 1) {
      returnInvalidArgs();
    }

    //Player INfo

    if ("?".equalsIgnoreCase(args[0]) || "help".equalsIgnoreCase(args[0])) {
      tell(getUsage());
    }

    final UUID target = findTarget();

    //Formatting & so

    //Async --> final variable

    Scheduler.runAsync(() -> {
      final List<Punish> punishes = storageProvider.listPunishes(target);
      //Sorting by creation

      punishes.sort((o1, o2) -> o1.creation() > o2.creation() ? 1 : -1);

      tell("&7" + Common.chatLineSmooth());
      tell("&7Data for: &6" + playerProvider.getName(target));
      tell("&7IP: " + playerProvider.getIp(target).orElse("unknown"));
      tell(" ");
      for (final Punish punish : punishes) {
        final String isActive = punish.isOld() ? "&7[&cI&7]" : "&7[&2A&7]";
        final String end =
            punish.removed() ? "&cRemoved" : Settings.Advanced.formatDate(punish.getEndTime());
        tell("&7[&8" + punish.punishType() + "&7] " + isActive + " " + punish
            .reason() + "§7┃ Creation: "
            + Settings.Advanced.formatDate(punish.creation())
            + " - End: " + end

        );
      }

      tell(" ");
      tell("&7" + Common.chatLineSmooth());
    });


    // Send info here
  }
}
