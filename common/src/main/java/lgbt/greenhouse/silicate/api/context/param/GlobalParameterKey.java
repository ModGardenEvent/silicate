package lgbt.greenhouse.silicate.api.context.param;

import com.mojang.serialization.DataResult;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.resources.ResourceLocation;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
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
		return "GlobalParameterKey<" + this.name + ">";
	}

	private static @NotNull DataResult<? extends GlobalParameterKey<?>> validateParamType(ResourceLocation id) {
		try {
			return DataResult.success(Objects.requireNonNull(
					SilicateBuiltInRegistries.GLOBAL_PARAMETER_KEY.getValue(id),
					"GlobalParameterKey (" + id + ") is unregistered"
			));
		} catch (NullPointerException e) {
			return DataResult.error(e::getMessage);
		}
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
