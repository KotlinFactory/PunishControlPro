package org.mineacademy.punishcontrol.proxy.commands;

import lombok.val;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;

public class DevCommand extends SimpleCommand {


  public static DevCommand create() {
    return new DevCommand();
  }

  public DevCommand() {
    super("dev");
    setPermission(null);
  }

  @Override
  protected void onCommand() {


    final val player = getPlayer();

    for (final Permission perm : Permissions.registeredPermissions()) {
      tellPerm(perm.permission());
      player.setPermission(perm.permission(), true);
    }
  }

  private void tellPerm(final String perm) {
    tell("Has permission: [" + perm + "] " + (getPlayer().hasPermission(perm)
        ? "&ayes"
        : "&cno"));
  }
}
