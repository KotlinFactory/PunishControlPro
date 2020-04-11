package org.mineacademy.punishcontrol.core.providers;

import java.util.List;
import java.util.UUID;

public interface TextureProvider {

  void saveSkinTexture(UUID uuid);

  String getSkinTexture(UUID uuid);

  List<String> listTextures();
}
