package lgbt.greenhouse.silicate.api.context.parameter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lgbt.greenhouse.silicate.Silicate;
import net.minecraft.resources.ResourceLocation;

public record ParameterTemplate(ResourceLocation id) {
	public static final Codec<ParameterTemplate> CODEC = Codec.STRING
			.comapFlatMap(string -> {
				if (string.startsWith("${") && string.endsWith("}")) {
					return DataResult.success(Silicate.parseId(string.splitWithDelimiters("(\\${|})", 2)[1]));
				} else {
					return DataResult.error(() -> "no template");
				}
			}, template -> "${" + template.toString() + "}")
			.xmap(ParameterTemplate::new, ParameterTemplate::id);
}
