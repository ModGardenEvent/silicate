package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.meta.Deferred;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import lgbt.greenhouse.silicate.api.context.parameter.GlobalParameterKeys;

/**
 * A condition that tests {@link #condition} with the vehicle of {@link #entity}.
 * @param entity The vehicle.
 * @param condition The game condition to check against.
 */
public record EntityVehiclePredicate(
		ParameterKey<Entity> entity,
		Deferred<Holder<GamePredicate<?>>> condition
) implements GamePredicate<EntityVehiclePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		Entity entity = ctx.getParam(this.entity);
		if (entity.getVehicle() == null) {
			return false;
		} else {
			ParameterMap oldParamMap = ctx.getParams();
			ParameterMap.Mutable paramMap = ParameterMap.Mutable.of(oldParamMap);
			return testVehicle(ctx, entity.getVehicle(), paramMap);
		}
	}

	private boolean testVehicle(GameContext ctx, Entity vehicle, ParameterMap.Mutable paramMap) {
		paramMap.set(GlobalParameterKeys.VEHICLE_ENTITY, vehicle);
		GameContext context = GameContext.of(ctx.getLevel(), paramMap);
		return condition.get(ctx).value().test(context);
	}

	@Override
	public GamePredicate.Type<EntityVehiclePredicate> getType() {
		return SilicatePredicateTypes.ENTITY_VEHICLE;
	}

	public static final class Type extends GamePredicate.Type<EntityVehiclePredicate> {
		@Override
		protected MapCodec<EntityVehiclePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EntityVehiclePredicate.class))
					.withParameter(
							"entity",
							SilicateValueTypes.ENTITY,
							EntityVehiclePredicate::entity
					)
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							EntityVehiclePredicate::condition
					)
					.build();
		}
	}
}
