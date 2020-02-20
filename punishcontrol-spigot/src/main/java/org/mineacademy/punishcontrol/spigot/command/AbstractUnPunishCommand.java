package org.mineacademy.punishcontrol.spigot.command;

import lombok.Getter;
import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.gui.MenuPunishBrowser;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

import java.util.*;

@Getter
public abstract class AbstractUnPunishCommand extends SimpleCommand {
	public static final Set<AbstractUnPunishCommand> REGISTERED_COMMANDS = new HashSet<>();

	public static final String INVALID_SILENCE_USAGE = "§cCan't be silent and super-silent simultaneously";
	public static final String UNKNOWN_PLAYER = "§cThis player is not known";


	private final StorageProvider provider;
	private final PunishType punishType;

	private boolean silent;
	private boolean superSilent;

	private final String[] MORE_ARGUMENTS_AS_CONSOLE_MESSAGE = new String[]{
		"You need to provide more information to run this command from console", "Please provide 3 arguments", "Usage: " + getUsage()
	};

	protected AbstractUnPunishCommand(final StorageProvider provider, final PunishType punishType, @NonNull final String... labels) {
		super(new StrictList<>(labels));
		this.provider = provider;
		this.punishType = punishType;
		setTellPrefix(Settings.PLUGIN_PREFIX);
		addTellPrefix(true);
		REGISTERED_COMMANDS.add(this);
	}


	@Override protected final void onCommand() {
		this.silent = checkSilent();
		this.superSilent = checkSuperSilent();

		if (isSilent() && isSuperSilent()) {
			returnTell(INVALID_SILENCE_USAGE);
		}


		final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
		//Args without params
		finalArgs.removeAll(Arrays.asList("-S", "-s", "-silent", "-super-slient"));

		switch (finalArgs.size()) {
			case 0:
				if (!isPlayer()) {
					returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
				}

				MenuPunishBrowser.showTo(getPlayer(), punishType);
				break;
			case 1:
				final UUID target = findTarget(finalArgs);

				switch (punishType) {
					case BAN:
						checkBoolean(provider.removeBanFor(target), "Player is not banned");
						break;
					case MUTE:
						checkBoolean(provider.removeMuteFor(target), "Player is not muted");
						break;
					case WARN:
						checkBoolean(provider.removeWarnFor(target), "Player is not warned");
						break;
				}

				break;
			default:
				returnInvalidArgs();
				break;

		}

	}

	// ----------------------------------------------------------------------------------------------------
	// Internal  helper-methods
	// ----------------------------------------------------------------------------------------------------

	private boolean checkSilent() {
		for (final String arg : args) {
			if (arg.equals("-s") || arg.equals("-silent")) {
				return true;
			}
		}
		return false;
	}

	private boolean checkSuperSilent() {
		for (final String arg : args) {
			//TODO Rework
			if (arg.equals("-S") || arg.equals("-Super-Silent")) {
				return true;
			}
		}
		return false;
	}

	private UUID findTarget(final List<String> args) {
		final UUID target = Providers.playerProvider().getUUID(args.get(0));
		checkNotNull(target, UNKNOWN_PLAYER);
		return target;
	}
}
