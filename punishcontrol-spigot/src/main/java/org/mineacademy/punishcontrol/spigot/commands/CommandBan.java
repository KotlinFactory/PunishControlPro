package org.mineacademy.punishcontrol.spigot.commands;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractPunishCommand;

import javax.inject.Inject;

public final class CommandBan extends AbstractPunishCommand {

  @Inject
  public CommandBan(
      final @NonNull StorageProvider storageProvider,
      final @NonNull PlayerProvider playerProvider) {
    super(storageProvider, playerProvider, PunishType.BAN, "ban");
  }
}
