package lgbt.greenhouse.silicate.api.predicate.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

import java.util.Map;

public record Definitions(Map<Identifier, DynamicValue> map) {
	public static final MapCodec<Definitions> CODEC = Codec.unboundedMap(
					Identifier.CODEC,
					DynamicValue.createCodec("type", "value").codec()
			)
			.optionalFieldOf("definitions", Map.of())
			.xmap(
					Definitions::new,
					Definitions::map
			);
}
