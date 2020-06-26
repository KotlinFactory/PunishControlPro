package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Arrays;
import java.util.Collections;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

@Localizable
public abstract class AbstractPunishTypeBrowser
    extends AbstractBrowser<PunishType> {

  @Localizable("Parts.Select")
  private static String SELECT = "Select";
  @Localizable("Menu.Proxy.PunishTypes.ChooseType_Lore")
  private static String CHOOSE_TYPE = "Choose type";

  protected AbstractPunishTypeBrowser(final Menu parent) {
    super(parent, Arrays.asList(PunishType.values()));
    setTitle("&8" + CHOOSE_TYPE);
  }

  @Override
  protected ItemStack convertToItemStack(final PunishType item) {
    return ItemCreator
        .of(ItemStacks.forPunishType(item)).name(item.localized())
        .lores(Collections.singletonList("&7" + SELECT + " " + item.localized()))
        .build()
        .makeMenuTool();
  }

  @Override
  protected void onPageClick(
      final Player player,
      final PunishType punishType,
      final ClickType click) {
    onClick(punishType);
  }

  protected abstract void onClick(final PunishType punishType);

}
