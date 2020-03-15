package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import javax.inject.Inject;

public final class CommandUnBan extends AbstractUnPunishCommand {

  @Inject
  public CommandUnBan(final StorageProvider storageProvider) {
    super(storageProvider, PunishType.BAN, "unban");
    setUsage("[player]");
    setDescription("Unban a player");
  }
}
