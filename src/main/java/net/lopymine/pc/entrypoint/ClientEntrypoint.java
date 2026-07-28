package net.lopymine.pc.entrypoint;

//? if fabric {

import net.fabricmc.api.ClientModInitializer;
import net.lopymine.pc.client.PlayerCushionsClient;

public class ClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		PlayerCushionsClient.onInitializeClient();
	}
}

//?} elif neoforge {

/*import net.lopymine.mtd.PlayerCushions;
import net.lopymine.mtd.client.PlayerCushionsClient;
import net.lopymine.mtd.modmenu.ModMenuIntegration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = PlayerCushions.MOD_ID, dist = Dist.CLIENT)
public class ClientEntrypoint {

	public ClientEntrypoint(ModContainer container) {
		PlayerCushionsClient.onInitializeClient();
		new ModMenuIntegration().register(container);
	}

}

*///?}
