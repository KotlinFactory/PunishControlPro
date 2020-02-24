package org.mineacademy.punishcontrol.spigot.settings;

import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.settings.SimpleLocalization;

public class Localization extends SimpleLocalization {


	public static Replacer PLAYER_DOES_NOT_EXIST;

	private static void init() {

		PLAYER_DOES_NOT_EXIST = getReplacer("Player.Not_Exists");

	}

	@Override
	protected int getConfigVersion() {
		return 1;

	}
}
