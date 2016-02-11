package com.lostshard.Lostshard.Intake.Modules;

import org.bukkit.OfflinePlayer;

import com.lostshard.Lostshard.Intake.Providers.OfflinePlayerProvider;
import com.sk89q.intake.parametric.AbstractModule;

public class OfflinePlayerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(OfflinePlayer.class).toProvider(new OfflinePlayerProvider());
	}

}