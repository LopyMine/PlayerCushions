package net.lopymine.pc.client.event;

import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.data.LoadingState;
import net.lopymine.pc.gui.combined.*;
import net.lopymine.pc.gui.state.LoadingStateTooltipData;
import net.lopymine.pc.thread.PlayerCushionsTaskExecutor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class PlayerCushionsEvents {

	public static void register() {
		registerTooltipCallbacks();
		registerLifecycleEvents();
	}

	private static void registerTooltipCallbacks() {
		ClientTooltipComponentCallback.EVENT.register((data) -> {
			if (data instanceof LoadingStateTooltipData(LoadingState state)) {
				return ClientTooltipComponent.create(PlayerCushions.text("text.status").append(state.getText()).getVisualOrderText());
			}
			if (data instanceof CombinedTooltipData(List<ClientTooltipComponent> list)) {
				return new CombinedTooltipComponent(list);
			}
			return null;
		});
	}

	private static void registerLifecycleEvents() {
		ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
			PlayerCushionsTaskExecutor.stop();
		});
	}
}
