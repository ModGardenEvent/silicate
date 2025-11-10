package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.Deferred;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;

import java.util.List;

/**
 * A condition that tests {@link #condition} with the passengers of {@link #entity}.
 * @param entity The parameter key that has a passenger.
 * @param condition The game condition to check against.
 * @param matchAll Whether to check if all passengers match or if any match.
 */
public record EntityPassengerPredicate(
		ParameterKey<Entity> entity,
		Deferred<Holder<GamePredicate<?>>> condition,
		Deferred<Boolean> matchAll
) implements GamePredicate<EntityPassengerPredicate> {
	@Override
	public boolean test(GameContext ctx) {
		List<Entity> passengers = ctx.getParameter(this.entity).getPassengers();
		ParameterMap parameterMap = ctx.getParameterMap();
		if (matchAll.get(ctx)) {
			return !passengers.isEmpty() && passengers.stream()
					.allMatch(passenger -> testPassenger(ctx, passenger, parameterMap));
		} else {
			return passengers.stream()
					.anyMatch(passenger -> testPassenger(ctx, passenger, parameterMap));
		}
	}

	private boolean testPassenger(GameContext ctx, Entity passenger, ParameterMap parameterMap) {
		parameterMap.set(GlobalParameterKeys.PASSENGER_ENTITY, passenger);
		GameContext context = GameContext.of(ctx.getLevel(), parameterMap);
		return condition.get(ctx).value().test(context);
	}

	@Override
	public GamePredicate.Type<EntityPassengerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_PASSENGER;
	}

	public static final class Type extends GamePredicate.Type<EntityPassengerPredicate> {
		@Override
		protected MapCodec<EntityPassengerPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityPassengerPredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityPassengerPredicate::entity
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityPassengerPredicate::condition
					)
					.withDefaultOptionalValue(
							"matchAll",
							SilicatePrimitives.BOOLEAN,
							EntityPassengerPredicate::matchAll,
							false
					)
					.build();
		}
	}
}
