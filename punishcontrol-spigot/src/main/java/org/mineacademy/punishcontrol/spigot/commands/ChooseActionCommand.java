package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;
import org.mineacademy.punishcontrol.spigot.menus.ChooseActionMenu;

public final class ChooseActionCommand extends AbstractSimplePunishControlCommand {

  @Inject
  public ChooseActionCommand(
      @NonNull final PlayerProvider playerProvider) {
    super(playerProvider, new StrictList<>("chooseaction", "action", "aktion"));
    setPermission("punishcontrol.command.action");
    setDescription("Choose an action for players");
  }

  @Override
  protected void onCommand() {
    checkConsole();
    if (args.length != 1) {
      returnInvalidArgs();
    }

    ChooseActionMenu.showTo(getPlayer(), findTarget());
  }
}
