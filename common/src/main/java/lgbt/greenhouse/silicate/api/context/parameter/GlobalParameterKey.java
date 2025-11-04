package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class GlobalParameterKey<T>
		implements ParameterKey<T> {
	private final ResourceLocation name;
	private final ValueType<T> type;

	public GlobalParameterKey(
			ResourceLocation name,
			ValueType<T> type
	) {
		this.name = name;
		this.type = type;
	}

	@Override
	public @NotNull String toString() {
		return "GlobalParameterKey<" + this.type + ">(" + this.name + ")";
	}

	@Override
	public ParameterScope scope() {
		return ParameterScope.GLOBAL;
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
