package org.mineacademy.punishcontrol.core.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class Permissions {
	public String OP = "PunishControl.*";
	public String DEV = "PunishControl.Developer";
	public String MERGE = "PunishControl.Merge";
	public String BY_PASS = "PunishControl.bypass";


	@UtilityClass
	public class CMD {
		public String HELP = "PunishControl.CMD.Help";
		public String EXPORT = "PunishControl.CMD.Export";
	}

	@UtilityClass
	public class BANS {
		public String BAN = "PunishControl.Bans.Ban";
		public String LIST = "PunishControl.Bans.List";
		public String UN_BAN = "PunishControl.Bans.UnBan";
	}


	@UtilityClass
	public class MUTES {
		public String MUTE = "PunishControl.Mutes.Mute";
		public String LIST = "PunishControl.Mutes.List";
		public String UN_MUTE = "PunishControl.Mutes.UnMute";
	}

	@UtilityClass
	public class WARNS {
		public String WARN = "PunishControl.Warns.Warn";
		public String LIST = "PunishControl.Warns.List";
	}

	@UtilityClass
	public class KICKS {
		public String KICK = "PunishControl.Kicks.Kick";
		public String LIST = "PunishControl.Kicks.List";
	}

	@UtilityClass
	public class REPORTS {
		public String REPORT_PLAYER = "PunishControl.Reports.Report";
		public String ACCEPT = "PunishControl.Reports.Accept";
		public String DENY = "PunishControl.Reports.Deny";


	}
}

