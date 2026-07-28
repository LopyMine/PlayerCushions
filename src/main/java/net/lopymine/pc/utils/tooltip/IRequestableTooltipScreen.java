package net.lopymine.pc.utils.tooltip;

import org.jetbrains.annotations.Nullable;

public interface IRequestableTooltipScreen {

	void playerCushions$requestTooltip(@Nullable TooltipRequest tooltipRequest);

	@Nullable
	TooltipRequest playerCushions$getCurrentRequest();

}
