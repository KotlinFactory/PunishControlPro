package org.mineacademy.punishcontrol.proxy.impl;

import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.provider.WorkingDirectoryProvider;

import java.io.File;

public class ProxyWorkingDirectoryProvider implements WorkingDirectoryProvider {
	@Override
	public File getDataFolder() {
		return SimplePlugin.getData();
	}

	@Override
	public File getSource() {
		return SimplePlugin.getSource();
	}
}
