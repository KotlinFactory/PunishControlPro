package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractUnPunishCommand;

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
