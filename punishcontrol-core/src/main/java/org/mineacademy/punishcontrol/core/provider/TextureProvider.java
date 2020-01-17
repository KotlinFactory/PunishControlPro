package org.mineacademy.punishcontrol.core.provider;

import java.util.UUID;

public interface TextureProvider {

	void saveSkinTexture(UUID uuid);

	String getSkinTexture(UUID uuid);
}
