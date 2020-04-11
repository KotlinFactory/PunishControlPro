package org.mineacademy.punishcontrol.proxy.impl;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyTextureProvider implements TextureProvider {

  public static ProxyTextureProvider create() {
    return new ProxyTextureProvider();
  }

  @Override
  public void saveSkinTexture(final UUID uuid) {
  }

  @Override
  public String getSkinTexture(final UUID uuid) {
    return null;
  }

  @Override
  public List<String> listTextures() {
    return null;
  }
}
