package lgbt.greenhouse.silicate.api.context.param;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;

public record LocalParameterKey<T>(ResourceLocation name, ValueType<T> type) implements ParameterKey<T> {
	@Override
	public ParameterScope scope() {
		return ParameterScope.LOCAL;
	}

	@Override
	public ResourceLocation getId() {
		return this.name;
	}

	@Override
	public ValueType<T> getType() {
		return this.type;
	}
}
