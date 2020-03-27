package org.mineacademy.punishcontrol.core.providers;

import java.util.UUID;

public interface TextureProvider {

  void saveSkinTexture(UUID uuid);

  String getSkinTexture(UUID uuid);
}
