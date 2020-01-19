package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.punishcontrol.core.provider.TextureProvider;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyTextureProvider implements TextureProvider {

	public static ProxyTextureProvider newInstance() {
		return new ProxyTextureProvider();
	}

	@Override
	public void saveSkinTexture(final UUID uuid) {

	}

	@Override
	public String getSkinTexture(final UUID uuid) {
		return null;
	}
}
