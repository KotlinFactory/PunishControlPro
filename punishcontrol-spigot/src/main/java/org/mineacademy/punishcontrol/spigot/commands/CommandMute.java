package org.mineacademy.punishcontrol.spigot.commands;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.spigot.command.AbstractPunishCommand;

import java.util.UUID;

public final class CommandMute extends AbstractPunishCommand {

  private CommandMute() {
    super("mute");
    setUsage("[player] [time] [reason]");
    setDescription("Mute a player using a sleek gui");
  }

  public static CommandMute newInstance() {
    return new CommandMute();
  }

  @Override
  protected void onTargetProvided(final Player player, final @NonNull UUID target) {}

  @Override
  protected void onTargetAndDurationProvided(
      final Player player, final @NonNull UUID target, final PunishDuration punishDuration) {}

  @Override
  protected void onTargetAndDurationAndReasonProvided(
      @NonNull final CommandSender player,
      final @NonNull UUID target,
      final @NonNull PunishDuration punishDuration,
      final @NonNull String reason) {}
}
