//~ client_fabric_commands

package net.lopymine.pc.client.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.lopymine.pc.client.command.refresh.RefreshCommand;
import static net.lopymine.mossylib.utils.CommandUtils.literal;

public class PlayerCushionsCommandManager {

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(literal("player-cushions")
				.then(RefreshCommand.getInstance()));
	}
}
