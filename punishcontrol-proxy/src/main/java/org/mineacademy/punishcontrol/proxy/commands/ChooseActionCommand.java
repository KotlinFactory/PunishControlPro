package org.mineacademy.punishcontrol.proxy.commands;

import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.proxy.command.AbstractSimplePunishControlCommand;
import org.mineacademy.punishcontrol.proxy.menus.ChooseActionMenu;

public final class ChooseActionCommand extends AbstractSimplePunishControlCommand {

  @Inject
  public ChooseActionCommand(
      @NonNull final PlayerProvider playerProvider) {
    super(playerProvider, new StrictList<>("chooseaction", "action", "acttion"));
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
