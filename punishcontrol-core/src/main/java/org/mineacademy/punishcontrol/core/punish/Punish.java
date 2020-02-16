package org.mineacademy.punishcontrol.core.punish;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@Accessors(chain = true, fluent = true)
public abstract class Punish {
	private String reason;
	private String ip;
	private final UUID target, creator;
	@NonNull private PunishDuration punishDuration;
	private final PunishType punishType;
	@NonNull private long creation;
	private boolean removed = false;

	protected Punish(@NonNull final UUID target, @NonNull final UUID creator, @NonNull final PunishDuration duration, @NonNull final PunishType punishType, final long creation) {
		this.target = target;
		this.creator = creator;
		this.punishDuration = duration;
		this.punishType = punishType;
		this.creation = creation;
	}

	protected Punish(final long creation, @NonNull final Map<String, Object> banRawData, @NonNull final PunishType punishType) {
		this(UUID.fromString((String) banRawData.get("target")), UUID.fromString((String) banRawData.get("creator")), PunishDuration.of((long) banRawData.get("duration")), punishType, creation);
		ip((String) banRawData.get("ip"));
		removed((Boolean) banRawData.get("removed"));
	}

	public abstract void create();

	public final boolean isOld() {
		if (removed()) {
			return true;
		}
		return getEndTime() > System.currentTimeMillis();
	}

	public final long getEndTime() {
		return creation + punishDuration.toMs();
	}

	public final Optional<String> ip() {
		return Optional.ofNullable(ip);
	}

	//target, creator, reason, duration, ip, removed
	public final Map<String, Object> toMap() {
		final Map<String, Object> result = new HashMap<>();

		result.put("target", target().toString());
		result.put("creator", creator().toString());
		result.put("reason", reason());
		result.put("duration", punishDuration().toMs());
		result.put("creation", creation());
		result.put("ip", ip().isPresent() ? ip().get() : "unknown");
		result.put("removed", removed());

		return result;
	}
}
