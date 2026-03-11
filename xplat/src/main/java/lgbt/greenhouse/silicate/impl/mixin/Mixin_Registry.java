package lgbt.greenhouse.silicate.impl.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import lgbt.greenhouse.silicate.impl.SilicateConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
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
					target = "Lnet/minecraft/resources/Identifier;CODEC:Lcom/mojang/serialization/Codec;"
			)
	)
	private Codec<Identifier> silicate$overrideDefaultNamespace(Codec<Identifier> original) {
		if (!this.key().identifier().getNamespace().equals(SilicateConstants.MOD_ID)) return original;

		Function<Identifier, Identifier> namespaceTransformer = id -> {
			if (id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
				return SilicateConstants.id(id.getPath());
			} else {
				return id;
			}
		};
		return original.xmap(namespaceTransformer, namespaceTransformer);
	}
}
