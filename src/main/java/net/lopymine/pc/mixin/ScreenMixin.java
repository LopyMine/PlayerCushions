package net.lopymine.pc.mixin;

import net.lopymine.pc.utils.tooltip.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandler implements Renderable, IRequestableTooltipScreen {

	@Unique
	private TooltipRequest playerCushions$tooltipRequest;

	@Inject(at = @At("TAIL"), method = "extractRenderStateWithTooltipAndSubtitles")
	private void renderWithTooltip(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (this.playerCushions$tooltipRequest != null) {
			context.nextStratum();
			this.playerCushions$tooltipRequest.renderRenderState(context, mouseX, mouseY, delta);
			this.playerCushions$tooltipRequest = null;
		}
	}

	@Override
	public void playerCushions$requestTooltip(TooltipRequest tooltipRequest) {
		this.playerCushions$tooltipRequest = tooltipRequest;
	}

	@Override
	public TooltipRequest playerCushions$getCurrentRequest() {
		return this.playerCushions$tooltipRequest;
	}
}
