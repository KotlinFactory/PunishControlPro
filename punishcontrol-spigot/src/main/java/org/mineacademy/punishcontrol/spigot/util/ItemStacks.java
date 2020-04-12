package org.mineacademy.punishcontrol.spigot.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.DyeColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.punish.PunishType;


@UtilityClass
public class ItemStacks {

  public ItemStack glow(final ItemCreator creator) {
    final val item = creator.make();
    try {
      final ItemMeta meta = item.getItemMeta();
      if (meta.hasEnchant(Enchantment.WATER_WORKER)) {
        meta.removeEnchant(Enchantment.WATER_WORKER);
        meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
      } else {
        meta.addEnchant(Enchantment.WATER_WORKER, 70, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      }
      item.setItemMeta(meta);

      //Old mc?
    } catch (final Throwable throwable) {
      System.err.println("Exception!");
    }

    return item;
  }

  public ItemStack yellowDye() {
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(
          CompMaterial.INK_SAC.getMaterial(), 1,
          DyeColor.YELLOW.getDyeData());
    }
    return CompMaterial.YELLOW_DYE.toItem();
  }

  public static ItemStack redPane(){
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(
          CompMaterial.RED_STAINED_GLASS_PANE.getMaterial(), 1,
          DyeColor.RED.getDyeData());
    }
    return CompMaterial.RED_STAINED_GLASS_PANE.toItem();
  }

  public static ItemStack greenPane(){
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(
          CompMaterial.GREEN_STAINED_GLASS_PANE.getMaterial(), 1,
          DyeColor.GREEN.getDyeData());
    }
    return CompMaterial.GREEN_STAINED_GLASS_PANE.toItem();
  }

  public ItemStack cyanDye() {
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(
          CompMaterial.INK_SAC.getMaterial(), 1,
          DyeColor.CYAN.getDyeData());
    }
    return CompMaterial.CYAN_DYE.toItem();

  }

  public ItemStack cyanGlassPane() {
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(
          CompMaterial.ORANGE_STAINED_GLASS.getMaterial(), 1,
          DyeColor.CYAN.getDyeData());
    }
    return CompMaterial.CYAN_STAINED_GLASS_PANE.toItem();
  }


  public ItemStack forPunishType(@NonNull final PunishType punishType) {
    switch (punishType) {
      case BAN:
        return ItemCreator
            .of(new ItemStack(CompMaterial.OAK_DOOR.getMaterial()))
            .build()
            .makeMenuTool();
      case MUTE:
        return ItemCreator
            .of(new ItemStack(CompMaterial.PAPER.getMaterial()))
            .build()
            .makeMenuTool();
      case WARN:
        return ItemCreator
            .of(yellowDye())
            .build()
            .makeMenuTool();
    }

    throw new IllegalStateException(
        "Invalid punishtype: '" + punishType.getClass().getSimpleName() + "'"
    );
  }
}
