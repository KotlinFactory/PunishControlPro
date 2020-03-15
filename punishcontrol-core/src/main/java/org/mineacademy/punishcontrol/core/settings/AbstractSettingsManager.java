package org.mineacademy.punishcontrol.core.settings;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractSettingsManager {
  @NonNull private final File file;
}
