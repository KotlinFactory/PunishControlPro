package org.mineacademy.punishcontrol.spigot.commands;

import de.leonhard.storage.util.FileUtils;
import lombok.val;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.spigot.Scheduler;

public final class BackupCommand extends SimpleCommand {

  public static BackupCommand create() {
    return new BackupCommand();
  }

  private BackupCommand() {
    super("backup");
    setPermission("punishcontrol.command.backup");
    setDescription("Backup server files");
  }

  @Override
  protected void onCommand() {
    if (args.length != 0) {
      return;
    }

    tell("Starting backup:)");

    final val player = getPlayer();
    Scheduler.runAsync(() -> {

      FileUtils.zipFile(
          SimplePlugin.getData().getAbsolutePath() + "/data",
          SimplePlugin.getData().getAbsolutePath() + "/backups/backup-" + TimeUtil
              .getFormattedDate());
      Common.tell(player, "&aBackup is done");
    });
    tell("&aBackup is in progress:)");
  }
}
