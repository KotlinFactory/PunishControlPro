package org.mineacademy.punishcontrol.core.storage;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;

public interface StorageProvider {

  default void setup() {

  }

  default boolean isSetup() {
    return true;
  }

  default PlayerCache getCacheFor(@NonNull final UUID uuid) {
    return new PlayerCache(this, uuid);
  }

  default Set<UUID> listPunishedPlayers(){
    return listCurrentPunishes()
        .stream()
        .filter((punish -> !punish.isOld()))
        .map(Punish::target)
        .collect(Collectors.toSet());
  }

  // ----------------------------------------------------------------------------------------------------
  // Listing all punishes/warns/reports ever made
  // ----------------------------------------------------------------------------------------------------

  default List<Punish> listPunishes() {
    final List<Punish> result = new ArrayList<>();

    result.addAll(listBans());
    result.addAll(listMutes());
    result.addAll(listWarns());

    return result;
  }

  List<Ban> listBans();

  List<Mute> listMutes();

  List<Warn> listWarns();

  // ----------------------------------------------------------------------------------------------------
  // Listing all current punishes/warns/reports
  // ----------------------------------------------------------------------------------------------------


  default List<Punish> listCurrentPunishes(){
    final List<Punish> result = new ArrayList<>();

    result.addAll(listBans());
    result.addAll(listMutes());
    result.addAll(listWarns());

    return result;
  }

  default List<Ban> listCurrentBans() {

    final List<Ban> bans = listBans();

    // Making our stream parallel on large collections
    final Stream<Ban> banStream =
        bans.size() > 40 ? bans.stream().parallel() : bans.stream();

    return banStream.filter((ban) -> !ban.isOld()).collect(Collectors.toList());
  }

  default List<Mute> listCurrentMutes() {

    final List<Mute> mutes = listMutes();

    // Making our stream parallel on large collections
    final Stream<Mute> warnStream =
        mutes.size() > 40 ? mutes.stream().parallel() : mutes.stream();

    return warnStream.filter((mute) -> !mute.isOld())
        .collect(Collectors.toList());
  }


  default List<Warn> listCurrentWarns() {
    final List<Warn> warns = listWarns();

    // Making our stream parallel on large collections
    final Stream<Warn> warnStream =
        warns.size() > 40 ? warns.stream().parallel() : warns.stream();

    return warnStream.filter((mute) -> !mute.isOld())
        .collect(Collectors.toList());
  }

  // ----------------------------------------------------------------------------------------------------
  //
  // Methods which needs UUID's/Punishes as argument
  //
  // ----------------------------------------------------------------------------------------------------

  // ----------------------------------------------------------------------------------------------------
  // List all punishes/warns/reports the player ever had
  // ----------------------------------------------------------------------------------------------------

  default List<Punish> listPunishes(final UUID uuid) {
    final List<Punish> result = new ArrayList<>();

    result.addAll(listBans(uuid));
    result.addAll(listMutes(uuid));
    result.addAll(listWarns(uuid));

    return result;
  }

  List<Ban> listBans(@NonNull UUID uuid);

  List<Mute> listMutes(@NonNull UUID uuid);

  List<Warn> listWarns(@NonNull UUID uuid);

  // ----------------------------------------------------------------------------------------------------
  // Methods to handle the data of specific players
  // ----------------------------------------------------------------------------------------------------


  default Optional<Ban> currentBan(@NonNull final UUID uuid) {

    for (final Ban ban : listBans(uuid)) {
      if (!ban.isOld()) {
        return Optional.of(ban);
      }
    }
    return Optional.empty();
  }

  default Optional<Ban> currentBan(@NonNull final InetAddress inetAddress) {
    for (final Ban ban : listCurrentBans()) {
      final String toCompare = ban.ip().orElse("unknown");
      if ("unknown".equalsIgnoreCase(toCompare)) {
        continue;
      }
      //toCompare is never null
      if (toCompare.equalsIgnoreCase(inetAddress.getHostAddress())) {
        return Optional.ofNullable(ban);
      }
    }
    return Optional.empty();
  }


  default Optional<Mute> currentMute(@NonNull final UUID uuid) {
    for (final Mute mute : listMutes(uuid)) {
      if (!mute.isOld()) {
        return Optional.of(mute);
      }
    }

    return Optional.empty();
  }

