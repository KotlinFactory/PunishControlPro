package org.mineacademy.punishcontrol.core.group;

import java.util.*;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;

@UtilityClass
public class Groups {

  private final PlayerProvider provider = Providers.playerProvider();
  private final List<Group> registeredGroups = new ArrayList<>();

  public boolean canOverride(@NonNull final UUID target) {
    if (target.equals(FoConstants.CONSOLE)) {
      return true;
    }
    final val optionalGroup = primaryGroupOf(target);
    return optionalGroup.map(Group::overridePunishes).orElse(true);
  }

  public boolean hasAccess(
      @NonNull final UUID uuid,
      @NonNull final PunishTemplate punishTemplate) {
    final val optionalGroup = primaryGroupOf(uuid);
    if (!optionalGroup.isPresent()) {
      return true;
    }

    final Group group = optionalGroup.get();

    if (group.templateByPasses().contains("*")) {
      return true;
    }

    if (group.templateByPasses().contains(punishTemplate.name())) {
      return true;
    }

    switch (punishTemplate.punishType()) {
      case BAN:
        return punishTemplate.duration().lessThan(group.banLimit());
      case MUTE:
        return punishTemplate.duration().lessThan(group.muteLimit());
      case WARN:
        return punishTemplate.duration().lessThan(group.warnLimit());
    }

    return true;
  }

  public boolean hasAccess(
      @NonNull final UUID target,
      @NonNull final PunishType punishType,
      @NonNull final PunishDuration punishDuration) {
    //The console has all rights:)
    if (target.equals(FoConstants.CONSOLE)) {
      return true;
    }
    final val optionalGroup = primaryGroupOf(target);
    if (!optionalGroup.isPresent()) {
      return true;
    }

    final val group = optionalGroup.get();

    if (group.templateOnly()) {
      return false;
    }

    switch (punishType) {
      case BAN:
        return group.banLimit().moreThan(punishDuration);
      case MUTE:
        return group.muteLimit().moreThan(punishDuration);
      case WARN:
        return group.warnLimit().moreThan(punishDuration);
    }
    return false;
  }

  public void registerGroup(@NonNull final Group group) {
    registeredGroups.add(group);
  }

  public List<Group> list(@NonNull final UUID target) {
    final List<Group> result = new ArrayList<>();

    for (final Group group : registeredGroups) {
      if (!provider.hasPermission(target, group.permission())) {
        continue;
      }
      result.add(group);
    }

    return result;
  }

  /**
   * @param target UUID to find the group of
   * @return The group of the highest priority the player according to the UUID has
   */
  public Optional<Group> primaryGroupOf(@NonNull final UUID target) {
    Group lastResult = null;

    for (final Group group : registeredGroups) {

      // Does the player has the permissions he need
      if (!provider.hasPermission(target, group.permission())) {
        continue;
      }

      // Has the last result a higher priority than the current group?
      if (lastResult != null && lastResult.priority() <= group.priority()) {
        continue;
      }

      lastResult = group;
    }

    return Optional.ofNullable(lastResult);
  }

  // Returns the name of the registered groups
  public List<String> groupNames() {

    final List<String> result = new ArrayList<>();

    for (final Group group : registeredGroups) {
      result.add(group.name());
    }

    return result;
  }

  public List<Group> registeredGroups() {
    // We don't want anybody else to change our list:)
    return Collections.unmodifiableList(registeredGroups);
  }
}
