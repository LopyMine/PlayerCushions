package net.lopymine.pc.client;

import net.lopymine.mossylib.loader.MossyLoader;
import net.lopymine.mossylib.logger.MossyLogger;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.cache.KnownPlayerUUIDsConfigManager;
import net.lopymine.pc.client.command.PlayerCushionsCommandManager;
import net.lopymine.pc.client.event.PlayerCushionsEvents;
import net.lopymine.pc.tags.TagsSkinProviders;
import org.slf4j.*;

public class PlayerCushionsClient {

	public static MossyLogger LOGGER = PlayerCushions.LOGGER.extend("Client");

	public static void onInitializeClient() {
		LOGGER.info("{} Client Initialized", PlayerCushions.MOD_NAME);
		MossyLoader.registerCommands(PlayerCushionsCommandManager::register);
		TagsSkinProviders.register();
		PlayerCushionsEvents.register();
		KnownPlayerUUIDsConfigManager.start();
	}
}
