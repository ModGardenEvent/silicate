package lgbt.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record GlobalParameterKey<T>(ResourceLocation name, Class<T> clazz)
		implements ParameterKey<T> {
	/**
	 * The generic {@link Codec} for any {@link GlobalParameterKey}.
	 * <br>
	 * If implementing parameter types programmatically, use {@link #getCodec(Class)} instead!
	 * @see #getCodec(Class)
	 */
	public static final Codec<GlobalParameterKey<?>> ANY_CODEC = ResourceLocation.CODEC
			.comapFlatMap(
					GlobalParameterKey::validateParamType,
					GlobalParameterKey::name
			);

	/**
	 * Get this parameter type's codec.
	 * @param clazz The class of the type in {@link T}.
	 * @return The parameter type's codec.
	 * @param <T> The value type of the parameter type.
	 * @implNote Although potentially unsafe, this shouldn't result in any Mad Gadget situations given that the actual non-erased type at runtime will be checked. See the implementation of {@link TypedGamePredicate#validate(Holder, Class)} for further information.
	 * @see TypedGamePredicate#validate(Holder, Class)
	 */
	@SuppressWarnings({"JavadocReference", "unchecked"})// We want people to be able to verify the underlying implementation.
	public static <T> Codec<GlobalParameterKey<T>> getCodec(Class<T> clazz) {
		// Spooky!
		return ResourceLocation.CODEC
				.flatXmap(name -> {
					GlobalParameterKey<?> paramType = SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE.getValue(name);
					if (paramType != null) {
						if (clazz.equals(paramType.clazz()))
							return DataResult.success((GlobalParameterKey<T>)paramType);
						if (clazz.isAssignableFrom(paramType.clazz())) // If the paramType extends the specified class...
							return DataResult.success(new GlobalParameterKey<>(name, clazz)); // Create a new ContextParamType that may be used in place of the old one.
						return DataResult.error(() -> paramType + " is not of the proper parameter"); // Remapping is the reason why we don't tell which class.
					}
					return DataResult.error(() -> "Context Param Type '" + name + "' does not exist");
				}, paramType -> DataResult.success(paramType.name()));
	}

	@Override // Makes sure that values return the correct value within the context when clazz is cast.
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof GlobalParameterKey<?> globalParameterKey))
			return false;
		return globalParameterKey.name().equals(this.name());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name());
	}

	@Override
	public @NotNull String toString() {
		return "ContextParamType<" + this.name() + ">";
	}

	private static @NotNull DataResult<? extends GlobalParameterKey<?>> validateParamType(ResourceLocation id) {
		try {
			return DataResult.success(Objects.requireNonNull(
					SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE.getValue(id),
					"ContextParamType (" + id + ") is unregistered"
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
	public Class<T> getType() {
		return this.clazz;
	}
}
