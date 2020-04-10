package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.UUID;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browser.PunishTemplateBrowser;

@Getter
@Accessors(fluent = true)
public enum SettingTypes {
  PLAYER {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(CompMaterial.PLAYER_HEAD,
          "&6Player settings",
          "&7Give permissions",
          "&7to apply punishes",
          "&7or view",
          "&7which permissions",
          "&7a player has")
          .build();
    }

    @Override
    public void showMenu(final Player player) {
      Scheduler.runAsync(() -> {
        final val browser = new AbstractPlayerBrowser(
            Providers.playerProvider(),
            Providers.textureProvider(),
            DaggerSpigotComponent.create().menuMain(),
            true) {

          @Override
          public void onClick(final UUID data) {
            PlayerSettingsMenu.showTo(getViewer(), data);
          }
        };
        Scheduler.runSync(() -> browser.displayTo(player));
      });

    }
  },

  STORAGE {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator
          .of(CompMaterial.ENDER_CHEST,
              "&6Punish Storage",
              "&7Click here to",
              "&7change how punishes",
              "&7are stored")
          .build();
    }

    @Override
    public void showMenu(final Player player) {
      StorageSettingsMenu.showTo(player);
    }
  },

  PUNISH_TEMPLATE{
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(CompMaterial.PAPER, "&6Punish-Templates", "&7View and add", "&7Punish-Templates").build();
    }

    @Override
    public void showMenu(final Player player) {
      PunishTemplateBrowser.showTo(player);
    }
  }



  ;

//  LANGUAGE {
//    @Override
//    public ItemCreator itemCreator() {
//      return ItemCreator.of(CompMaterial.PAPER, "&6Language", "&7Select the language").build();
//    }
//
//    @Override
//    public void showMenu(final Player player) {
//      LanguageSettingsMenu.showTo(player);
//    }
//  };

  public ItemCreator itemCreator() {
    throw new AbstractMethodError("Not implemented");

  }

  public void showMenu(final Player player) {
    throw new AbstractMethodError("Not implemented");
  }
}
