package org.mineacademy.punishcontrol.proxy.commands;

import de.leonhard.storage.util.FileUtils;
import lombok.val;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.TimeUtil;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.util.Scheduler;

public final class BackupCommand extends SimpleCommand {

  public static BackupCommand create() {
    return new BackupCommand();
  }

  private BackupCommand() {
    super("backup");
    setPermission("punishcontrol.command.backup");
    setDescription("Backup server files");
    setUsage("?");
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
