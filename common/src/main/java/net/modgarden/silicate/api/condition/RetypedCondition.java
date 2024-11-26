package net.modgarden.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modgarden.silicate.api.context.GameContext;
import net.modgarden.silicate.api.context.param.ContextParamType;

/**
 * Implements {@link TypedGameCondition} on a regular {@link GameCondition}.
 * This is meant for use by datapacks.
 */
public record RetypedCondition(
		ContextParamType<?> paramType,
		GameCondition<?> condition
) implements TypedGameCondition<RetypedCondition, Object> {
	public static final MapCodec<RetypedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ContextParamType.ANY_CODEC
					.fieldOf("param_type")
					.forGetter(RetypedCondition::paramType),
			GameCondition.CODEC
					.fieldOf("condition")
					.forGetter(RetypedCondition::condition)
	).apply(instance, RetypedCondition::new));

	@SuppressWarnings("unchecked") // Checked at runtime.
	@Override
	public ContextParamType<Object> getParamType() {
		return (ContextParamType<Object>) paramType;
	}

	@Override
	public boolean test(GameContext context) {
		return condition.test(context);
	}

	@Override
	public MapCodec<RetypedCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<RetypedCondition> getType() {
		return GameConditionTypes.RETYPED;
	}
}
