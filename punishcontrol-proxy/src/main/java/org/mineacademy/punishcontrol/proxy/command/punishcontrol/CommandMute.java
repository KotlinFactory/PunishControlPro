package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

import java.util.UUID;

public final class CommandMute extends AbstractPunishCommand {

	public static CommandMute newInstance() {
		return new CommandMute();
	}

	private CommandMute() {
		super("mute");
		setUsage("[player] [time] [reason]");
	}

	@Override
	protected void onCase2(final @NonNull CommandSender sender, final @NonNull UUID target) {
	}

	@Override
	protected void onCase3(final @NonNull CommandSender sender, final @NonNull UUID target, final @NonNull PunishDuration punishDuration) {
	}

	@Override
	protected void onCase4(final @NonNull CommandSender player, final @NonNull UUID target, final @NonNull PunishDuration punishDuration, final @NonNull String reason) {
	}
}
