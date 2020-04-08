package org.mineacademy.punishcontrol.spigot.util;

import lombok.experimental.UtilityClass;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.remain.CompMaterial;


@UtilityClass
public class ItemStacks {

  public ItemStack yellowDye(){
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(CompMaterial.INK_SAC.getMaterial(), 1,
          DyeColor.YELLOW.getDyeData());
    }
    return CompMaterial.YELLOW_DYE.toItem();
  }

  public ItemStack cyanDye() {
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(CompMaterial.INK_SAC.getMaterial(), 1,
          DyeColor.CYAN.getDyeData());
    }
    return CompMaterial.CYAN_DYE.toItem();

  }

  public ItemStack cyanGlassPane() {
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(CompMaterial.ORANGE_STAINED_GLASS.getMaterial(), 1,
          DyeColor.CYAN.getDyeData());
    }
    return CompMaterial.CYAN_STAINED_GLASS_PANE.toItem();
  }
}