  default Optional<Mute> currentMute(@NonNull final InetAddress inetAddress) {
    for (final Mute mute : listCurrentMutes()) {
      final String toCompare = mute.ip().orElse("unknown");
      if ("unknown".equalsIgnoreCase(toCompare)) {
        continue;
      }
      //toCompare is never null
      if (toCompare.equalsIgnoreCase(inetAddress.getHostAddress())) {
        return Optional.ofNullable(mute);
      }
    }
    return Optional.empty();
  }

  default Optional<Warn> currentWarn(@NonNull final UUID uuid) {
    for (final Warn warn : listWarns(uuid)) {
      if (!warn.isOld()) {
        return Optional.of(warn);
      }
    }

    return Optional.empty();
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to check whether a player is punished
  // ----------------------------------------------------------------------------------------------------

  default boolean isPunished(
      @NonNull final UUID uuid,
      @NonNull final PunishType punishType) {
    switch (punishType) {
      case BAN:
        return isBanned(uuid);
      case MUTE:
        return isMuted(uuid);
      case WARN:
        return isWarned(uuid);
    }
    throw new IllegalStateException("Invalid punish-type");
  }

  default boolean isBanned(@NonNull final UUID uuid) {
    return currentBan(uuid).isPresent();
  }

  default boolean isMuted(@NonNull final UUID uuid) {
    return currentMute(uuid).isPresent();
  }

  default boolean isWarned(@NonNull final UUID uuid) {
    return currentWarn(uuid).isPresent();
  }

  default boolean isBanned(@NonNull final InetAddress inetAddress) {
    for (final Ban ban : listCurrentBans()) {
      final String toCompare = ban.ip().orElse("unknown");
      if ("unknown".equalsIgnoreCase(toCompare)) {
        continue;
      }
      //toCompare is never null
      if (toCompare.equalsIgnoreCase(inetAddress.getHostAddress())) {
        return true;
      }
    }
    return false;
  }

  default boolean isMuted(@NonNull final InetAddress inetAddress) {
    for (final Mute ban : listCurrentMutes()) {
      final String toCompare = ban.ip().orElse("unknown");
      if ("unknown".equalsIgnoreCase(toCompare)) {
        continue;
      }
      //toCompare is never null
      if (toCompare.equalsIgnoreCase(inetAddress.getHostAddress())) {
        return true;
      }
    }
    return false;
  }

  default boolean isWarned(@NonNull final InetAddress inetAddress) {
    for (final Warn ban : listCurrentWarns()) {
      final String toCompare = ban.ip().orElse("unknown");
      if ("unknown".equalsIgnoreCase(toCompare)) {
        continue;
      }
      //toCompare is never null
      if (toCompare.equalsIgnoreCase(inetAddress.getHostAddress())) {
        return true;
      }
    }
    return false;
  }

  // ----------------------------------------------------------------------------------------------------
  // Saving & Removing our punishes
  // ----------------------------------------------------------------------------------------------------

  default void savePunish(@NonNull final Punish punish) {
    switch (punish.punishType()) {
      case BAN:
        saveBan((Ban) punish);
        break;
      case MUTE:
        saveMute((Mute) punish);
        break;
      case WARN:
        saveWarn((Warn) punish);
    }
  }

  void saveBan(@NonNull Ban ban);

  void saveMute(@NonNull Mute mute);

  void saveWarn(@NonNull Warn warn);

  // Returns false if not punished

  default void removePunish(final Punish punish) {
    switch (punish.punishType()) {
      case BAN:
        removeBan((Ban) punish);
        break;

      case MUTE:
        removeMute((Mute) punish);
        break;

      case WARN:
        removeWarn((Warn) punish);
        break;
    }
  }

  void removeBan(@NonNull final Ban ban);

  void removeMute(@NonNull final Mute mute);

  void removeWarn(@NonNull final Warn warn);

  default boolean removeBanFor(@NonNull final UUID target) {
    final Optional<Ban> optionalBan = currentBan(target);

    if (!optionalBan.isPresent()) {
      return false;
    }

    final Ban ban = optionalBan.get();

    removeBan(ban);

    return true;
  }

  default boolean removeMuteFor(@NonNull final UUID target) {
    final Optional<Mute> optionalMute = currentMute(target);

    if (!optionalMute.isPresent()) {
      return false;
    }

    final Mute mute = optionalMute.get();

    removeMute(mute);

    return true;
  }

  default boolean removeWarnFor(@NonNull final UUID target) {
    final Optional<Warn> optionalWarn = currentWarn(target);

    if (!optionalWarn.isPresent()) {
      return false;
    }

    final Warn warn = optionalWarn.get();

    removeWarn(warn);

    return true;
  }
}
