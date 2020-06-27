package org.mineacademy.punishcontrol.spigot.menus.punish;

import de.leonhard.storage.util.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.conversations.PunishReasonConversation;
import org.mineacademy.punishcontrol.spigot.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public final class PunishCreatorMenu extends Menu implements Schedulable {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  private static final String[] CHOOSE_DURATION_LORE = {
      "&7Choose the",
      "&7duration of the",
      "&7punish"
  };

  @NonNls
  private static final String NO_ACCESS = "You don't have access to the template";
  @NonNls
  private static final String ALREADY_SUPER_SILENT = "Punish is already super-silent";
  @NonNls
  private static final String CHANGE_TYPE = "Change type";
  @NonNls
  private static final String CHOOSE_ACTION = "Choose action";
  @NonNls
  private static final String REASON = "Reason";
  @NonNls
  private static final String CHOOSE_PLAYER = "Choose player";
  @NonNls
  private static final String APPLY = "Apply";
  @NonNls
  private static final String APPLY_PUNISHMENT = "Apply punishment";
  private static final String[] MENU_INFORMATION = {"&7Menu to", "&7create punishes"};
  @NonNls
  private static final String TARGET_IS_UNPUNISHABLE = "Target is unpunishable";
  @NonNls
  private static final String CAN_T_OVERRIDE_PUNISHES = "Can't override punishes";
  @NonNls
  private static final String CREATED_PUNISH = "Created punish";
  @NonNls
  private static final String YOU_WOULD_EXCEED_YOUR_LIMITS = "You would exceed your limits";
  @NonNls
  private static final String MISSING_DURATION = "Missing duration!";
  @NonNls
  private static final String MISSING_REASON = "Missing reason!";
  @NonNls
  private static final String MISSING_CREATOR = "Missing creator!";
  @NonNls
  private static final String MISSING_TARGET = "Missing target!";
  @NonNls
  private static final String MISSING_PUNISH_TYPE = "Missing punish-type!";
  private static final String[] MAKE_SUPER_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7super silent"};
  private static final String[] MAKE_NOT_SUPER_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7not silent"};
  private static final String[] MAKE_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7silent"};
  private static final String[] MAKE_NOT_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7not silent"};
  private static final String[] CHOOSE_TARGET_LORE = {
      "&7Choose the",
      "&7player the",
      "&7punish should be",
      "&7applied to"};

  private static String[] CHOOSE_REASON_LORE = new String[]{
      "&7Choose the",
      "&7reason of the",
      "&7punish"};

  @NonNls
  private static final String CURRENT = "Current";
  @NonNls
  private static final String CHOOSE_A_DIFFERENT_REASON = "Choose a different reason";
  private static final String[] CHOOSE_TEMPLATE_LORE = {
      "&7Create a punish",
      "&7from an existing",
      "&7template"};
  @NonNls
  private static final String DURATION = "Duration";
  @NonNls
  private static final String SUPER_SILENT = "super-silent";
  @NonNls
  private static final String NOT_SUPER_SILENT = "not super-silent";
  @NonNls
  private static final String SILENT = "silent";
  @NonNls
  private static final String NOT_SILENT = "not silent";
  private static Replacer TYPE_REPLACER = Replacer
      .of(
          "&7Change the type",
          "&7of the punish",
          "&7" + CURRENT + "ly: {type}");

  private static Replacer duration = Replacer.of(
      "&7" + CURRENT + "ly: ",
      "&7{currently}",
      "&7Punish will end on:",
      "&7{end}",
      "",
      "&7Click to change");

  // ----------------------------------------------------------------------------------------------------
  // Button position
  // ----------------------------------------------------------------------------------------------------

  private static final int SIZE = 9 * 5;
  private static final int CHOOSE_PLAYER_SLOT = 33;
  private static final int CHOOSE_DURATION_SLOT = 29;
  private static final int APPLY_SLOT = 22;
  private static final int CHOOSE_REASON_SLOT = 19;
  private static final int MAKE_SILENT_SLOT = 30;
  private static final int MAKE_SUPER_SILENT_SLOT = 32;
  private static final int CHOOSE_TYPE_SLOT = 4;
  private static final int CHOOSE_TEMPLATE_SLOT = 25;

  // ----------------------------------------------------------------------------------------------------
  // Fields
  // ----------------------------------------------------------------------------------------------------

  private final Button fromTemplate;
  private final Button chooseDuration;
  private final Button makeSilent;
  private final Button makeSuperSilent;
  //  private final Button chooseReason;
  private final Button choosePlayer;
  private final Button applyPunish;
  private final TextureProvider textureProvider;
  private final StorageProvider storageProvider;
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
    punishCreatorMenu.displayTo(player, true);
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
      final StorageProvider storageProvider,
      final MainMenu mainMenu) {
    super(mainMenu);
    this.storageProvider = storageProvider;
    this.textureProvider = textureProvider;
    this.playerProvider = playerProvider;
    setSize(SIZE);

    if (punishBuilder().target() != null) {
      setTitle("&8Punish " + playerProvider.findName(punishBuilder().target()).orElse("unknown"));
    } else {
      setTitle("&8Create punish");
    }

    chooseDuration = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractDurationChooser(PunishCreatorMenu.this) {
          @Override
          protected void confirm() {
            punishBuilder().duration(ms);
            PunishCreatorMenu.this.displayTo(player);
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        if (punishBuilder().duration() != null) {
          duration
              .replaceAll(
                  "currently", punishBuilder().duration().toString(),
                  "end", Settings.Advanced
                      .formatDate(
                          System.currentTimeMillis() + punishBuilder().duration().toMs()));

          return ItemCreator.of(
              CompMaterial.CLOCK,
              "&6" + DURATION,
              duration.replacedMessage())
              .build()
              .make();
        }

        return ItemCreator.of(
            CompMaterial.CLOCK,
            "&6" + DURATION,
            CHOOSE_DURATION_LORE)
            .build()
            .make();
      }
    };

    fromTemplate = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractTemplateBrowser(PunishCreatorMenu.this) {

          @Override
          protected void onPageClick(
              final Player player,
              final PunishTemplate punishTemplate,
              final ClickType click) {
            //Applying new setting & showing up
            if (!Groups.hasAccess(player.getUniqueId(), punishTemplate)) {
              animateTitle("&c" + NO_ACCESS);
              return;
            }

            //Reinitialisation of our punishBuilder since this is easier & more safe to
            //apply. (To many values are changed)
            showTo(
                player,
                punishTemplate.toPunishBuilder().target(punishBuilder.target()),
                punishTemplate);
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        if (punishTemplate != null) {
          return ItemCreator
              .of(ItemStacks.forPunishType(punishBuilder().punishType()))
              .name("&6" + CHOOSE_ACTION)
              //TODO
              .lores(Arrays.asList(CHOOSE_TEMPLATE_LORE))
              .lore("&7" + CURRENT + ": &6" + punishTemplate.name())
              .build()
              .makeMenuTool();
        }

        return ItemCreator
            .of(CompMaterial.PAPER)
            .name("&6" + CHOOSE_ACTION)
            .lores(Arrays.asList(CHOOSE_TEMPLATE_LORE))
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
          animateTitle("&c" + MISSING_PUNISH_TYPE);
          return;
        }
        if (punishBuilder().target() == null) {
          animateTitle("&c" + MISSING_TARGET);
          return;
        }
        if (punishBuilder().creator() == null) {
          animateTitle("&c" + MISSING_CREATOR);
          return;
        }

        if (punishBuilder().reason() == null) {
          animateTitle("&c" + MISSING_REASON);
          return;
        }

        if (punishBuilder().duration() == null) {
          animateTitle("&c" + MISSING_DURATION);
          return;
        }

        //Checking access

        //Not from template
        if (punishTemplate == null) {
          if (!Groups.hasAccess(
              getViewer().getUniqueId(),
              punishBuilder().punishType(),
              punishBuilder().duration())) {
            animateTitle("&c" + YOU_WOULD_EXCEED_YOUR_LIMITS);
            return;
          }
        } else {
          if (!Groups.hasAccess(
              getViewer().getUniqueId(),
              punishTemplate)) {
            animateTitle("&c" + YOU_WOULD_EXCEED_YOUR_LIMITS);
            return;
          }
        }

        animateTitle("&7" + CREATED_PUNISH);
        async(() -> {

          if (!playerProvider.punishable(punishBuilder.target())) {
            animateTitle("&c" + TARGET_IS_UNPUNISHABLE);
            return;
          }

          if (storageProvider
                  .isPunished(punishBuilder.target(), punishBuilder.punishType())
              && !Groups
              .canOverride(getViewer().getUniqueId())) {
            animateTitle("&c" + CAN_T_OVERRIDE_PUNISHES);
            return;
          }

          punishBuilder().build().create();

          if (getParent() != null) {
            Common.runLater(() -> getParent().displayTo(getViewer()));
          }
        });
        getParent().displayTo(getViewer());
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(
                ItemSettings.APPLY_ITEM.itemType(),
                "&a" + APPLY,
                "&7" + APPLY_PUNISHMENT)
            .build()
            .makeMenuTool();
      }
    };

    choosePlayer = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {

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
              .name("&6" + CHOOSE_PLAYER)
              .lores(Arrays.asList(
                  "&7Choose different player",
                  "&7" + CURRENT + ": " + playerProvider.findName(target)
                      .orElse("unknown")))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .of(CompMaterial.PLAYER_HEAD, "&6" + CHOOSE_PLAYER,
                "&7Choose the",
                "&7player the",
                "&7punish should be",
                "&7applied to")
            .build()
            .makeMenuTool();
      }
    };

    makeSilent = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (punishBuilder.superSilent()) {
          animateTitle("&c" + ALREADY_SUPER_SILENT);
          return;
        }
        punishBuilder.silent(!punishBuilder.silent());
        restartMenu(punishBuilder.silent()
            ? "&a" + SILENT
            : "&" + NOT_SILENT);
      }

      @Override
      public ItemStack getItem() {
        if (punishBuilder.silent()) {
          return ItemCreator
              .ofString(ItemSettings.ENABLED.itemType())
              .name("&6" + SILENT)
              .lores(Arrays.asList(
                  MAKE_NOT_SILENT_LORE
              ))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .ofString(ItemSettings.DISABLED.itemType())
            .name("&6make " + SILENT)
            .lores(Arrays.asList(
                MAKE_SILENT_LORE
            ))
            .build()
            .makeMenuTool();
      }
    };

    makeSuperSilent = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (punishBuilder.silent()) {
          punishBuilder.silent(false);
        }
        punishBuilder.superSilent(!punishBuilder.superSilent());
        restartMenu(punishBuilder.superSilent()
            ? "&a" + SUPER_SILENT
            : "&c" + NOT_SUPER_SILENT);
      }

      @Override
      public ItemStack getItem() {
        if (punishBuilder.superSilent()) {
          return ItemCreator
              .ofString(ItemSettings.ENABLED.itemType())
              .name("&6" + SUPER_SILENT)
              .lores(Arrays.asList(
                  MAKE_NOT_SUPER_SILENT_LORE
              ))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .ofString(ItemSettings.DISABLED.itemType())
            .name("&6Make " + SUPER_SILENT)
            .lores(Arrays.asList(
                MAKE_SUPER_SILENT_LORE
            ))
            .build()
            .makeMenuTool();
      }
    };
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  //Lazy getter for the builder
  private PunishBuilder punishBuilder() {
    Valid.notNull(punishType, "Punishment Type was set to null!");
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

    if (slot == CHOOSE_TYPE_SLOT) {

      TYPE_REPLACER
          .find("type")
          .replace(punishBuilder.punishType().localized());

      return ItemCreator
          .of(ItemStacks.forPunishType(punishBuilder().punishType()))
          .name("&6" + CHANGE_TYPE)
          .lores(Arrays.asList(TYPE_REPLACER.replacedMessage()))
          .build()
          .makeMenuTool();
    }

    if (slot == CHOOSE_REASON_SLOT) {
      if (punishBuilder().reason() != null) {
        return ItemCreator.of(
            ItemSettings.REASON_ITEM.itemType(),
            "&6" + REASON,
            "&7" + CHOOSE_A_DIFFERENT_REASON,
            "&7" + CURRENT + ": " + punishBuilder.reason())
            .build()
            .make();
      }

      return ItemCreator.of(
          ItemSettings.REASON_ITEM.itemType(),
          "&6" + REASON,
          CHOOSE_REASON_LORE)
          .build()
          .make();
    }

    if (slot == 22) {
      return applyPunish.getItem();
    }

    if (slot == CHOOSE_TEMPLATE_SLOT) {
      return fromTemplate.getItem();
    }

    if (slot == 29) {
      return chooseDuration.getItem();
    }

    //Handling Button for choosePlayer
    if (slot == CHOOSE_PLAYER_SLOT) {
      final UUID target = punishBuilder().target();
      if (target != null) {
        return ItemCreator
            .ofSkullHash(textureProvider.getSkinTexture(target))
            .name("&6" + CHOOSE_PLAYER)
            .lores(Collections.singleton("&7" + CURRENT + ": " + playerProvider.findName(target)
                .orElse("unknown")))
            .build()
            .makeMenuTool();
      }

      return ItemCreator
          .of(CompMaterial.PLAYER_HEAD, "&6" + CHOOSE_PLAYER,
              CHOOSE_TARGET_LORE)
          .build()
          .makeMenuTool();
    }

    if (slot == MAKE_SILENT_SLOT) {
      return makeSilent.getItem();
    }

    if (slot == MAKE_SUPER_SILENT_SLOT) {
      return makeSuperSilent.getItem();
    }

    return null;
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {
    if (slot == CHOOSE_TYPE_SLOT) {
      new AbstractPunishTypeBrowser(this) {
        @Override
        protected void onClick(final PunishType punishType) {
          showTo(player, punishBuilder().punishType(punishType));
        }
      }.displayTo(player);
    }

    if (slot == CHOOSE_PLAYER_SLOT) {
      //We don't care about the click-type
      choosePlayer.onClickedInMenu(player, this, ClickType.LEFT);
    }

    if (slot == CHOOSE_REASON_SLOT) {
      getViewer().closeInventory();
      PunishReasonConversation.create(this).start(getViewer());
    }
  }
}