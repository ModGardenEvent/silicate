package lgbt.greenhouse.silicate.api.context.param;

import net.minecraft.resources.ResourceLocation;

public interface ParameterKey<T> {
	ParameterScope scope();

	ResourceLocation getId();

	Class<T> getType();
}
