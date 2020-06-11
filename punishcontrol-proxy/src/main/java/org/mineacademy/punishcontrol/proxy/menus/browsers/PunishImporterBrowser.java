package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import de.exceptionflug.protocolize.world.Sound;
import de.exceptionflug.protocolize.world.SoundCategory;
import de.exceptionflug.protocolize.world.WorldModule;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.punish.importer.PunishImporter;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.AbstractConfirmMenu;

public final class PunishImporterBrowser extends AbstractBrowser<PunishImporter> {

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      DaggerProxyComponent.create().punishImporterBrowser().displayTo(player);
    });
  }

  @Inject
  public PunishImporterBrowser(
      @NonNull final SettingsBrowser parent,
      @NonNull @Named("importers") final Collection<PunishImporter> content) {
    super("PunishImporter", parent, content);
    setTitle("&8Import punishments");
  }

  @Override
  protected void onClick(ClickType clickType, PunishImporter punishImporter) {

    if (!punishImporter.applicable()) {
      animateTitle("&cCan't import from this importer!");
      return;
    }

    new AbstractConfirmMenu(this) {

      @Override
      public void onConfirm() {
        animateTitle("68Started importing");
        async(() -> {
          punishImporter.importAll();
          animateTitle("&aFinished importing;)");
        });
      }
    }.displayTo(getPlayer());

    later(() -> {
      setTitle("&6Imported punishments");
      build();

      WorldModule.playSound(
          getPlayer(),
          Sound.ENTITY_ARROW_HIT_PLAYER,
          SoundCategory.NEUTRAL,
          20f,
          10f);
    }, 3, TimeUnit.SECONDS);
  }

  @Override
  protected ItemStack convertToItemStack(PunishImporter punishImporter) {

    ItemType type;

    try {
      type = ItemType.valueOf(punishImporter.itemString());
    } catch (final Throwable throwable) {
      type = ItemType.STONE;
    }

    return Item
        .of(type)
        .name("&6" + punishImporter.pluginName().orElse("vanilla"))
        .lore(punishImporter.description())
        .addLore(" ")
        .addLore("&6Is applicable: " + (punishImporter.applicable() ? "&ayes" : "&cno"))
        .build();
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "Menu to import punishment"
    };
  }
}
