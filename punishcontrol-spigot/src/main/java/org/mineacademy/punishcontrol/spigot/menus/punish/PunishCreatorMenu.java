package org.mineacademy.punishcontrol.spigot.menus.punish;

import de.leonhard.storage.util.Valid;
import java.util.Arrays;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.conversation.AddReasonConversation;
import org.mineacademy.punishcontrol.spigot.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.PunishChooserMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public final class PunishCreatorMenu extends Menu {

  public static final int SIZE = 9 * 5;
  private final Button fromTemplate;
  private final Button chooseDuration;
  private final Button chooseReason;
  private final Button choosePlayer;
  private final Button choosePunishType;
  private final TextureProvider textureProvider;
  private final PlayerProvider playerProvider;

  //Silent & Super silent

  private PunishBuilder punishBuilder;
  private PunishTemplate punishTemplate;
  private final PunishType punishType = PunishType.BAN;


  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().punishCreatorMenu().displayTo(player);
  }

  public static void showTo(
      @NonNull final Player player,
      @NonNull final PunishBuilder punishBuilder) {
    final val punishCreatorMenu = DaggerSpigotComponent
        .create()
        .punishCreatorMenu();
    punishCreatorMenu.punishBuilder = punishBuilder;
    punishCreatorMenu.displayTo(player);
  }

  public static void showTo(
      @NonNull final Player player,
      @NonNull final PunishBuilder punishBuilder,
      @NonNull final PunishTemplate punishTemplate) {
    final val punishCreatorMenu = DaggerSpigotComponent
        .create()
        .punishCreatorMenu();
    punishCreatorMenu.punishBuilder = punishBuilder;
    punishCreatorMenu.punishTemplate = punishTemplate;
    punishCreatorMenu.displayTo(player);
  }


  @Inject
  public PunishCreatorMenu(
      final TextureProvider textureProvider,
      final PlayerProvider playerProvider,
      final MainMenu mainMenu) {
    super(mainMenu);
    this.textureProvider = textureProvider;
    this.playerProvider = playerProvider;
    setSize(SIZE);

    chooseDuration = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractDurationChooser(menu) {

          @Override
          protected void confirm() {
            Valid.checkBoolean(menu instanceof PunishChooserMenu,
                "Invalid type?");
            final val creatorMenu = (PunishCreatorMenu) menu;
            creatorMenu.punishBuilder().duration(ms);
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator.of(CompMaterial.CLOCK,
            "&6Duration",
            "&7Choose the",
            "&7duration of the",
            "&7punish")
            .build()
            .make();
      }
    };

    fromTemplate = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        new AbstractTemplateBrowser(menu) {

          @Override
          protected void onClick(final PunishTemplate choosenTemplate) {
            final val builder = choosenTemplate.toPunishBuilder();
            builder.target(punishBuilder().target());
            PunishCreatorMenu.showTo(getViewer(), builder, choosenTemplate);
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        if (punishTemplate != null) {

          return ItemCreator
              .of(ItemStacks.forPunishType(punishBuilder().punishType()))
              .name("&6Choose template")
              .lores(Arrays.asList(
                  "&7Create an punish",
                  "&7from an existing",
                  "&7template",
                  "&7Current: &6: " + punishTemplate.name()))
              .build()
              .makeMenuTool();
        }

        return ItemCreator
            .of(ItemStacks.forPunishType(punishBuilder().punishType()))
            .name("&6From template")
            .lores(Arrays.asList(
                "&7Create an punish",
                "&7from an existing",
                "&7template"))
            .build()
            .makeMenuTool();
      }
    };

    chooseReason = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        player.closeInventory();
        AddReasonConversation.create((PunishCreatorMenu) menu);
      }

      @Override
      public ItemStack getItem() {

        if (punishBuilder().reason() != null) {
          return ItemCreator.of(CompMaterial.BOOK,
              "&6Reason",
              "&7Choose different reason",
              "&7Current: " + punishBuilder.reason())
              .build()
              .make();
        }

        return ItemCreator.of(CompMaterial.BOOK,
            "&6Reason",
            "&7Choose the",
            "&7reason of the",
            "&7punish")
            .build()
            .make();
      }
    };

    choosePlayer = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractPlayerBrowser(
            Providers.playerProvider(),
            Providers.textureProvider(),
            menu,
            true) {

          @Override
          public void onClick(final UUID data) {
            PunishCreatorMenu.showTo(player, punishBuilder().target(data));
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        final UUID target = punishBuilder().target();
        if (target != null) {
          return ItemCreator
              .fromCustomHash(textureProvider.getSkinTexture(target))
              .lore("&6Choose player")
              .lores(Arrays.asList("&7Choose different player",
                  "&7Current: " + playerProvider
                      .getName(punishBuilder().target())))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .of(CompMaterial.PLAYER_HEAD, "&6Choose player",
                "&7Choose the",
                "&7player the", "&7punish should be", "&7applied to")
            .build()
            .makeMenuTool();
      }
    };

    choosePunishType = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractPunishTypeBrowser(menu) {
          @Override
          protected void onClick(final PunishType punishType) {
            PunishCreatorMenu
                .showTo(player, punishBuilder().punishType(punishType));
          }
        };
      }

      @Override
      public ItemStack getItem() {
        return ItemStacks.forPunishType(punishBuilder().punishType());
      }
    };
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to", "&7create punishes"};
  }

  //Lazy getter for the builder
  private PunishBuilder punishBuilder() {
    Valid.notNull(punishType, "PunishType was set to null!");
    if (punishBuilder != null) {
      return punishBuilder;
    }

    return punishBuilder = PunishBuilder.of(punishType);
  }

  public void setReason(final String reason) {
    punishBuilder.reason(reason);
  }

  @Override
  public ItemStack getItemAt(final int slot) {
    if (slot == 10) {
      return choosePlayer.getItem();
    }
    if (slot == 17) {
      return fromTemplate.getItem();
    }

    if (slot == 19) {
      return chooseReason.getItem();
    }

    if (slot == 28) {
      return chooseDuration.getItem();
    }

    if (slot == 30) {
      choosePunishType.getItem();
    }

    return null;
  }
}


    