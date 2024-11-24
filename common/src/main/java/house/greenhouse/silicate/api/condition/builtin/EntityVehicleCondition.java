package house.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import house.greenhouse.silicate.api.condition.GameConditionType;
import house.greenhouse.silicate.api.condition.GameConditionTypes;
import house.greenhouse.silicate.api.condition.TypedGameCondition;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamMap;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import net.minecraft.world.entity.Entity;

/**
 * A condition that tests {@link #condition} with the vehicle of {@link #paramType}.
 * @param paramType The parameter type that has a vehicle.
 * @param condition The game condition to check against.
 */
public record EntityVehicleCondition(
		ContextParamType<Entity> paramType,
		TypedGameCondition<?, Entity> condition
) implements TypedGameCondition<EntityVehicleCondition, Entity> {
	public static final MapCodec<EntityVehicleCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.getCodec(Entity.class)
					.fieldOf("param_type")
					.forGetter(EntityVehicleCondition::paramType),
			TypedGameCondition.getTypedCodec(Entity.class)
					.fieldOf("condition")
					.forGetter(EntityVehicleCondition::condition)
	).apply(instance, EntityVehicleCondition::new));

	@Override
	public boolean test(GameContext oldContext) {
		Entity entity = oldContext.getParam(paramType);
		if (entity.getVehicle() == null) {
			return false;
		} else {
			ContextParamMap oldParamMap = oldContext.getParams();
			ContextParamMap.Mutable paramMap = ContextParamMap.Mutable.of(oldParamMap);
			paramMap.set(condition.getParamType(), entity.getVehicle());
			GameContext context = GameContext.of(oldContext.getLevel(), paramMap);
			return condition.test(context);
		}
	}

	@Override
	public MapCodec<EntityVehicleCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<EntityVehicleCondition> getType() {
		return GameConditionTypes.ENTITY_VEHICLE;
	}

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}
