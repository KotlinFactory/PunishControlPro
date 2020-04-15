package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractPunishCommand;

public final class BanCommand extends AbstractPunishCommand {

  @Inject
  public BanCommand(
      final @NonNull StorageProvider storageProvider,
      final @NonNull PlayerProvider playerProvider) {
    super(storageProvider, playerProvider, PunishType.BAN, "ban");
    setDescription("Ban a player");
  }
}
