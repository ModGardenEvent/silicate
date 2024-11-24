package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import house.greenhouse.silicate.api.SilicateRegistries;
import house.greenhouse.silicate.api.condition.builtin.EntityPassengerCondition;
import house.greenhouse.silicate.api.condition.builtin.EntityVehicleCondition;
import house.greenhouse.silicate.api.context.GameContext;

import java.util.function.Predicate;

/**
 * A distribution-agnostic contextual {@link Predicate} used to determine game behavior.
 * <br>
 * This is the main type in Silicate.
 * <h1>{@link TypedGameCondition}</h1>
 * If a condition has a parameter type, it is recommended to implement {@link TypedGameCondition} so that conditions that chain it can automatically determine their parameter type.
 * @see EntityPassengerCondition
 * @see EntityVehicleCondition
 * @see TypedGameCondition
 */
public interface GameCondition<T extends GameCondition<T>> extends Predicate<GameContext> {
	Codec<GameCondition<?>> CODEC = SilicateRegistries.GAME_CONDITION_TYPE.byNameCodec()
			.dispatch("type", GameCondition::getType, GameConditionType::codec);
	
	@Override
	boolean test(GameContext context);
	
	/**
	 * @return the codec responsible for condition configuration.
	 */
	MapCodec<T> getCodec();
	
	GameConditionType<T> getType();
}