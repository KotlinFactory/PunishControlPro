package org.mineacademy.punishcontrol.spigot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.settings.Settings;
import org.mineacademy.punishcontrol.spigot.menus.MenuPunishBrowser;

@Getter
public abstract class AbstractUnPunishCommand extends
    AbstractSimplePunishControlCommand {

  protected final StorageProvider provider;
  protected final PunishType punishType;

  protected AbstractUnPunishCommand(
      @NonNull final StorageProvider provider,
      @NonNull final PlayerProvider playerProvider,
      @NonNull final PunishType punishType,
      @NonNull final String... labels) {
    super(playerProvider, new StrictList<>(labels));
    this.provider = provider;
    this.punishType = punishType;
    setTellPrefix(Settings.PLUGIN_PREFIX);
    addTellPrefix(true);
    REGISTERED_COMMANDS.add(this);
  }

  @Override
  protected final void onCommand() {
    silent = checkSilent();
    superSilent = checkSuperSilent();

    if (silent && superSilent) {
      returnTell(INVALID_SILENCE_USAGE);
    }

    final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
    // Args without params
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

        //TODO: Async
        switch (punishType) {
          case BAN:
            checkBoolean(provider.removeBanFor(target), "Player is not banned");
            break;
          case MUTE:
            checkBoolean(provider.removeMuteFor(target), "Player is not muted");
            break;
          case WARN:
            checkBoolean(provider.removeWarnFor(target),
                "Player is not warned");
            break;
        }
        break;
      default:
        returnInvalidArgs();
        break;
    }
  }
}
