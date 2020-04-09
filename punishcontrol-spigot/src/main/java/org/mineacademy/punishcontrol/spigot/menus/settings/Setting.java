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

@Getter
@Accessors(fluent = true)
public enum Setting {
  PLAYER {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(CompMaterial.PLAYER_HEAD).build();
    }

    @Override
    public int slot() {
      return 2;
    }

    @Override
    public void showMenu(final Player player) {
      Scheduler.runAsync(() -> {
        final val browser = new AbstractPlayerBrowser(Providers.playerProvider(), Providers.textureProvider(),
            DaggerSpigotComponent.create().menuMain()){

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
      return ItemCreator.of(CompMaterial.ENDER_CHEST).build();
    }

    @Override
    public int slot() {
      return 3;
    }

    @Override
    public void showMenu(final Player player) {
      StorageSettingsMenu.showTo(player);
    }
  },
  LANGUAGE {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(CompMaterial.PAPER).build();
    }

    @Override
    public int slot() {
      return 4;
    }

    @Override
    public void showMenu(final Player player) {
      LanguageSettingsMenu.showTo(player);
    }
  };


  public ItemCreator itemCreator() {
    throw new AbstractMethodError("Not implemented");

  }

  public int slot() {
    throw new AbstractMethodError("Not implemented");
  }

  public void showMenu(final Player player) {
    throw new AbstractMethodError("Not implemented");
  }
}
