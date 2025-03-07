package org.mineacademy.punishcontrol.spigot.importers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.providers.PluginManager;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.importer.AbstractPunishImporter;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

public final class BukkitPunishImporter extends AbstractPunishImporter {

  private final BanList IP_BAN_LIST = Bukkit.getBanList(Type.IP);
  private final BanList NAME_BAN_LIST = Bukkit.getBanList(Type.NAME);

  @Inject
  public BukkitPunishImporter(
      @NonNull final StorageProvider storageProvider,
      @NonNull final PluginManager pluginManager) {
    super(storageProvider, pluginManager, null);
  }

  @Override
  public List<Punish> listPunishesToImport() {

    return new ArrayList<>();
  }

  @Override
  public Notification notificationOnSuccess() {
    return Notification
        .of("66Imported Bukkit punishments!")
        .text(
            "&7Successfully imported punishments",
            "&7from bukkit"
        )
        .itemType(CompMaterial.LAVA_BUCKET);
  }

  @Override
  public String[] description() {
    return new String[]{"Import Punishments from Bukkit"};
  }

  @Override
  public String itemString() {
    return CompMaterial.LAVA_BUCKET.name();
  }

  @Override
  public boolean applicable() {
    return false;
  }
}
