package org.mineacademy.punishcontrol.proxy.settings;

import de.leonhard.storage.Yaml;
import org.mineacademy.bfo.settings.SimpleLocalization;

public class Localization extends SimpleLocalization {
	@Override
	protected int getConfigVersion() {
		return 0;
	}

	@Override
	public Yaml getConfigInstance() {
		return null;
	}
}
