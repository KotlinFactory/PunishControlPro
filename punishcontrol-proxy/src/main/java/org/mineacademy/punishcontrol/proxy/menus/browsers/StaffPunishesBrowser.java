package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishBrowser;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

/**
 * Browser to browse punishments created by a given staff member
 */
@Localizable
public final class StaffPunishesBrowser extends AbstractPunishBrowser {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls @Localizable("Menu.Proxy.StaffPunishesBrowser.Show_Punishments_By")
  private static final String SHOW_PUNISHMENTS_CREATED_BY = "Show punishments created by";

  // ----------------------------------------------------------------------------------------------------
  // Displaying
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final UUID staffMember) {
    Scheduler.runAsync(() -> {
      final StaffPunishesBrowser.Builder builder =
          DaggerProxyComponent.create().staffPunishesBrowserBuilder();
      val browser = builder.build(staffMember);
      browser.displayTo(player);
    });
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructor's
  // ----------------------------------------------------------------------------------------------------

  private final UUID staffMember;

  public StaffPunishesBrowser(
      @NonNull final MainMenu parent,
      @NonNull final PlayerProvider playerProvider,
      @NonNull final TextureProvider textureProvider,
      @NonNull final StorageProvider storageProvider,
      @NonNull final UUID staffMember /*dynamic */) {
    super(
        "StaffPunishesBrowser",
        parent,
        playerProvider,
        storageProvider.listCurrentPunishes()
            .stream()
            .filter(punish -> punish.creator().equals(staffMember))
            .collect(Collectors.toList()));

    this.staffMember = staffMember;

    setTitle("&8Punishments by " + playerProvider.findName(staffMember).orElse(
        "unknown"));

    registerActionHandler("Back", (
        back -> {
          new AbstractPlayerBrowser(
              playerProvider,
              textureProvider,
              this) {
            @Override
            protected void onClick(final ClickType clickType, final UUID target) {
              StaffPunishesBrowser.showTo(getPlayer(), target);
            }

            @Override
            protected List<String> lore(final UUID uuid) {
              return Collections.singletonList(
                  SHOW_PUNISHMENTS_CREATED_BY + " " + playerProvider
                      .findName(uuid)
                      .orElse(
                          "unknown")
              );
            }
          }.displayTo(getPlayer());
          return CallResult.DENY_GRABBING;
        }));
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), staffMember);
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