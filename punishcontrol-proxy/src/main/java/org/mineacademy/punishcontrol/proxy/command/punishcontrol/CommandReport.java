package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

import java.util.UUID;

public final class CommandReport extends AbstractPunishCommand {
	public static CommandReport newInstance() {
		return new CommandReport();
	}

	private CommandReport() {
		super("report");
		setDescription("Report a player using a sleek gui.");
		setUsage("[player] [reason]");
	}

	@Override
	protected void onCase2(final @NonNull CommandSender sender, final @NonNull UUID target) {
		super.onCase2(sender, target);
	}

	@Override
	protected void onCase3(final @NonNull CommandSender sender, final @NonNull UUID target, final @NonNull PunishDuration punishDuration) {
		super.onCase3(sender, target, punishDuration);
	}

	@Override
	protected void onCase4(final @NonNull CommandSender player, final @NonNull UUID target, final @NonNull PunishDuration punishDuration, final @NonNull String reason) {
		super.onCase4(player, target, punishDuration, reason);
	}
}
