package org.mineacademy.punishcontrol.proxy;

import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.mineacademy.burst.item.Item;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

@UtilityClass
public class ItemUtil {

  public ItemStack forPunishType(@NonNull final PunishType punishType) {
    switch (punishType) {
      case BAN:
        return Item
            .of(ItemType.OAK_DOOR)
            .build();
      case MUTE:
        return Item
            .of(ItemType.PAPER)
            .build();
      case WARN:
        return Item
            .of(ItemType.YELLOW_DYE)
            .build();
    }

    throw new IllegalStateException(
        "Invalid punish type: '" + punishType.getClass().getSimpleName() + "'"
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

    return Arrays.asList(lores.replacedMessage());
  }
}
