package org.mineacademy.punishcontrol.core.fo.constants;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

/**
 * Stores constants for this plugin
 */
@UtilityClass
public class FoConstants {

  private final PluginDataProvider DATA = Providers
      .pluginDataProvider();
  public final UUID CONSOLE = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

  public String configLine() {
    return "-------------------------------------------------------------------------------------------";
  }

  @UtilityClass
  public class File {

    /**
     * The name of our settings file
     */
    public final String SETTINGS = "settings.yml";
  }

  @UtilityClass
  public class Header {

    /**
     * The header that is put into the file that has been automatically updated.
     */
    public final String[] UPDATED_FILE = new String[]{
        configLine(),
        "",
        " Your file has been automatically updated at " + TimeUtil.getFormattedDate(),
        " to " + DATA.getNamed() + " " + DATA.getVersion(),
        "",
        " Due to the improvements LightningStorage (Library we use) brought most comments",
        " should have been preserved.",
        "",
        " If you'd like to view the default file, you can either:",
        " a) Open the " + DATA.getSource().getName() + " with a WinRar or similar",
        " b) or, visit: https://github.com/kangarko/" + DATA.getNamed() + "/wiki",
        "",
        configLine(),
        ""
    };
  }
}
