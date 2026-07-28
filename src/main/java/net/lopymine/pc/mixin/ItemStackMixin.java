package net.lopymine.pc.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.lopymine.pc.PlayerCushions;
import net.lopymine.pc.config.PlayerCushionsConfig;
import net.lopymine.pc.data.*;
import net.lopymine.pc.gui.combined.CombinedTooltipData;
import net.lopymine.pc.gui.state.LoadingStateTooltipData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow public abstract boolean is(Predicate<Holder<Item>> item);

	@ModifyReturnValue(at = @At("RETURN"), method = "getTooltipImage")
	private Optional<TooltipComponent> getTooltipData(Optional<TooltipComponent> original) {
		if (!PlayerCushionsConfig.getInstance().isModEnabled() || !this.is((holder) -> holder.is(ItemTags.CUSHIONS))) {
			return original;
		}

		ItemStack itemStack = (ItemStack) (Object) this;
		Component customName = itemStack.getCustomName();
		if (customName == null) {
			return original;
		}

		PlayerSkinData data = PlayerCushionsDataManager.getData(customName.getString());
		if (data == null) {
			return original;
		}

		LoadingState state = data.getState();
		ClientTooltipComponent component = ClientTooltipComponent.create(PlayerCushions.text("text.status").append(state.getText()).getVisualOrderText());

		List<ClientTooltipComponent> list = new ArrayList<>();
		list.add(component);
		original.ifPresent(tooltipComponent -> list.add(ClientTooltipComponent.create(tooltipComponent)));

		return Optional.of(new CombinedTooltipData(list));
	}

}
