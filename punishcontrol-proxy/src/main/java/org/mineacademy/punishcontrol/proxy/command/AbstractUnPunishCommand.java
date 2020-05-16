package org.mineacademy.punishcontrol.proxy.command;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.menus.browsers.AllPunishesBrowser;

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
  }

  @Override
  protected final void onCommand() {
    silent = checkSilent();
    superSilent = checkSuperSilent();

    if (silent && superSilent) {
      returnTell(INVALID_SILENCE_USAGE);
    }

    switch (args.length) {
      case 0:
        if (!isPlayer()) {
          returnTell("&c/" + getLabel() + " " + getUsage());
        }
        AllPunishesBrowser.showTo(getPlayer());
        break;
      case 1:
        final UUID target = findTarget(args[0]);

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
        tell("&7Successfully unpunished &6" + playerProvider.findName(target).orElse(
            "unknown"));
        break;
      default:
        returnInvalidArgs();
        break;
    }
  }
}
