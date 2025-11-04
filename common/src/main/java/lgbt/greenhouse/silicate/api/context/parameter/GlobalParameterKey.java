package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record GlobalParameterKey<T>(
		@ApiStatus.Internal ResourceLocation name,
		@ApiStatus.Internal ValueType<T> type
)
		implements ParameterKey<T> {
	@Override // Makes sure that values return the correct value within the context when clazz is cast.
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof GlobalParameterKey<?> globalParameterKey))
			return false;
		return globalParameterKey.getId().equals(this.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
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
