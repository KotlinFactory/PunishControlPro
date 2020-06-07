package org.mineacademy.punishcontrol.spigot.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.Players;


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

  public ItemStack redPane() {
    if (MinecraftVersion.olderThan(V.v1_13)) {
      final ItemStack limeDye = new ItemStack(
          CompMaterial.RED_STAINED_GLASS_PANE.getMaterial(), 1,
          DyeColor.RED.getDyeData());
    }
    return CompMaterial.RED_STAINED_GLASS_PANE.toItem();
  }

  public ItemStack greenPane() {
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
            .ofString(ItemSettings.BAN_ITEM.itemType())
            .build()
            .makeMenuTool();
      case MUTE:
        return ItemCreator
            .ofString(ItemSettings.MUTE_ITEM.itemType())
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

  public List<String> loreForPlayer(
      final UUID target,
      final StorageProvider storageProvider) {
    final boolean targetOnline = Players.find(target).isPresent();

    final Replacer lores = Replacer.of(
        "",
        "&6Online: &7{online}",
        "&6Banned: &7{banned}",
        "&6Muted: &7{muted}",
        "&6Warned: &7{warned}",
        ""
    );

    final val ban = storageProvider.currentBan(target);
    final val mute = storageProvider.currentMute(target);
    final val warn = storageProvider.currentWarn(target);

    lores.find("online", "banned", "muted", "warned");
    lores.replace(
        targetOnline ? "&ayes" : "&cno",
        ban.map(value -> "&ayes &7- " + value.punishDuration().toString())
            .orElse("&cno"),
        mute.map(value -> "&ayes &7- " + value.punishDuration().toString())
            .orElse("&cno"),
        warn.map(value -> "&ayes &7- " + value.punishDuration().toString())
            .orElse("&cno"));

    return Arrays.asList(lores.getReplacedMessage());
  }
}
