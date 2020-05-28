package org.mineacademy.punishcontrol.spigot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.flatfiles.SecureJson;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.util.MojangUtils;

public final class SpigotTextureProvider
    extends SecureJson
    implements TextureProvider {

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
    Debugger.debug("hash", "Received hash: '" + textureHash + "'");

    if (textureHash != null) {
      set(uuid.toString(), textureHash);
    }
  }

  @Override
  public String getSkinTexture(final UUID uuid) {
    return getString(uuid.toString());
  }

  @Override
  public List<String> listTextures() {
    final val result = new ArrayList<String>();
    for (final val entry : fileData.toMap().entrySet()) {
      result.add(entry.getValue().toString());
    }
    return result;
  }

  @Override
  public ExceptionHandler exceptionHandler() {
    return Providers.exceptionHandler();
  }
}
