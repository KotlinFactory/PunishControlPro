package org.mineacademy.punishcontrol.spigot.menus.settings;

public enum Setting {
  PLAYER{
    @Override
    public void showMenu() {
      super.showMenu();
    }
  },
  STORAGE{
    @Override
    public void showMenu() {
      super.showMenu();
    }
  },
  LANGUAGE{
    @Override
    public void showMenu() {
      super.showMenu();
    }
  };

  public void showMenu(){
    throw new AbstractMethodError("Not implemented");
  }
}
