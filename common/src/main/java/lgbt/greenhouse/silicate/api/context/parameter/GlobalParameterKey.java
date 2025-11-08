package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;

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
	public String toString() {
		return "GlobalParameterKey<" + this.type + ">(" + this.name + ")";
	}

	@Override
	public ParameterScope getScope() {
		return ParameterScope.GLOBAL;
	}

	@Override
	public ResourceLocation getId() {
		return this.name;
	}

	@Override
	public ValueType<T> getTypeStatic() {
		return this.type;
	}
}
