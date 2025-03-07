package org.mineacademy.punishcontrol.proxy.commands;

import javax.inject.Inject;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.command.AbstractUnPunishCommand;

public final class UnMuteCommand extends AbstractUnPunishCommand {

  @Inject
  public UnMuteCommand(
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(storageProvider, playerProvider, PunishType.MUTE, "unmute", "umute");
    setUsage("[player]");
    setDescription("Unmute a player");
    setPermission("punishcontrol.command.unmute");
  }

}
