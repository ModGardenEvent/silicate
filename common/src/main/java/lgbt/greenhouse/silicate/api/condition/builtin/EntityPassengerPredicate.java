package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.MaybeTypedPredicate;
import lgbt.greenhouse.silicate.api.condition.TypedGamePredicate;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.ParameterMap;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKeys;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * A condition that tests {@link #condition} with the passengers of {@link #paramType}.
 * @param paramType The parameter type that has a passenger.
 * @param condition The game condition to check against.
 * @param matchAll Whether to check if all passengers match or if any match.
 */
public record EntityPassengerPredicate(
		GlobalParameterKey<Entity> paramType,
		MaybeTypedPredicate<Entity> condition,
		boolean matchAll
) implements TypedGamePredicate<EntityPassengerPredicate, Entity> {
	public static final MapCodec<EntityPassengerPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			GlobalParameterKey.getCodec(SilicateValueTypes.ENTITY)
					.fieldOf("param_type")
					.forGetter(EntityPassengerPredicate::paramType),
			TypedGamePredicate.getMaybeTypedCodec(SilicateValueTypes.ENTITY)
					.fieldOf("condition")
					.forGetter(EntityPassengerPredicate::condition),
			Codec.BOOL
					.optionalFieldOf("matchAll")
					.forGetter(condition -> Optional.of(condition.matchAll()))
	).apply(instance, EntityPassengerPredicate::of));

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private static EntityPassengerPredicate of(GlobalParameterKey<Entity> paramType, MaybeTypedPredicate<Entity> condition, Optional<Boolean> matchAll) {
		return new EntityPassengerPredicate(
				paramType,
				condition,
				matchAll.orElse(false)
		);
	}

	@Override
	public boolean test(GameContext oldContext) {
		List<Entity> passengers = oldContext.getParam(paramType).getPassengers();
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
		return condition.test(context);
	}

	@Override
	public @NotNull MapCodec<EntityPassengerPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public @NotNull Type<EntityPassengerPredicate> getType() {
		return SilicatePredicateTypes.ENTITY_PASSENGER;
	}

	@Override
	public GlobalParameterKey<Entity> getParamType() {
		return paramType;
	}
}
