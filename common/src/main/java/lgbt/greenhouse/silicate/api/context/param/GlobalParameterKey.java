package lgbt.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
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
	/**
	 * The generic {@link Codec} for any {@link GlobalParameterKey}.
	 * <br>
	 * If implementing parameter types programmatically, use {@link #getCodec(ValueType)} instead!
	 * @see #getCodec(ValueType)
	 */
	public static final Codec<GlobalParameterKey<?>> ANY_CODEC = ResourceLocation.CODEC
			.comapFlatMap(
					GlobalParameterKey::validateParamType,
					GlobalParameterKey::getId
			);

	/**
	 * Get this parameter type's codec.
	 *
	 * @param <T> The value type of the parameter type.
	 * @param type The class of the type in {@link T}.
	 * @return The parameter type's codec.
	 */
	@Deprecated
	@SuppressWarnings({"unchecked"})// if the types are equal, we can safely cast
	public static <T> Codec<GlobalParameterKey<T>> getCodec(ValueType<T> type) {
		// Spooky!
		// edit: less spooky
		return ResourceLocation.CODEC
				.flatXmap(name -> {
					GlobalParameterKey<?> paramType = SilicateBuiltInRegistries.GLOBAL_PARAMETER_KEY.getValue(name);
					if (paramType != null) {
						if (type.equals(paramType.type()))
							return DataResult.success((GlobalParameterKey<T>) paramType);
						if (type.isAssignableFrom(paramType.type())) // If the paramKey extends the specified class...
							return DataResult.success(new GlobalParameterKey<>(name, type)); // Create a new ContextParamType that may be used in place of the old one.
						return DataResult.error(() -> paramType + " is not of the proper parameter"); // Remapping is the reason why we don't tell which class.
					}
					return DataResult.error(() -> "Context Param Type '" + name + "' does not exist");
				}, paramType -> DataResult.success(paramType.getId()));
	}

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
