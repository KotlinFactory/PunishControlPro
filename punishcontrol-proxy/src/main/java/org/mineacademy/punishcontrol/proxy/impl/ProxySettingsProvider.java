package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.punishcontrol.core.provider.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.punish.Ban;

import java.util.List;
import java.util.Set;

//TODO

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxySettingsProvider implements SettingsProvider {

	public static ProxySettingsProvider newInstance() {
		return new ProxySettingsProvider();
	}

	@Override
	public Set<String> getReportReasons() {
		return null;
	}

	@Override
	public boolean cacheResults() {
		return false;
	}

	@Override
	public List<String> getJoinMessageForBannedPlayer(final Ban ban) {
		return null;
	}
}
