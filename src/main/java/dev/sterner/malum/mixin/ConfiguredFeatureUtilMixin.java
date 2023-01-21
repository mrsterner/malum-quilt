package dev.sterner.malum.mixin;

import dev.sterner.malum.common.registry.MalumConfiguredFeatureRegistry;
import net.minecraft.world.gen.BootstrapContext;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.util.ConfiguredFeatureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConfiguredFeatureUtil.class)
public class ConfiguredFeatureUtilMixin {

	@Inject(method = "bootstrap", at = @At("TAIL"))
	private static void malum$initConfFeatures(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext, CallbackInfo ci) {
		MalumConfiguredFeatureRegistry.bootstrap(bootstrapContext);
	}
}
