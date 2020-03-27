package org.mineacademy.punishcontrol.proxy.commands;

import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.proxy.command.AbstractPunishCommand;

import java.util.UUID;

public final class CommandKick extends AbstractPunishCommand {

  private CommandKick() {
    super("kick");
  }

  public static CommandKick newInstance() {
    return new CommandKick();
  }

  @Override
  protected void onCase2(final @NonNull CommandSender sender, final @NonNull UUID target) {}

  @Override
  protected void onCase3(
      final @NonNull CommandSender sender,
      final @NonNull UUID target,
      final @NonNull PunishDuration punishDuration) {}

  @Override
  protected void onCase4(
      final @NonNull CommandSender player,
      final @NonNull UUID target,
      final @NonNull PunishDuration punishDuration,
      final @NonNull String reason) {}
}
