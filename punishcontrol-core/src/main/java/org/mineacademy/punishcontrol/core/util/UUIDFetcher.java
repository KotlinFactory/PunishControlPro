package org.mineacademy.punishcontrol.core.util;


import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.UUID;


/**
 * Way more reliable UUIDFetcher implementation than
 * the default one.
 * <p>
 * A bit slower as using the mojang api
 * but doesn't have a rate-limit.
 */
@UtilityClass
public class UUIDFetcher {
	private final String urlString = "https://mcuuid.net/?q=";

	public String getName(final UUID uuid) {
		for (final String line : readStringArrayFromUrl()) {
			if (!line.startsWith("<h3>")) {
				continue;
			}

			return line.replace("<h3>", "")
				.replace("</h3>",
					"");
		}

//		throw new LightningValidationException("Can't find Name for '" + uuid + "'");
		return null;
	}

	@SneakyThrows
	public UUID getUUID(final String name) {
		for (final String line : readStringArrayFromUrl()) {
			if (
				!line.startsWith(
					"<td><input type=\"text\" class=\"form-control\" onclick=\"this.select();\" readonly=\"readonly\" value=\"")) {
				continue;
			}

			final String[] parts =
				line.split("\\s");

			return UUID.fromString(
				parts[5].replace("value=",
					"")
					.replace("\"",
						""));
		}

//		throw new LightningValidationException("Can't find UUID for '" + name + "'");
		return null;
	}

	private String[] readStringArrayFromUrl() {
		try {
			final URLConnection urlConnection = new URL(urlString).openConnection();

			urlConnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

			@Cleanup final InputStream inputStream = urlConnection.getInputStream();
			return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next().split("\n");
		} catch (final IOException ex) {

			if (ex instanceof UnknownHostException) {
				System.err.println("Wasn't able to fetch uuid/name. We are offline.");
			} else {
				ex.printStackTrace();
			}

			return new String[0];
		}
	}
}


