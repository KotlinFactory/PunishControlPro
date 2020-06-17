package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPunishBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

/**
 * Browser to browse punishments created by a given staff member
 */
public final class StaffPunishesBrowser extends AbstractPunishBrowser {

  private static final String SHOW_PUNISHMENTS_CREATED_BY = "Show punishments created by {target}";
  private static final String PUNISHMENTS_BY_TARGET = "Punishments by {target}";
  private final UUID staffMember;

  public static void showTo(
      @NonNull final Player player,
      @NonNull final UUID staffMember) {
    Scheduler.runAsync(() -> {
      Builder builder =
          DaggerSpigotComponent.create().staffPunishesBrowserBuilder();
      val browser = builder.build(staffMember);
      browser.displayTo(player);
    });
  }


  public StaffPunishesBrowser(
      @NonNull final MainMenu parent,
      @NonNull final PlayerProvider playerProvider,
      @NonNull final TextureProvider textureProvider,
      @NonNull final StorageProvider storageProvider,
      @NonNull final UUID staffMember /*dynamic */) {
    super(
        new AbstractPlayerBrowser(
            playerProvider,
            textureProvider,
            parent) {

          @Override
          public void onClick(UUID target) {
            StaffPunishesBrowser.showTo(getViewer(), target);
          }

          @Override
          protected List<String> lore(UUID uuid) {
            return Collections.singletonList(
                SHOW_PUNISHMENTS_CREATED_BY
                    .replace("{target}", playerProvider.findName(uuid).orElse("unknown"))
            );
          }
        },
        playerProvider,
        storageProvider.listCurrentPunishes()
            .stream()
            .filter(punish -> punish.creator().equals(staffMember))
            .collect(Collectors.toList()));

    this.staffMember = staffMember;

    setTitle("&8"+ PUNISHMENTS_BY_TARGET
        .replace("{target}", playerProvider.findName(staffMember).orElse("unknown")));

//   .displayTo(getPlayer());
//    registerActionHandler("Back", (back -> {

//      return CallResult.DENY_GRABBING;
//    }));
  }

  @Override
  protected void redrawForPlayer(Player player) {
    showTo(getViewer(), staffMember);

  }

  // ----------------------------------------------------------------------------------------------------
  // Builder to inject dynamic members
  // ----------------------------------------------------------------------------------------------------

  public static final class Builder {

    private final MainMenu parent;
    private final TextureProvider textureProvider;
    private final PlayerProvider playerProvider;
    private final StorageProvider storageProvider;

    @Inject
    public Builder(
        @NonNull final MainMenu parent,
        @NonNull final PlayerProvider playerProvider,
        @NonNull final TextureProvider textureProvider,
        @NonNull final StorageProvider storageProvider) {
      this.parent = parent;

      this.textureProvider = textureProvider;
      this.playerProvider = playerProvider;
      this.storageProvider = storageProvider;
    }

    public StaffPunishesBrowser build(final UUID staffMember) {
      return new StaffPunishesBrowser(
          parent,
          playerProvider,
          textureProvider,
          storageProvider,
          staffMember);
    }
  }
}