package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;
import lgbt.greenhouse.silicate.api.SilicateRegistries;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityPassengerCondition;
import lgbt.greenhouse.silicate.api.condition.builtin.EntityVehicleCondition;
import lgbt.greenhouse.silicate.api.context.GameContext;

import java.util.function.Predicate;

/**
 * A distribution-agnostic contextual {@link Predicate} used to determine game behavior.
 * <br>
 * This is the main type in Silicate.
 * <h2>{@link TypedGameCondition}</h2>
 * If a condition has a parameter type, it is recommended to implement {@link TypedGameCondition} so that conditions that chain it can automatically determine their parameter type.
 * @see EntityPassengerCondition
 * @see EntityVehicleCondition
 * @see TypedGameCondition
 * @see MaybeTypedCondition
 */
public interface GameCondition<T extends GameCondition<T>> extends Predicate<GameContext> {
	Codec<GameCondition<?>> TYPED_CODEC = SilicateBuiltInRegistries.GAME_CONDITION_TYPE.byNameCodec()
			.dispatch("predicate", GameCondition::getType, GameConditionType::codec);
	Codec<Holder<GameCondition<?>>> CODEC = RegistryFileCodec.create(SilicateRegistries.CONDITION_TEMPLATE, TYPED_CODEC);

	@Override
	boolean test(GameContext context);

	/**
	 * @return the codec responsible for condition configuration.
	 */
	MapCodec<T> getCodec();

	GameConditionType<T> getType();

	/**
	 * A helper function for retrieving condition templates.
	 * @param location The condition template's {@link ResourceLocation}.
	 * @return The condition template.
	 */
	static Holder<GameCondition<?>> getTemplate(ResourceLocation location) {
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
