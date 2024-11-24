package house.greenhouse.silicate.api.context.param;

import com.mojang.serialization.Codec;
import house.greenhouse.silicate.api.condition.GameCondition;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused") // use generic as marker
public record ContextParamType<T>(ResourceLocation name, Class<T> clazz) {
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
}