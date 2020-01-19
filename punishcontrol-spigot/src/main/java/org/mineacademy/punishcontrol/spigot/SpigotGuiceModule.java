package org.mineacademy.punishcontrol.spigot;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class SpigotGuiceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(PunishControl.class).toInstance(PunishControl.getInstance());

		final Injector injector = Guice.createInjector(this);
	}
}
