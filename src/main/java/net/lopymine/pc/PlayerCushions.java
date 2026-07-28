package net.lopymine.pc;

import net.lopymine.mossylib.logger.MossyLogger;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;

public class PlayerCushions {

	public static final String MOD_NAME = /*$ mod_name*/ "Player Cushions";
	public static final String MOD_ID = /*$ mod_id*/ "player_cushions";
	public static final MossyLogger LOGGER = new MossyLogger(MOD_NAME);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static Identifier getDataTextureId(String path) {
		return id("player_cushions/textures/" + path);
	}

	public static MutableComponent text(String path, Object... args) {
		return Component.literal(Component.translatable(String.format("%s.%s", MOD_ID, path), args).getString().replace('&', '§'));
	}

	public static void onInitialize() {
		LOGGER.info("{} Initialized", MOD_NAME);
	}
}