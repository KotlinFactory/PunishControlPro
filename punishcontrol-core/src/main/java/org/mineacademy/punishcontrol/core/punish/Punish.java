package org.mineacademy.punishcontrol.core.punish;

import de.leonhard.storage.util.Valid;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.provider.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.provider.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Data
@Accessors(chain = true, fluent = true)
@ToString
public abstract class Punish {
	protected static final PunishProvider PUNISH_PROVIDER = Providers.punishProvider();
	protected static final ExceptionHandler EXCEPTION_HANDLER = Providers.exceptionHandler();
	protected static final StorageProvider STORAGE_PROVIDER = Providers.storageProvider();
	protected static final PlayerProvider PLAYER_PROVIDER = Providers.playerProvider();

	private String reason;
	private String ip;
	private final UUID target, creator;
	@NonNull private PunishDuration punishDuration;
	private final PunishType punishType;
	@NonNull private long creation;
	private boolean removed = false;

	//Silence ^^

	private boolean isSilent;
	private boolean isSuperSilent;

	/**
	 * Validates our raw-data which
	 * should contain keys to parse a punish from
	 * <p>
	 * Keys which will be validated:
	 * target, creator, duration, ip, removed
	 *
	 * @param rawData Map to validate
	 * @return False is validation fails
	 */
	protected static void validateRaWData(final Map<String, Object> rawData) {
		if (!(rawData.get("target") instanceof String)) {
			Valid.error(
				"PunishRaw-Data is missing an String named 'target'",
				"Have you altered data?",
				"Data: '" + rawData.toString() + "'"
			);
		}

		if (!(rawData.get("creator") instanceof String)) {
			Valid.error(
				"PunishRaw-Data is missing an String named 'creator'",
				"Have you altered data?",
				"Data: '" + rawData.toString() + "'"
			);
		}

		if (!(rawData.get("ip") instanceof String)) {
			Valid.error(
				"PunishRaw-Data is missing an String named 'ip'",
				"Have you altered data?",
				"Data: '" + rawData.toString() + "'"
			);
		}

		if (!(rawData.containsKey("removed"))) {
			Valid.error(
				"PunishRaw-Data is missing an Boolean named 'removed'",
				"Have you altered data?",
				"Data: '" + rawData.toString() + "'"
			);
		}

		if (!(rawData.containsKey("duration"))) {
			Valid.error(
				"PunishRaw-Data is missing an long named 'duration'",
				"Have you altered data?",
				"Data: '" + rawData.toString() + "'"
			);

		}
	}

	protected Punish(@NonNull final UUID target, @NonNull final UUID creator, @NonNull final PunishDuration duration, @NonNull final PunishType punishType, final long creation) {
		this.target = target;
		this.creator = creator;
		this.punishDuration = duration;
		this.punishType = punishType;
		this.creation = creation;
	}

	protected Punish(final long creation, @NonNull final Map<String, Object> banRawData, @NonNull final PunishType punishType) {
		this(UUID.fromString((String) banRawData.get("target")), UUID.fromString((String) banRawData.get("creator")), PunishDuration.of((long) banRawData.get("duration")), punishType, creation);
		validateRaWData(banRawData);
		ip((String) banRawData.get("ip"));
		removed((Boolean) banRawData.get("removed"));
	}

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
		result.put("ip", ip().orElse("unknown"));
		result.put("removed", removed());

		return result;
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods that might be overridden
	// ----------------------------------------------------------------------------------------------------

	public void create() {
		if (!PUNISH_PROVIDER.handlePunishEvent(this)) {
			return;
		}

		try {
			STORAGE_PROVIDER.savePunish(this);

			PUNISH_PROVIDER.broadCastPunishMessage(this, isSilent, isSuperSilent);
		} catch (final Throwable throwable) {

			EXCEPTION_HANDLER.saveError(throwable,
				"org.mineacademy.core.Punish.create()",
				"Failed to create punish!",
				"Data: " + toString()
			);

			//Sending messages

			PLAYER_PROVIDER.sendIfOnline(creator,
				"&cException while creating Punish!",
				"Please check your console."
			);
		}
	}
}
