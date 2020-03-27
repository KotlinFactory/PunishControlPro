package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractUnPunishCommand;

import javax.inject.Inject;

public final class CommandUnBan extends AbstractUnPunishCommand {

  @Inject
  public CommandUnBan(final StorageProvider storageProvider) {
    super(storageProvider, PunishType.BAN, "unban");
    setUsage("[player]");
    setDescription("Unban a player");
  }
}
