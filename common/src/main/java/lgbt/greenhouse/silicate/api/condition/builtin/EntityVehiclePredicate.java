package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.PredicateType;
import lgbt.greenhouse.silicate.api.condition.PredicateTypes;
import lgbt.greenhouse.silicate.api.condition.MaybeTypedPredicate;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ContextParamMap;
import lgbt.greenhouse.silicate.api.context.param.ContextParamType;
import lgbt.greenhouse.silicate.api.context.param.ContextParamTypes;

/**
 * A condition that tests {@link #condition} with the vehicle of {@link #paramType}.
 * @param paramType The parameter type that has a vehicle.
 * @param condition The game condition to check against.
 */
public record EntityVehiclePredicate(
		ContextParamType<Entity> paramType,
		MaybeTypedPredicate<Entity> condition
) implements TypedGamePredicate<EntityVehiclePredicate, Entity> {
	public static final MapCodec<EntityVehiclePredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.getCodec(Entity.class)
					.fieldOf("param_type")
					.forGetter(EntityVehiclePredicate::paramType),
			TypedGamePredicate.getMaybeTypedCodec(Entity.class)
					.fieldOf("condition")
					.forGetter(EntityVehiclePredicate::condition)
	).apply(instance, EntityVehiclePredicate::new));

	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(paramType);
		if (entity.getVehicle() == null) {
			return false;
		} else {
			ContextParamMap oldParamMap = oldContext.getParams();
			ContextParamMap.Mutable paramMap = ContextParamMap.Mutable.of(oldParamMap);
			return testVehicle(oldContext.getLevel(), entity.getVehicle(), paramMap);
		}
	}

	private boolean testVehicle(Level level, Entity vehicle, ContextParamMap.Mutable paramMap) {
		paramMap.set(ContextParamTypes.VEHICLE_ENTITY, vehicle);
		GameContext context = GameContext.of(level, paramMap);
		return condition.test(context);
	}

	@Override
	public MapCodec<EntityVehiclePredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<EntityVehiclePredicate> getType() {
		return PredicateTypes.ENTITY_VEHICLE;
	}

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}
