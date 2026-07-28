package net.lopymine.pc.cache;

import net.lopymine.pc.client.PlayerCushionsClient;
import net.lopymine.pc.config.cache.KnownPlayerUUIDsConfig;

public class KnownPlayerUUIDsConfigManager {

	private static boolean requestedSave = false;

	public static void start() {
		Thread thread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(5000);
					if (!requestedSave) {
						continue;
					}
					KnownPlayerUUIDsConfig config = KnownPlayerUUIDsConfig.getInstance();
					if (!config.isDirty()) {
						continue;
					}
					config.save();
					config.setDirty(false);
					requestedSave = false;
				} catch (Exception e) {
					PlayerCushionsClient.LOGGER.error("Failed to save config:", e);
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public static void save() {
		requestedSave = true;
	}

}
