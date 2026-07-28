package net.lopymine.pc.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.EnumMap;
import net.lopymine.pc.data.*;
import net.minecraft.client.renderer.entity.CushionRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.decoration.Cushion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CushionRenderer.class)
public class CushionRendererMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/EnumMap;get(Ljava/lang/Object;)Ljava/lang/Object;"), method = "extractRenderState(Lnet/minecraft/world/entity/decoration/Cushion;Lnet/minecraft/client/renderer/entity/state/CushionRenderState;F)V")
	private Object wrapOperation(EnumMap<?, ?> instance, Object key, Operation<Object> original, @Local(argsOnly = true) Cushion cushion) {
		Component customName = cushion.getCustomName();
		if (customName != null) {
			PlayerSkinData data = PlayerCushionsDataManager.getData(customName.getString());
			if (data != null && data.getState() == LoadingState.DOWNLOADED) {
				Identifier skinId = data.getSkinId();
				if (skinId != null) {
					return skinId;
				}
			}
		}
		return original.call(instance, key);
	}

}
