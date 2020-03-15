package org.mineacademy.punishcontrol.spigot.impl;

import de.leonhard.storage.Json;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.provider.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.util.MojangUtils;

import java.util.UUID;

public final class SpigotTextureProvider extends Json implements TextureProvider {
  private SpigotTextureProvider() {
    super(
        PunishControlManager.FILES.SKIN_STORAGE,
        SimplePlugin.getData().getAbsolutePath() + "/data/");
  }

  public static SpigotTextureProvider newInstance() {
    return new SpigotTextureProvider();
  }

  @Override
  public void saveSkinTexture(final UUID uuid) {
    final String textureHash = MojangUtils.getTextureHash(uuid);
    Debugger.debug("hash", textureHash);

    if (textureHash != null) {
      set(uuid.toString(), textureHash);
    }
  }

  @Override
  public String getSkinTexture(final UUID uuid) {
    return getString(uuid.toString());
  }
}
