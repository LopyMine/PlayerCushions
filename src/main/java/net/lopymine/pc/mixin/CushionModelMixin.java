package net.lopymine.pc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.object.cushion.CushionModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CushionModel.class)
public class CushionModelMixin {

	@Inject(at = @At("RETURN"), method = "createBodyLayer")
	private static void addSecondLayer(CallbackInfoReturnable<LayerDefinition> cir, @Local MeshDefinition definition) {
		PartDefinition root = definition.getRoot();
		root.addOrReplaceChild("cushion_second_layer_from_players_cushion_mod", CubeListBuilder.create().texOffs(0, 20).addBox(-31.0F, -4.0F, -1.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(-0.003F)), PartPose.offset(23.0F, 4.0F, -7.0F));
	}

}
