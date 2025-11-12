package lgbt.greenhouse.silicate.api.predicate.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record Definitions(Map<ResourceLocation, DynamicValue> map) {
	public static final MapCodec<Definitions> CODEC = Codec.unboundedMap(
					ResourceLocation.CODEC,
					DynamicValue.createCodec("type", "value").codec()
			)
			.optionalFieldOf("definitions", Map.of())
			.xmap(
					Definitions::new,
					Definitions::map
			);
}
