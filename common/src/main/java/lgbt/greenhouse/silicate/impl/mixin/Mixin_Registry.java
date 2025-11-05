package lgbt.greenhouse.silicate.impl.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.impl.Silicate;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(Registry.class)
public interface Mixin_Registry<T> {
	@Shadow
	ResourceKey<? extends Registry<T>> key();

	@ModifyExpressionValue(
			method = "referenceHolderWithLifecycle",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/resources/ResourceLocation;CODEC:Lcom/mojang/serialization/Codec;"
			)
	)
	private Codec<ResourceLocation> silicate$overrideDefaultNamespace(Codec<ResourceLocation> original) {
		if (!this.key().location().getNamespace().equals(Silicate.MOD_ID)) return original;

		Function<ResourceLocation, ResourceLocation> namespaceTransformer = id -> {
			if (id.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)) {
				return Silicate.id(id.getPath());
			} else {
				return id;
			}
		};
		return original.xmap(namespaceTransformer, namespaceTransformer);
	}
}
