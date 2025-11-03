package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.param.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ParameterMap;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKeys;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A condition that tests {@link #condition} with the passengers of {@link #parameterKey}.
 * @param parameterKey The parameter key that has a passenger.
 * @param condition The game condition to check against.
 * @param matchAll Whether to check if all passengers match or if any match.
 */
public record EntityPassengerPredicate(
		ParameterKey<Entity> parameterKey,
		Holder<GamePredicate<?>> condition,
		boolean matchAll
) implements TypedGamePredicate<EntityPassengerPredicate, Entity> {
	@Override
	public boolean test(GameContext oldContext) {
		List<Entity> passengers = oldContext.getParam(parameterKey).getPassengers();
		ParameterMap oldParamMap = oldContext.getParams();
		ParameterMap.Mutable paramMap = ParameterMap.Mutable.of(oldParamMap);
		if (matchAll) {
			return !passengers.isEmpty() && passengers.stream().allMatch(passenger -> testPassenger(oldContext.getLevel(), passenger, paramMap));
		} else {
			return passengers.stream().anyMatch(passenger -> testPassenger(oldContext.getLevel(), passenger, paramMap));
		}
	}

	private boolean testPassenger(Level level, Entity passenger, ParameterMap.Mutable paramMap) {
		paramMap.set(GlobalParameterKeys.PASSENGER_ENTITY, passenger);
		GameContext context = GameContext.of(level, paramMap);
		return condition.value().test(context);
	}

	@Override
	public @NotNull GamePredicate.Type<EntityPassengerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_PASSENGER;
	}

	public static final class Type extends GamePredicate.Type<EntityPassengerPredicate> {
		@Override
		protected MapCodec<EntityPassengerPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityPassengerPredicate.class))
					.withField(
							"player",
							SilicateValueTypes.ENTITY
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityPassengerPredicate::condition
					)
					.withOptionalValue(
							"matchAll",
							SilicatePrimitives.BOOLEAN,
							EntityPassengerPredicate::matchAll,
							false
					)
					.build();
		}
	}
}
