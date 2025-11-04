package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public record LocalParameterKey<T>(
		@ApiStatus.Internal ResourceLocation name,
		@ApiStatus.Internal ValueType<T> type
) implements ParameterKey<T> {
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
