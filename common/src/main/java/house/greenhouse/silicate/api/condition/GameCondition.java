package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import house.greenhouse.silicate.api.SilicateRegistries;
import house.greenhouse.silicate.api.context.GameContext;

import java.util.function.Predicate;

/**
 * A distribution-agnostic contextual {@link Predicate} used to determine game behavior.
 * <br>
 * This is the main type in Silicate.
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