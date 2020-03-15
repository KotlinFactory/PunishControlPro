package org.mineacademy.punishcontrol.core.provider.providers;

import java.util.UUID;

public interface TextureProvider {

  void saveSkinTexture(UUID uuid);

  String getSkinTexture(UUID uuid);
}
