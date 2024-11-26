package net.modgarden.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modgarden.silicate.api.context.GameContext;

/**
 * A condition that always returns a specific value when tested.
 * @param value The value to return in a test.
 */
public record AlwaysCondition(boolean value) implements GameCondition<AlwaysCondition> {
	public static final MapCodec<AlwaysCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL
					.fieldOf("value")
					.forGetter(AlwaysCondition::value)
	).apply(instance, AlwaysCondition::new));

	@Override
	public boolean test(GameContext context) {
		return value;
	}

	@Override
	public MapCodec<AlwaysCondition> getCodec() {
		return CODEC;
	}

	@Override
	public GameConditionType<AlwaysCondition> getType() {
		return GameConditionTypes.ALWAYS;
	}
}
