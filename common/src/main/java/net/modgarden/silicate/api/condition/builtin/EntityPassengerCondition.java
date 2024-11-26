package net.modgarden.silicate.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modgarden.silicate.api.condition.GameConditionType;
import net.modgarden.silicate.api.condition.GameConditionTypes;
import net.modgarden.silicate.api.condition.TypedGameCondition;
import net.modgarden.silicate.api.context.GameContext;
import net.modgarden.silicate.api.context.param.ContextParamMap;
import net.modgarden.silicate.api.context.param.ContextParamType;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Optional;

/**
 * A condition that tests {@link #condition} with the passengers of {@link #paramType}.
 * @param paramType The parameter type that has a passenger.
 * @param condition The game condition to check against.
 * @param matchAll Whether to check if all passengers match or if any match.
 */
public record EntityPassengerCondition(
		ContextParamType<Entity> paramType,
		TypedGameCondition<?, Entity> condition,
		boolean matchAll
) implements TypedGameCondition<EntityPassengerCondition, Entity> {
	public static final MapCodec<EntityPassengerCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.getCodec(Entity.class)
					.fieldOf("param_type")
					.forGetter(EntityPassengerCondition::paramType),
			TypedGameCondition.getTypedCodec(Entity.class)
					.fieldOf("condition")
					.forGetter(EntityPassengerCondition::condition),
			Codec.BOOL
					.optionalFieldOf("matchAll")
					.forGetter(condition -> Optional.of(condition.matchAll()))
	).apply(instance, EntityPassengerCondition::of));

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static EntityPassengerCondition of(ContextParamType<Entity> paramType, TypedGameCondition<?, Entity> condition, Optional<Boolean> matchAll) {
		return new EntityPassengerCondition(
				paramType,
				condition,
				matchAll.orElse(false)
		);
	}

	@Override
	public boolean test(GameContext oldContext) {
		List<Entity> passengers = oldContext.getParam(paramType).getPassengers();
		ContextParamMap oldParamMap = oldContext.getParams();
		ContextParamMap.Mutable paramMap = ContextParamMap.Mutable.of(oldParamMap);
		if (matchAll) {
			return !passengers.isEmpty() && passengers.stream().allMatch(passenger -> testPassenger(oldContext, passenger, paramMap));
		} else {
			return passengers.stream().anyMatch(passenger -> testPassenger(oldContext, passenger, paramMap));
		}
	}

	private boolean testPassenger(GameContext oldContext, Entity passenger, ContextParamMap.Mutable paramMap) {
		paramMap.set(condition.getParamType(), passenger);
		GameContext context = GameContext.of(oldContext.getLevel(), paramMap);
		return condition.test(context);
	}

	@Override
	public MapCodec<EntityPassengerCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<EntityPassengerCondition> getType() {
		return GameConditionTypes.ENTITY_PASSENGER;
	}

	@Override
	public ContextParamType<Entity> getParamType() {
		return paramType;
	}
}
