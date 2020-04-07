package org.mineacademy.punishcontrol.spigot.commands;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractUnPunishCommand;

public final class CommandUnMute extends AbstractUnPunishCommand {

  @Inject
  public CommandUnMute(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(storageProvider, playerProvider, PunishType.MUTE, "unmute");
    setUsage("[player]");
  }

}
