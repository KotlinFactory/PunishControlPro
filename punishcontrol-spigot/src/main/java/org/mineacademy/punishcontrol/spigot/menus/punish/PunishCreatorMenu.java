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
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.conversation.PunishReasonConversation;
import org.mineacademy.punishcontrol.spigot.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public final class PunishCreatorMenu extends Menu {

  public static final int SIZE = 9 * 5;
  public static final int PLAYER_CHOOSER_SLOT = 33;
  public static final int CHOOSE_REASON_SLOT = 19;
  private final Button fromTemplate;
  private final Button chooseDuration;
  //  private final Button chooseReason;
  private final Button choosePlayer;
  private final Button applyPunish;
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

    if (punishBuilder().target() != null) {
      setTitle("&8Punish " + playerProvider.findNameUnsafe(punishBuilder().target()));
    } else {
      setTitle("&8Create punish");
    }

    chooseDuration = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractDurationChooser(menu) {

          @Override
          protected void confirm() {
            Valid.checkBoolean(menu instanceof PunishCreatorMenu,
                "Invalid type?");
            final val creatorMenu = (PunishCreatorMenu) menu;
            creatorMenu.punishBuilder().duration(ms);
            creatorMenu.displayTo(player);
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        if (punishBuilder().duration() != null) {
          return ItemCreator.of(CompMaterial.CLOCK,
              "&6Duration",
              "&7Currently: ",
              "&7" + punishBuilder().duration().toString(),
              "&7Punish will end on:",
              "&7" + Settings.Advanced
                  .formatDate(
                      System.currentTimeMillis() + punishBuilder().duration()
                          .toMs()),
              "",
              "&7Click to change")
              .build()
              .make();
        }

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
                  "&7Current: &6" + punishTemplate.name()))
              .build()
              .makeMenuTool();
        }

        return ItemCreator
            .of(CompMaterial.PAPER)
            .name("&6From template")
            .lores(Arrays.asList(
                "&7Create an punish",
                "&7from an existing",
                "&7template"))
            .build()
            .makeMenuTool();
      }
    };

    applyPunish = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        punishBuilder()
            .creator(getViewer().getUniqueId())
            .creation(System.currentTimeMillis());


        if (punishBuilder().punishType() == null) {
          animateTitle("&cMissing punish-type!");
          return;
        }
        if (punishBuilder().target() == null) {
          animateTitle("&cMissing target!");
          return;
        }
        if (punishBuilder().creator() == null) {
          animateTitle("&cMissing creator!");
          return;
        }

        if (punishBuilder().reason() == null) {
          animateTitle("&cMissing reason!");
          return;
        }

        punishBuilder().build().create();

      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.EMERALD_BLOCK,
                "&aApply",
                "&7Apply punish")
            .build()
            .makeMenuTool();
      }
    };

//    chooseReason = new Button() {
//      @Override
//      public void onClickedInMenu(
//          final Player player, final Menu menu, final ClickType click) {
//        AddReasonConversation.create((PunishCreatorMenu) menu);
//      }
//
//      @Override
//      public ItemStack getItem() {
//
//        if (punishBuilder().reason() != null) {
//          return ItemCreator.of(CompMaterial.BOOK,
//              "&6Reason",
//              "&7Choose different reason",
//              "&7Current: " + punishBuilder.reason())
//              .build()
//              .make();
//        }
//
//        return ItemCreator.of(CompMaterial.BOOK,
//            "&6Reason",
//            "&7Choose the",
//            "&7reason of the",
//            "&7punish")
//            .build()
//            .make();
//      }
//    };

    choosePlayer = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractPlayerBrowser(
            Providers.playerProvider(),
            Providers.textureProvider(),
            menu) {

          @Override
          public void onClick(final UUID data) {
            PunishCreatorMenu.showTo(player, punishBuilder().target(data));
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        //Must be handled elsewhere
        final UUID target = punishBuilder().target();
        if (target != null) {
          return ItemCreator
              .ofSkullHash(textureProvider.getSkinTexture(target))
              .lore("&6Choose player")
              .lores(Arrays.asList("&7Choose different player",
                  "&7Current: " + playerProvider
                      .findNameUnsafe(punishBuilder().target())))
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

//    choosePunishType = new Button() {
//      @Override
//      public void onClickedInMenu(
//          final Player player, final Menu menu, final ClickType click) {
//
//      }
//
//      @Override
//      public ItemStack getItem() {
//        return ItemStacks.forPunishType(punishBuilder().punishType());
//      }
//    };
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

    if (slot == 4) {
      return ItemCreator
          .of(ItemStacks.forPunishType(punishBuilder().punishType()))
          .name("&6Change type")
          .lores(Arrays.asList(
              "&7Change the type",
              "&7of the punish",
              "&7Currently: " + punishBuilder.punishType().localized()))
          .build()
          .makeMenuTool();
    }

    if (slot == CHOOSE_REASON_SLOT) {
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

    if (slot == 22) {
      return applyPunish.getItem();
    }

    if (slot == 25) {
      return fromTemplate.getItem();
    }

    if (slot == 29) {
      return chooseDuration.getItem();
    }

    //Handling Button for choosePlayer
    if (slot == PLAYER_CHOOSER_SLOT) {
      final UUID target = punishBuilder().target();
      if (target != null) {
        return ItemCreator
            .ofSkullHash(textureProvider.getSkinTexture(target))
            .name("&6Choose player")
            .lores(Arrays.asList(
                "&7Current: " + playerProvider
                    .findNameUnsafe(punishBuilder().target()),
                "&7Click to choose", "&7another player"))
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

    return null;
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {
    if (slot == 4) {
      new AbstractPunishTypeBrowser(this) {
        @Override
        protected void onClick(final PunishType punishType) {
          PunishCreatorMenu
              .showTo(player, punishBuilder().punishType(punishType));
        }
      }.displayTo(player);
    }

    if (slot == PLAYER_CHOOSER_SLOT) {
      //We don't care about the click-type
      choosePlayer.onClickedInMenu(player, this, ClickType.LEFT);
    }

    if (slot == CHOOSE_REASON_SLOT) {
      getViewer().closeInventory();
      PunishReasonConversation.create(this).start(getViewer());
    }
  }
}


    