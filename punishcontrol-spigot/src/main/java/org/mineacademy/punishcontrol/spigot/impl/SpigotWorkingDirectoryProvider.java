package org.mineacademy.punishcontrol.spigot.impl;

import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.provider.WorkingDirectoryProvider;

import java.io.File;

public class SpigotWorkingDirectoryProvider implements WorkingDirectoryProvider {
	@Override
	public File getDataFolder() {
		return SimplePlugin.getData();
	}

	@Override
	public File getSource() {
		return SimplePlugin.getSource();
	}
}
