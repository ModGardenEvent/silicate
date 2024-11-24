package house.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import house.greenhouse.silicate.api.SilicateRegistries;
import house.greenhouse.silicate.api.condition.GameCondition;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import net.minecraft.resources.ResourceLocation;
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
	 * @implNote Although potentially unsafe, this shouldn't result in any Mad Gadget situations given that the actual non-erased type at runtime will be checked. See the implementation of {@link TypedGameCondition#validate(GameCondition, Class)} for further information.
	 * @see TypedGameCondition#validate(GameCondition, Class)
	 */
	@SuppressWarnings("JavadocReference") // We want people to be able to verify the underlying implementation.
	public static <T> Codec<ContextParamType<T>> getCodec(Class<T> clazz) {
		// Spooky!
		return ResourceLocation.CODEC
				.xmap(name -> new ContextParamType<>(name, clazz), ContextParamType::name);
	}
	
	@Override
	public String toString() {
		return "ContextParamType<" + this.name() + ">";
	}
	
	private static @NotNull DataResult<? extends ContextParamType<?>> validateParamType(ResourceLocation id) {
		try {
			return DataResult.success(Objects.requireNonNull(
					SilicateRegistries.CONTEXT_PARAM_TYPE.get(id),
					"ContextParamType (" + id + ") is unregistered"
			));
		} catch (NullPointerException e) {
			return DataResult.error(e::getMessage);
		}
	}
}