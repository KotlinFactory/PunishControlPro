package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Collection;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public abstract class AbstractBrowser<T>
    extends MenuPagged<T>
    implements Schedulable {

  protected AbstractBrowser(final Menu parent, final Collection<T> content) {
    super(parent, content);
  }

  @Override
  protected final boolean addPageNumbers() {
    return true;
  }
}
