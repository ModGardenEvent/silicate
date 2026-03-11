package lgbt.greenhouse.silicate.api.context.parameter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lgbt.greenhouse.silicate.impl.SilicateConstants;
import net.minecraft.resources.Identifier;

public record ParameterTemplate(Identifier id) {
	public static final Codec<ParameterTemplate> CODEC = Codec.STRING
			.comapFlatMap(string -> {
				if (string.startsWith("${") && string.endsWith("}")) {
					String[] split = string.splitWithDelimiters("(\\$\\{|\\})", 3);
					return DataResult.success(SilicateConstants.parseId(split[2]));
				} else {
					return DataResult.error(() -> "no template");
				}
			}, template -> "${" + template + "}")
			.xmap(ParameterTemplate::new, ParameterTemplate::id);
}
