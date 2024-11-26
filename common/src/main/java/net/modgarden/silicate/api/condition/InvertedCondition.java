package net.modgarden.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modgarden.silicate.api.context.GameContext;

/**
 * A condition that inverts another condition's test.
 * @param condition The condition to invert.
 */
public record InvertedCondition(
		GameCondition<?> condition
) implements GameCondition<InvertedCondition> {
	public static final MapCodec<InvertedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			GameCondition.CODEC
					.fieldOf("condition")
					.forGetter(InvertedCondition::condition)
	).apply(instance, InvertedCondition::new));

	@Override
	public boolean test(GameContext context) {
		return !condition.test(context);
	}

	@Override
	public MapCodec<InvertedCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<InvertedCondition> getType() {
		return GameConditionTypes.INVERTED;
	}
}
