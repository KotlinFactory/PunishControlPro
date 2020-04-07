package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractUnPunishCommand;

public final class CommandUnWarn extends AbstractUnPunishCommand {

  private final StorageProvider storageProvider;

  @Inject
  public CommandUnWarn(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(storageProvider, playerProvider, PunishType.WARN, "unwarn");
    this.storageProvider = storageProvider;
    setUsage("[player]");
  }
}
