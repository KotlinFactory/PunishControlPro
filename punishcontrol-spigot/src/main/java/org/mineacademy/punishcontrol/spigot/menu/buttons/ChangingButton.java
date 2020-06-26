package org.mineacademy.punishcontrol.spigot.menu.buttons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.*;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.ItemCreator.ItemCreatorBuilder;
import org.mineacademy.fo.remain.CompMaterial;

@Setter
@Getter
@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangingButton {

  private final List<ItemCreator.ItemCreatorBuilder> creators;
  private int slot = 0;
  private String name = "";
  private List<String> lore = new ArrayList<>();
  // ----------------------------------------------------------------------------------------------------
  // Static factory methods
  // ----------------------------------------------------------------------------------------------------

  public static ChangingButton fromCustomHashes(final String... hashes) {
    return fromCustomHashes(Arrays.asList(hashes));
  }

  public static ChangingButton fromCustomHashes(final List<String> hashes) {
    final val creators = new ArrayList<ItemCreator.ItemCreatorBuilder>();

    for (final String hash : hashes)
      creators.add(ItemCreator.ofSkullHash(hash));

    return of(creators);
  }

  public static ChangingButton of(
      final List<ItemCreator.ItemCreatorBuilder> creators) {
    return new ChangingButton(creators);
  }

  public static ChangingButton of(
      final ItemCreator.ItemCreatorBuilder... creators) {
    return of(Arrays.asList(creators));
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods for editing our Changing button once its instantiated
  // ----------------------------------------------------------------------------------------------------

  public ChangingButton lore(final String... lores) {
    return lore(Arrays.asList(lores));
  }

  public ChangingButton lore(final List<String> lore) {
    this.lore = lore;
    for (final ItemCreatorBuilder creator : creators)
      creator.lores(lore);
    return this;
  }

  public ChangingButton name(final String name) {
    this.name = name;
    for (final ItemCreatorBuilder creator : creators)
      creator.name(name);
    return this;
  }

  // ----------------------------------------------------------------------------------------------------
  // Convenience methods
  // ----------------------------------------------------------------------------------------------------

  public ItemStack nextItem() {
    final int index = getRandomNumberInRange(0, creators().size() - 1);
    if (creators.isEmpty())
      return ItemCreator
          .of(CompMaterial.PLAYER_HEAD)
          .name(name)
          .lores(lore)
          .build()
          .make();
    if (creators.size() == 1)
      return creators.get(0).build().makeMenuTool();
    return creators().get(index).build().makeMenuTool();
  }

  private int getRandomNumberInRange(final int min, final int max) {

    if (min > max)
      return max;

    final Random random = new Random();
    return random.nextInt((max - min) + 1) + min;
  }
}
