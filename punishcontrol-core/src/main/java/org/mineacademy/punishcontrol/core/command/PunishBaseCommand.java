package org.mineacademy.punishcontrol.core.command;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

import java.util.UUID;

public interface PunishBaseCommand<S> {
  String UNKNOWN_PLAYER = "§cThis player is not known";
  String INVALID_SILENCE_USAGE = "§cCan't be silent and super-silent simultaneously";

  // ----------------------------------------------------------------------------------------------------
  // Methods to implement
  // ----------------------------------------------------------------------------------------------------

  void checkNotNull(@NonNull final Object toCheck, @NonNull final String... messages);

  void showUpMainGUI();

  void invalidArgs();

  void returnTell(@NonNull final String... messages);

  boolean isSuperSilent();

  boolean isSilent();

  boolean isPlayer();

  int getMaxArgs();

  String[] getArgs();

  S getSender();

  // ----------------------------------------------------------------------------------------------------
  // Methods to handle our command-input
  // ----------------------------------------------------------------------------------------------------

  default void handleCommandInput(@NonNull final String[] args) {
    if (isSilent() && isSuperSilent()) {
      returnTell(INVALID_SILENCE_USAGE);
    }

    switch (args.length) {
      case 0:
        if (!isPlayer()) {
          returnTell(
              "Console needs to provide more information",
              "To run this command,",
              "please provide at least 2 arguments.");
        }
        showUpMainGUI();
        // SEND GUI
        break;
      case 1:
        onCase2(getSender(), findTarget());
        break;
      case 2:
        if (getMaxArgs() < 2) {
          invalidArgs();
        }
        onCase3(getSender(), findTarget(), PunishDuration.of(args[1]));
        break;
      case 3:
        if (getMaxArgs() < 3) {
          invalidArgs();
        }
        onCase4(getSender(), findTarget(), PunishDuration.of(args[1]), args[2]);
        break;
    }
  }

  default void onCase2(@NonNull final S sender, @NonNull final UUID target) {}

  default void onCase3(
      @NonNull final S sender,
      @NonNull final UUID target,
      @NonNull final PunishDuration punishDuration) {}

  default void onCase4(
      @NonNull final S player,
      @NonNull final UUID target,
      @NonNull final PunishDuration punishDuration,
      @NonNull final String reason) {}

  default boolean checkSilent() {
    for (final String arg : getArgs()) {
      if (arg.equals("-s") || arg.equals("-silent")) {
        return true;
      }
    }
    return false;
  }

  default boolean checkSuperSilent() {
    for (final String arg : getArgs()) {
      // TODO Rework
      if (arg.equals("-S") || arg.equals("-Super-Silent")) {
        return true;
      }
    }
    return false;
  }

  default UUID findTarget() {
    final UUID target = Providers.playerProvider().getUUID(getArgs()[0]);
    checkNotNull(target, UNKNOWN_PLAYER);
    return target;
  }
}
