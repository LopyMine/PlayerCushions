package net.lopymine.pc.entrypoint;

//? if fabric {

import net.fabricmc.api.ModInitializer;
import net.lopymine.pc.PlayerCushions;

public class CommonEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		PlayerCushions.onInitialize();
	}
}

//?} elif neoforge {

/*import net.lopymine.mtd.PlayerCushions;
import net.neoforged.fml.common.Mod;

@Mod(PlayerCushions.MOD_ID)
public class CommonEntrypoint {

	public CommonEntrypoint() {
		PlayerCushions.onInitialize();
	}

}

*///?}
