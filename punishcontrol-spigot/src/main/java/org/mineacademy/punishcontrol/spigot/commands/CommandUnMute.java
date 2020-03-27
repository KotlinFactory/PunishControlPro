package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.command.AbstractUnPunishCommand;

import javax.inject.Inject;

public final class CommandUnMute extends AbstractUnPunishCommand {

  @Inject
  public CommandUnMute(final StorageProvider storageProvider) {
    super(storageProvider, PunishType.MUTE, "unmute");
    setUsage("[player]");
  }
}
