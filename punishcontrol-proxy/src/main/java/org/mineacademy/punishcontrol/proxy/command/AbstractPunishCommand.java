package org.mineacademy.punishcontrol.proxy.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.bfo.Players;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.debug.LagCatcher;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.settings.Replacer;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;

/**
 * Command to handle (Un) banning, muting, warning, reporting & kicking players
 */
/*
TODO: Put in core & work with type parameters
 */

@Getter
public abstract class AbstractPunishCommand
    extends AbstractSimplePunishControlCommand {

  private final StorageProvider storageProvider;
  private final PunishType punishType;

  protected AbstractPunishCommand(
      @NonNull final StorageProvider storageProvider,
      @NonNull final PlayerProvider playerProvider,
      @NonNull final PunishType punishType,
      @NonNull final String... labels) {
    super(playerProvider, new StrictList<>(labels));
    this.storageProvider = storageProvider;
    this.punishType = punishType;
  }

  @Override
  protected final String[] getMultilineUsageMessage() {
    return new String[]{
        " ",
        "&2[] &7= Optional arguments (use only 1 at once)",
        "&6<> &7= Required arguments",
        "&7/" + getLabel() + " &8* &7See a list of players",
        "&7/" + getLabel() + " &6<player> &8* &7View options for player",
        "&7/" + getLabel() + " &2[-s] [-S] &6<player> <duration> <reason>",
        "&7/" + getLabel() + " &2[-s] [-S] &6<player> &6<punish-template>",
        " ",
    };
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override
  // ----------------------------------------------------------------------------------------------------

  // Is the reason provided valid? If not, use returnTell to break up the
  // command
  protected void handleReasonInput(@NonNull final String reason) {
  }

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

    final StringBuilder reason = new StringBuilder();

    switch (size) {
      case 0:
        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }

        PunishCreatorMenu.showTo(getPlayer(), PunishBuilder.of(punishType));
        break;
      case 1:

        if ("?".equalsIgnoreCase(finalArgs.get(0)) || "help"
            .equalsIgnoreCase(finalArgs.get(0))) {
          returnTell(getMultilineUsageMessage());
        }

        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }
        // Choose action (PUNISH)
        PunishCreatorMenu.showTo(getPlayer(),
            PunishBuilder.of(punishType).target(findTarget(finalArgs)));
        break;
      case 2:
        final val optionalTemplate = PunishTemplates.fromName(finalArgs.get(1));

        checkBoolean(
            optionalTemplate.isPresent(),
            "&cThis template doesn't exist"
        );

        final val template = optionalTemplate.get();

        checkBoolean(Groups.hasAccess(
            isPlayer()
                ? getPlayer().getUniqueId()
                : FoConstants.CONSOLE,
            template), "&cYou can't access this template.");

        checkBoolean(
            template.punishType() == punishType,
            "&cThis punish-template can't be applied to a &6" + punishType
                .localized()
        );

        Scheduler.runAsync(() -> {
          final UUID target = findTarget(finalArgs);
          final Punish punish = template.toPunishBuilder()
              .creator(isPlayer()
                  ? getPlayer().getUniqueId()
                  : FoConstants.CONSOLE)
              .target(target)
              .ip(playerProvider.ip(target).orElse("unknown"))
              .creation(System.currentTimeMillis())
              .build();

          punish.create();

          if (!punishType.shouldKick()) {
            return;
          }

          Players.find(target).ifPresent((player -> {
//          TODO: Format Reason!
            player.disconnect(reason.toString());
          }));
        });

        // Open inv
        break;
      case 3:

        final PunishDuration punishDuration = PunishDuration
            .of(finalArgs.get(1));

        //PunishDuration mustn't be empty: If its empty the given string had the wrong format
        checkBoolean(!punishDuration.isEmpty(),
            "&cInvalid time format! Example: 10days");

        checkBoolean(Groups.hasAccess(
            (isPlayer()
                ? getPlayer().getUniqueId()
                : FoConstants.CONSOLE),
            punishType,
            punishDuration),
            "&cThis action would exceed your limits."
        );

        Scheduler.runAsync(() -> {
          LagCatcher.start("spigot-cmd-save-async");

          final UUID target = findTarget(finalArgs);

          if (storageProvider.isPunished(target, punishType) && !Groups
              .canOverride(target)) {
            tell("&cYou are not allowed to override punishes");
            return;
          }

          for (int i = 0; i < finalArgs.size(); i++) {
            // Ignoring first & second argument//
            if (i == 0 || i == 1) {
              continue;
            }

            reason.append(finalArgs.get(i)).append(" ");
          }

          System.out.println("HEY");

          final Punish punish =
              PunishBuilder.of(punishType)
                  .target(target)
                  .creator(isPlayer()
                      ? getPlayer().getUniqueId()
                      : FoConstants.CONSOLE) //The uuid of the player called "Console"
                  .duration(punishDuration)
                  .creation(System.currentTimeMillis())
                  .reason(reason.toString())
                  .silent(silent)
                  .superSilent(superSilent)
                  .build();

          punish.create();

          final Replacer punishMessage =
              Replacer.of(
                  "&7You &asuccessfully &7punished &6{target} &7for &6{duration}",
                  "&6Reason: &6{reason}");

          punishMessage.replaceAll(
              "target",
              playerProvider.findName(target).orElse("unknown"),
              "duration",
              punishDuration.toString(),
              "reason",
              reason.toString());

          tell(punishMessage.replacedMessage());

          if (!punishType.shouldKick()) {
            return;
          }

          Players.find(target).ifPresent(targetPlayer -> {
            targetPlayer.disconnect(Punishes.formOnPunishMessage(punish));
          });

          LagCatcher.end("spigot-cmd-save-async");
        });
        break;
    }
  }
}
