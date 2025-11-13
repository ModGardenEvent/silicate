package lgbt.greenhouse.silicate.api.predicate.minecraft;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;

/**
 * A condition that tests {@link #condition} with the vehicle of {@link #entity}.
 * @param entity The vehicle.
 * @param condition The game condition to check against.
 */
public record EntityHasVehiclePredicate(
		ParameterKey<Entity> entity,
		Deferred<Holder<GamePredicate<?>>> condition
) implements GamePredicate<EntityHasVehiclePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		Entity entity = ctx.getParameter(this.entity);
		if (entity.getVehicle() == null) {
			return false;
		} else {
			ctx.pushParameterMap();
			ParameterMap parameterMap = ctx.getParameterMap();
			boolean result = testVehicle(ctx, entity.getVehicle(), parameterMap);
			ctx.popParameterMap();
			return result;
		}
	}

	private boolean testVehicle(GameContext ctx, Entity vehicle, ParameterMap parameterMap) {
		parameterMap.set(GlobalParameterKeys.VEHICLE_ENTITY, vehicle);
		return condition.get(ctx).value().test(ctx);
	}

	@Override
	public GamePredicate.Type<EntityHasVehiclePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_HAS_VEHICLE;
	}

	public static final class Type extends GamePredicate.Type<EntityHasVehiclePredicate> {
		@Override
		protected MapCodec<EntityHasVehiclePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityHasVehiclePredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityHasVehiclePredicate::entity
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityHasVehiclePredicate::condition
					)
					.build();
		}
	}
}
