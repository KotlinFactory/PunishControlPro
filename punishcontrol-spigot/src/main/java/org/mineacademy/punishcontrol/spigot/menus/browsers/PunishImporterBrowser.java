package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Arrays;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.punish.importer.PunishImporter;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;

public final class PunishImporterBrowser extends AbstractBrowser<PunishImporter> {

  @NonNls
  private static final String IMPORT_PUNISHMENTS = "Import punishments";
  @NonNls
  private static final String YES = "yes";
  @NonNls
  private static final String NO = "no";

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      DaggerSpigotComponent.create().punishImporterBrowser().displayTo(player);
    });
  }

  @Inject
  public PunishImporterBrowser(
      @NonNull final SettingsBrowser parent,
      @NonNull @Named("importers") final Collection<PunishImporter> content) {
    super(parent, content);
    setTitle("&8" + IMPORT_PUNISHMENTS);
  }

  @Override
  protected ItemStack convertToItemStack(PunishImporter punishImporter) {

    CompMaterial type;

    try {
      type = CompMaterial.fromString(punishImporter.itemString());
    } catch (final Throwable throwable) {
      type = CompMaterial.STONE;
    }

    return ItemCreator
        .of(type)
        .name("&6" + punishImporter.pluginName().orElse("vanilla"))
        .lores(Arrays.asList(punishImporter.description()))
        .lore(" ")
        .lore(
            "&6Is applicable: " + (punishImporter.applicable() ? "&a" + YES : "&c" + NO))
        .build()
        .make();
  }

  @Override
  protected void onPageClick(
      Player player, PunishImporter punishImporter,
      ClickType click) {
    if (!punishImporter.applicable()) {
      animateTitle("&cCan't import from this importer!");
      return;
    }

    new AbstractConfirmMenu(this) {

      @Override
      public void onConfirm() {
        animateTitle("&8Started importing");
        async(() -> {
          punishImporter.importAll();
          animateTitle("&aFinished importing;)");
        });
      }

      @Override
      protected void showParent() {
        PunishImporterBrowser.showTo(getViewer());
      }
    }.displayTo(getViewer());

    later(() -> {

      animateTitle("&6Imported punishments");
      setTitle("&6Imported punishments");

    }, 2 * 20);
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "Menu to import punishment"
    };
  }
}
