package org.mineacademy.punishcontrol.proxy.commands;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.command.AbstractUnPunishCommand;

public final class UnWarnCommand extends AbstractUnPunishCommand {

  private final StorageProvider storageProvider;

  @Inject
  public UnWarnCommand(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(storageProvider, playerProvider, PunishType.WARN, "unwarn", "uwarn");
    this.storageProvider = storageProvider;
    setUsage("[player]");
    setDescription("Unwarn a player");
    setPermission("punishcontrol.command.unwarn");

  }
}
