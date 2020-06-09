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
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
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
      final StorageProvider storageProvider,
      final PlayerProvider playerProvider) {
    final boolean targetOnline = Players.find(target).isPresent();

    final Replacer lores = Replacer.of(
        "",
        "&6Online: &7{online}",
        "&6Banned: &7{banned}",
        "&6Muted: &7{muted}",
        "&6Warned: &7{warned}",
        "&6Punishable: &7{punishable}"
    );

    final val ban = storageProvider.currentBan(target);
    final val mute = storageProvider.currentMute(target);
    final val warn = storageProvider.currentWarn(target);
    val punishable = playerProvider.punishable(target) ? "&ayes" : "&cno";

    lores.find("online", "banned", "muted", "warned", "punishable");
    lores.replace(
        targetOnline ? "&ayes" : "&cno",
        ban.map(value -> "&ayes &7- " + value.punishDuration().toString())
            .orElse("&cno"),
        mute.map(value -> "&ayes &7- " + value.punishDuration().toString())
            .orElse("&cno"),
        warn.map(value -> "&ayes &7- " + value.punishDuration().toString())
            .orElse("&cno"),
        punishable
    );

    return Arrays.asList(lores.getReplacedMessage());
  }


  public boolean isItem(@NonNull final CompMaterial material) {
    switch (material) {
      case ACACIA_WALL_SIGN:
      case ATTACHED_MELON_STEM:
      case ATTACHED_PUMPKIN_STEM:
      case BAMBOO_SAPLING:
      case BEETROOTS:
      case BIRCH_WALL_SIGN:
      case BLACK_WALL_BANNER:
      case BLUE_WALL_BANNER:
      case BRAIN_CORAL_WALL_FAN:
      case BROWN_WALL_BANNER:
      case BUBBLE_COLUMN:
      case BUBBLE_CORAL_WALL_FAN:
      case CARROTS:
      case CAVE_AIR:
      case COCOA:
      case CREEPER_WALL_HEAD:
      case CYAN_WALL_BANNER:
      case DARK_OAK_WALL_SIGN:
      case DEAD_BRAIN_CORAL_WALL_FAN:
      case DEAD_BUBBLE_CORAL_WALL_FAN:
      case DEAD_FIRE_CORAL_WALL_FAN:
      case DEAD_HORN_CORAL_WALL_FAN:
      case DEAD_TUBE_CORAL_WALL_FAN:
      case DRAGON_WALL_HEAD:
      case END_GATEWAY:
      case END_PORTAL:
      case FIRE:
      case FIRE_CORAL_WALL_FAN:
      case FROSTED_ICE:
      case GRAY_WALL_BANNER:
      case GREEN_WALL_BANNER:
      case HORN_CORAL_WALL_FAN:
      case JUNGLE_WALL_SIGN:
      case KELP_PLANT:
      case LAVA:
      case LIGHT_BLUE_WALL_BANNER:
      case LIGHT_GRAY_WALL_BANNER:
      case LIME_WALL_BANNER:
      case MAGENTA_WALL_BANNER:
      case MELON_STEM:
      case MOVING_PISTON:
      case NETHER_PORTAL:
      case OAK_WALL_SIGN:
      case ORANGE_WALL_BANNER:
      case PINK_WALL_BANNER:
      case PISTON_HEAD:
      case PLAYER_WALL_HEAD:
      case POTATOES:
      case POTTED_ACACIA_SAPLING:
      case POTTED_ALLIUM:
      case POTTED_AZURE_BLUET:
      case POTTED_BIRCH_SAPLING:
      case POTTED_BLUE_ORCHID:
      case POTTED_BROWN_MUSHROOM:
      case POTTED_CACTUS:
      case POTTED_DANDELION:
      case POTTED_DARK_OAK_SAPLING:
      case POTTED_DEAD_BUSH:
      case POTTED_FERN:
      case POTTED_JUNGLE_SAPLING:
      case POTTED_OAK_SAPLING:
      case POTTED_ORANGE_TULIP:
      case POTTED_OXEYE_DAISY:
      case POTTED_PINK_TULIP:
      case POTTED_POPPY:
      case POTTED_RED_MUSHROOM:
      case POTTED_RED_TULIP:
      case POTTED_SPRUCE_SAPLING:
      case POTTED_WHITE_TULIP:
      case PUMPKIN_STEM:
      case PURPLE_WALL_BANNER:
      case REDSTONE_WALL_TORCH:
      case REDSTONE_WIRE:
      case RED_WALL_BANNER:
      case SKELETON_WALL_SKULL:
      case SPRUCE_WALL_SIGN:
      case TALL_SEAGRASS:
      case TRIPWIRE:
      case TUBE_CORAL_WALL_FAN:
      case VOID_AIR:
      case AIR:
      case WALL_TORCH:
      case WATER:
      case WHITE_WALL_BANNER:
      case WITHER_SKELETON_WALL_SKULL:
      case YELLOW_WALL_BANNER:
      case ZOMBIE_WALL_HEAD:
        return false;
      default:
        return true;
    }
  }
}
