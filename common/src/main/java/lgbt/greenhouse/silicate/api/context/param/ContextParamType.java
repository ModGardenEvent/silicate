package lgbt.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.condition.TypedGameCondition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused") // use generic as marker
public record ContextParamType<T>(ResourceLocation name, Class<T> clazz) {
	/**
	 * The generic {@link Codec} for any {@link ContextParamType}.
	 * <br>
	 * If implementing parameter types programmatically, use {@link #getCodec(Class)} instead!
	 * @see #getCodec(Class)
	 */
	public static final Codec<ContextParamType<?>> ANY_CODEC = ResourceLocation.CODEC
			.comapFlatMap(
					ContextParamType::validateParamType,
					ContextParamType::name
			);

	/**
	 * Get this parameter type's codec.
	 * @param clazz The class of the type in {@link T}.
	 * @return The parameter type's codec.
	 * @param <T> The value type of the parameter type.
	 * @implNote Although potentially unsafe, this shouldn't result in any Mad Gadget situations given that the actual non-erased type at runtime will be checked. See the implementation of {@link TypedGameCondition#validate(Holder, Class)} for further information.
	 * @see TypedGameCondition#validate(Holder, Class)
	 */
	@SuppressWarnings({"JavadocReference", "unchecked"})// We want people to be able to verify the underlying implementation.
	public static <T> Codec<ContextParamType<T>> getCodec(Class<T> clazz) {
		// Spooky!
		return ResourceLocation.CODEC
				.flatXmap(name -> {
					ContextParamType<?> paramType = SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE.getValue(name);
					if (paramType != null) {
						if (clazz.equals(paramType.clazz()))
							return DataResult.success((ContextParamType<T>)paramType);
						if (clazz.isAssignableFrom(paramType.clazz())) // If the paramType extends the specified class...
							return DataResult.success(new ContextParamType<>(name, clazz)); // Create a new ContextParamType that may be used in place of the old one.
						return DataResult.error(() -> paramType + " is not of the proper parameter"); // Remapping is the reason why we don't tell which class.
					}
					return DataResult.error(() -> "Context Param Type '" + name + "' does not exist");
				}, paramType -> DataResult.success(paramType.name()));
	}

	@Override // Makes sure that values return the correct value within the context when clazz is cast.
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ContextParamType<?> contextParamType))
			return false;
		return contextParamType.name().equals(this.name());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name());
	}

	@Override
	public @NotNull String toString() {
		return "ContextParamType<" + this.name() + ">";
	}

	private static @NotNull DataResult<? extends ContextParamType<?>> validateParamType(ResourceLocation id) {
		try {
			return DataResult.success(Objects.requireNonNull(
					SilicateBuiltInRegistries.CONTEXT_PARAM_TYPE.getValue(id),
					"ContextParamType (" + id + ") is unregistered"
			));
		} catch (NullPointerException e) {
			return DataResult.error(e::getMessage);
		}
	}
}
