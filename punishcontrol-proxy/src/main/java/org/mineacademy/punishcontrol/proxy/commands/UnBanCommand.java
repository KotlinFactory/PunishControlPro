package org.mineacademy.punishcontrol.proxy.commands;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.command.AbstractUnPunishCommand;

public final class UnBanCommand extends AbstractUnPunishCommand {

  @Inject
  public UnBanCommand(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(storageProvider, playerProvider, PunishType.BAN, "unban", "uban");
    setUsage("[player]");
    setDescription("Unban a player");
    setPermission("punishcontrol.command.unban");
  }
}
