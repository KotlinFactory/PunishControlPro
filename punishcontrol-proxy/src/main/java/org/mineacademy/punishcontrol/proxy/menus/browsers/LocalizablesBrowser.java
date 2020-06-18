package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.menu.AbstractSearchableBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.localization.Localizable;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;

public final class LocalizablesBrowser extends AbstractSearchableBrowser<Localizable> {

  private final SettingsBrowser browser;

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(
        () -> DaggerProxyComponent.create().localizableBrowser().displayTo(player)
    );
  }

  @Inject
  public LocalizablesBrowser(
      @NonNull final SettingsBrowser parent,
      @NonNull @Named("localizables") final Collection<Localizable> content) {
    super("LocalizableBrowser", parent, content);
    this.browser = parent;
  }

  @Override
  public void redisplay(final Collection<Localizable> content) {
    if (getPlayer() == null)
      return;

    async(() -> new LocalizablesBrowser(browser, content).displayTo(getPlayer()));
  }

  @Override
  public Collection<Localizable> searchByPartialString(final String partial) {
    return null;
  }

  @Override
  protected void onClick(final ClickType clickType, final Localizable localizable) {
    
  }

  @Override
  protected ItemStack convertToItemStack(final Localizable localizable) {
    return null;
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }
}
