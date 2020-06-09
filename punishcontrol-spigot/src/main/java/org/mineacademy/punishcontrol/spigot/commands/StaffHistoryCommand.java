package org.mineacademy.punishcontrol.spigot.commands;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.browsers.StaffPunishesBrowser;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public final class StaffHistoryCommand
    extends AbstractSimplePunishControlCommand
    implements Schedulable {

  private final TextureProvider textureProvider;
  private final MainMenu mainMenu;

  @Inject
  public StaffHistoryCommand(
      @NonNull final PlayerProvider playerProvider,
      @NonNull final TextureProvider textureProvider,
      @NonNull MainMenu mainMenu) {
    super(playerProvider, new StrictList<>("staffhistory", "staffhis", "sth"));
    this.textureProvider = textureProvider;
    this.mainMenu = mainMenu;
    setPermission("punishcontrol.command.staffhistory");
    setDescription("History of punishments.");
    setUsage("<player>");
  }

  @Override
  protected void onCommand() {
    checkConsole();

    if (args.length != 0 && args.length != 1) {
      returnTell("&cUsage: /" + getLabel() + " <player>");
    }

    if (args.length == 0) {

      async(() -> new AbstractPlayerBrowser(playerProvider, textureProvider, mainMenu) {

        @Override
        public void onClick(UUID target) {
          StaffPunishesBrowser.showTo(getPlayer(), target);
        }

        @Override
        protected List<String> lore(UUID uuid) {
          return Collections.singletonList(
              "Show punishments created by " + playerProvider.findName(uuid).orElse(
                  "unknown")
          );
        }
      }.displayTo(getPlayer()));
      return;
    }

    final UUID target = findTarget();

    StaffPunishesBrowser.showTo(getPlayer(), target);
  }
}
