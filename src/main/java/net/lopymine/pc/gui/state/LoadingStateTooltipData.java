package net.lopymine.pc.gui.state;

import net.lopymine.pc.data.LoadingState;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record LoadingStateTooltipData(LoadingState state) implements TooltipComponent {

}
