package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Collection;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public abstract class AbstractBrowser<T>
    extends MenuPagged<T>
    implements Schedulable {

  private static int calculateSizeForContent(final int items) {
    return MathUtil.range((int) (9 * Math
            .floor(Math.abs(items / 5))),
        9, 9 * 5);
  }

  protected AbstractBrowser(final Menu parent, final Collection<T> content) {
    super(calculateSizeForContent(content.size()), parent, content);
  }

  @Override
  protected final boolean addPageNumbers() {
    return true;
  }

}
