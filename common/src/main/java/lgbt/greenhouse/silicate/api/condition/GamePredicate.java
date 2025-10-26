package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityPassengerPredicate;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityVehiclePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;

import java.util.function.Predicate;

/**
 * A distribution-agnostic contextual {@link Predicate} used to determine game behavior.
 * <br>
 * This is the main type in Silicate.
 * <h2>{@link TypedGamePredicate}</h2>
 * If a predicate has a parameter type, it is recommended to implement {@link TypedGamePredicate} so that conditions that chain it can automatically determine their parameter type.
 * @see EntityPassengerPredicate
 * @see EntityVehiclePredicate
 * @see TypedGamePredicate
 * @see MaybeTypedPredicate
 */
public interface GamePredicate<T extends GamePredicate<T>> extends Predicate<GameContext> {
	Codec<GamePredicate<?>> TYPED_CODEC = SilicateBuiltInRegistries.PREDICATE.byNameCodec()
			.dispatch("predicate", GamePredicate::getType, PredicateType::codec);
	Codec<Holder<GamePredicate<?>>> CODEC = RegistryFileCodec.create(SilicateRegistries.CONDITION_TEMPLATE, TYPED_CODEC);

	@Override
	boolean test(GameContext context);

	/**
	 * @return the codec responsible for condition configuration.
	 */
	MapCodec<T> getCodec();

	PredicateType<T> getType();

	/**
	 * A helper function for retrieving condition templates.
	 * @param location The condition template's {@link ResourceLocation}.
	 * @return The condition template.
	 */
	static Holder<GamePredicate<?>> getTemplate(ResourceLocation location) {
		return SilicateBuiltInRegistries
				.lookupOrThrow(SilicateRegistries.CONDITION_TEMPLATE)
				.getOrThrow(
						ResourceKey.create(
								SilicateRegistries.CONDITION_TEMPLATE,
								location
						)
				);
	}
}
