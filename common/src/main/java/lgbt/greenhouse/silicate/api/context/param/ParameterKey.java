package lgbt.greenhouse.silicate.api.context.param;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;

public interface ParameterKey<T> {
	ParameterScope scope();

	ResourceLocation getId();

	ValueType<T> getType();
}
