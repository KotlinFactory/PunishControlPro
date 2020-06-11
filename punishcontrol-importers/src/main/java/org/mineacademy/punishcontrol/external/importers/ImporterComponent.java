package org.mineacademy.punishcontrol.external.importers;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;

@Component(modules = Providers.class)
public interface ImporterComponent {

  LitebansPunishImporter litebansPunishImporter();

}
