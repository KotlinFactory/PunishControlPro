package org.mineacademy.punishcontrol.spigot.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.menus.MenuPlayerBrowser;

/** Command to handle (Un) banning, muting, warning, reporting & kicking players */
/*
TODO: Put in core & work with type parameters
 */

@Getter
public abstract class AbstractPunishCommand extends AbstractSimplePunishControlCommand {
  private static final UUID CONSOLE = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

  private final StorageProvider storageProvider;
  private final PunishType punishType;
  private final PlayerProvider playerProvider;

  protected AbstractPunishCommand(
      @NonNull final StorageProvider storageProvider,
      @NonNull final PlayerProvider playerProvider,
      @NonNull final PunishType punishType,
      @NonNull final String... labels) {
    super(new StrictList<>());
    this.storageProvider = storageProvider;
    this.playerProvider = playerProvider;
    this.punishType = punishType;
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override
  // ----------------------------------------------------------------------------------------------------

  // Is the reason provided valid? If not, use returnTell to break up the
  // command
  protected void handleReasonInput(@NonNull final String reason) {}

  /*
  ban linkskeinemitte 10d Hacking  //3
  report [-PARAM] linkskeinemitte test reason with spaces //
  kick [-PARAM] linkskeinemitte
  kick [-PARAM] linkskeinemitte hacking
  warn [-PARAM] linkskeinemitte 10d hacking

  unban linkskeinemitte //1
  unmute linkskeinemitte //1
  unwarn linkskeinemitte //1
   */

  @Override
  protected final void onCommand() {

    // Checking the console if needed.
    if (!consoleAllowed) {
      checkConsole();
    }

    silent = checkSilent();
    superSilent = checkSuperSilent();

    if (silent && superSilent) {
      returnTell(INVALID_SILENCE_USAGE);
    }

    final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
    // Args without params
    finalArgs.removeAll(Arrays.asList("-S", "-s", "-silent", "-super-slient"));

    final int size = Math.min(finalArgs.size(), 3);

    final UUID target;
    final PunishDuration punishDuration;

    final StringBuilder reason = new StringBuilder();

    switch (size) {
      case 0:
        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }

        MenuPlayerBrowser.showTo(getPlayer());
        break;
      case 1:
        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }
        // Choose action (PUNISH)
        // For player
        break;
      case 2:
        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }
        // Open inv

        break;
      case 3:
        target = findTarget(finalArgs);
        punishDuration = PunishDuration.of(finalArgs.get(1));

        for (int i = 0; i < finalArgs.size(); i++) {
          if (i == 0 || i == 1) // Ignoring first & second argument
          {
            continue;
          }
          reason.append(finalArgs.get(i)).append(" ");
        }

        final Punish punish =
            PunishBuilder.of(punishType)
                .target(target)
                .creator(isPlayer() ? getPlayer().getUniqueId() : CONSOLE) //The uuid of the player called "Console"
                .duration(punishDuration)
                .creation(System.currentTimeMillis())
                .reason(reason.toString())
                .silent(silent)
                .superSilent(superSilent)
                .build();

        Common.runLaterAsync(punish::create);

        final Replacer punishMessage =
            Replacer.of(
                "&7You &asuccessfully &7punished &6{target} &7for &6{duration}",
                "&6Reason: &6{reason}");

        punishMessage.replaceAll(
            "target",
            playerProvider.getName(target),
            "duration",
            punishDuration.toString(),
            "reason",
            reason.toString());

        tell(punishMessage.getReplacedMessage());

        break;
    }

    //    switch (size) {
    //      case 0: // Noting
    //        if (!isPlayer()) {
    //          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
    //        }
    //        MenuPlayerBrowser.showTo(getPlayer());
    //        break;
    //      case 1: // Target
    //        if (!isPlayer()) {
    //          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
    //        }
    //        onTargetProvided(getPlayer(), findTarget(finalArgs));
    //        break;
    //      case 2: // Target, Duration
    //        // ban NAME DURATION
    //        if (getMaxArgs() < 2) {
    //          returnInvalidArgs();
    //        }
    //
    //        if (!isPlayer()) {
    //          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
    //        }
    //
    //        onTargetAndDurationProvided(getPlayer(), findTarget(finalArgs), punishDuration);
    //        break;
    //      case 3: // Target, Duration, Reason
    //
    //        // Validating the reason
    //        handleReasonInput(reason.toString());
    //
    //        // ban NAME REASON, DURATION
    //        onTargetAndDurationAndReasonProvided(
    //            getSender(), findTarget(finalArgs), punishDuration, reason.toString());
    //        break;
    //    }

    // Declaring the reason. Instantiation below

  }
}
