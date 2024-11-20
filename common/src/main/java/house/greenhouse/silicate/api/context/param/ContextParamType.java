package house.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused") // use generic as marker
public record ContextParamType<T>(ResourceLocation name) {
	public static <T> Codec<ContextParamType<T>> getCodec() {
		return ResourceLocation.CODEC
				.xmap(ContextParamType::new, ContextParamType::name);
	}

	@Override
	public String toString() {
		return "ContextParamType<" + this.name() + ">";
	}
}