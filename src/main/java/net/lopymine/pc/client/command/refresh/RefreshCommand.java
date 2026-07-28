//~ client_fabric_commands

package net.lopymine.pc.client.command.refresh;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Map;
import java.util.concurrent.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.lopymine.mossylib.utils.CommandUtils;
import net.lopymine.mossylib.utils.command.CommandTextBuilder;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.api.MojangAPI;
import net.lopymine.pc.client.PlayerCushionsClient;
import net.lopymine.pc.data.PlayerCushionsDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import static net.lopymine.mossylib.utils.CommandUtils.literal;
import static net.lopymine.mossylib.utils.CommandUtils.argument;

public class RefreshCommand {

	private static final Map<String, CompletableFuture<Float>> RELOADING_FUTURES = new ConcurrentHashMap<>();
	@Nullable
	private static CompletableFuture<Float> RELOADING_ALL_FUTURE = null;

	public static LiteralArgumentBuilder<FabricClientCommandSource> getInstance() {
		return literal("refresh")
				.then(literal("all")
						.executes(RefreshCommand::reloadAll))
				.then(literal("player")
						.then(argument("nickname", StringArgumentType.word())
								.suggests((context, builder) ->
										SharedSuggestionProvider.suggest(PlayerCushionsDataManager.getAllLoadedKeys(), builder))
								.executes(RefreshCommand::reloadForPlayer)
						));
	}

	private static int reloadAll(CommandContext<FabricClientCommandSource> context) {
		if (RELOADING_ALL_FUTURE != null) {
			return 0;
		}

		Component startFeedback = CommandTextBuilder.startBuilder("command.refresh.all.start", PlayerCushions.MOD_ID).build();
		CommandUtils.sendMessage(startFeedback);

		RELOADING_ALL_FUTURE = PlayerCushionsDataManager.reloadData((seconds) -> {
			Component endFeedback = CommandTextBuilder.startBuilder("command.refresh.all.end", PlayerCushions.MOD_ID, seconds).build();
			Minecraft.getInstance().execute(() -> CommandUtils.sendMessage(endFeedback));
		}).whenComplete((r, e) -> {
			RELOADING_ALL_FUTURE = null;
			if (e != null) {
				PlayerCushionsClient.LOGGER.error("Failed to refresh all data: ", e);
			}
		});

		MojangAPI.useFallbackAPI = false;

		return Command.SINGLE_SUCCESS;
	}

	private static int reloadForPlayer(CommandContext<FabricClientCommandSource> context) {
		String nickname = StringArgumentType.getString(context, "nickname");

		CompletableFuture<Float> future = RELOADING_FUTURES.get(nickname);
		if (future != null) {
			return 0;
		}

		Component startFeedback = CommandTextBuilder.startBuilder("command.refresh.player.start", nickname).build();
		CommandUtils.sendMessage(startFeedback);

		CompletableFuture<Float> f = PlayerCushionsDataManager.reloadData(nickname, (seconds) -> {
			Component endFeedback = CommandTextBuilder.startBuilder("command.refresh.player.end", nickname, seconds).build();
			Minecraft.getInstance().execute(() -> CommandUtils.sendMessage(endFeedback));
		});

		if (f != null) {
			CompletableFuture<Float> fc = f.whenComplete((r, e) -> {
				RELOADING_FUTURES.remove(nickname);
				if (e != null) {
					PlayerCushionsClient.LOGGER.error("Failed to refresh data for \"{}\": ", nickname, e);
				}
			});
			RELOADING_FUTURES.put(nickname, fc);
		}

		return Command.SINGLE_SUCCESS;
	}
}
