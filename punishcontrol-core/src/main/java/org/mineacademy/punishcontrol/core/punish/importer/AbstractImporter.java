package org.mineacademy.punishcontrol.core.punish.importer;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AbstractImporter {

  private final String name;
}
