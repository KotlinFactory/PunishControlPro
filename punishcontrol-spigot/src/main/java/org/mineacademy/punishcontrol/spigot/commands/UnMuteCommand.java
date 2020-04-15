package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractUnPunishCommand;

public final class UnMuteCommand extends AbstractUnPunishCommand {

  @Inject
  public UnMuteCommand(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(storageProvider, playerProvider, PunishType.MUTE, "unmute");
    setUsage("[player]");
    setDescription("Unmute a player");
  }

}
