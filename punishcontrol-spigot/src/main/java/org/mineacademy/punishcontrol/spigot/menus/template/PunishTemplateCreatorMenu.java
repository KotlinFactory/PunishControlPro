package org.mineacademy.punishcontrol.spigot.menus.template;

import java.io.File;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;

public final class PunishTemplateCreatorMenu extends Menu {

  private final PunishTemplate template;


  public static void createAndShow(
      @NonNull final Player player,
      @NonNull final String name) {
    Scheduler.runAsync(() -> {
      final val menu = create(name);

      Scheduler.runSync(() -> menu.displayTo(player));
    });
  }

  public static void fromExistingAndShow(
      @NonNull final Player player,
      @NonNull final PunishTemplate punishTemplate) {
    Scheduler.runAsync(() -> {
      final val menu = fromExisting(punishTemplate);

      Scheduler.runSync(() -> menu.displayTo(player));
    });
  }

  public static PunishTemplateCreatorMenu fromExisting(
      @NonNull final PunishTemplate punishTemplate) {
    return new PunishTemplateCreatorMenu(punishTemplate);
  }

  public static PunishTemplateCreatorMenu create(@NonNull final String name) {
    final File file = new File(
        SimplePlugin.getData() + "/templates/",
        name + ".json");
    //Creating new
    final val template = PunishTemplate.createWithDefaults(file);
    //Registering
    PunishTemplates.register(template);
    return fromExisting(template);
  }

  private PunishTemplateCreatorMenu(
      @NonNull final PunishTemplate punishTemplate) {
    super(DaggerSpigotComponent.create().punishTemplateBrowser());
    template = punishTemplate;
  }
}
