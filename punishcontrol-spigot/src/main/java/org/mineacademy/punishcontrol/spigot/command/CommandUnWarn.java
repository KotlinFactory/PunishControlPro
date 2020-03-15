package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import javax.inject.Inject;

public final class CommandUnWarn extends AbstractUnPunishCommand {
  private final StorageProvider storageProvider;

  @Inject
  public CommandUnWarn(final StorageProvider storageProvider) {
    super(storageProvider, PunishType.WARN, "unwarn");
    this.storageProvider = storageProvider;
    setUsage("[player]");
  }
}
