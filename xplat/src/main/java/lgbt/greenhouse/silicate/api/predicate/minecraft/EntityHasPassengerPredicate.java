package lgbt.greenhouse.silicate.api.predicate.minecraft;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
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
public record EntityHasPassengerPredicate(
		ParameterKey<Entity> entity,
		Deferred<Holder<GamePredicate<?>>> condition,
		Deferred<Boolean> matchAll
) implements GamePredicate<EntityHasPassengerPredicate> {
	@Override
	public boolean test(GameContext ctx) {
		ctx.pushParameterMap();
		List<Entity> passengers = ctx.getParameter(this.entity).getPassengers();
		ParameterMap parameterMap = ctx.getParameterMap();
		boolean result;
		if (matchAll.get(ctx)) {
			result = !passengers.isEmpty() && passengers.stream()
					.allMatch(passenger -> testPassenger(ctx, passenger, parameterMap));
		} else {
			result = passengers.stream()
					.anyMatch(passenger -> testPassenger(ctx, passenger, parameterMap));
		}
		ctx.popParameterMap();
		return result;
	}

	private boolean testPassenger(GameContext ctx, Entity passenger, ParameterMap parameterMap) {
		parameterMap.set(GlobalParameterKeys.PASSENGER_ENTITY, passenger);
		return condition.get(ctx).value().test(ctx);
	}

	@Override
	public GamePredicate.Type<EntityHasPassengerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_HAS_PASSENGER;
	}

	public static final class Type extends GamePredicate.Type<EntityHasPassengerPredicate> {
		@Override
		protected MapCodec<EntityHasPassengerPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityHasPassengerPredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityHasPassengerPredicate::entity
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityHasPassengerPredicate::condition
					)
					.withDefaultOptionalValue(
							"matchAll",
							SilicatePrimitives.BOOLEAN,
							EntityHasPassengerPredicate::matchAll,
							false
					)
					.build();
		}
	}
}
