package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;

/**
 * A predicate that inverts another condition's test.
 * @param condition The condition to invert.
 */
public record InvertedPredicate(
		Holder<GamePredicate<?>> condition
) implements GamePredicate<InvertedPredicate> {
	public static final MapCodec<InvertedPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			GamePredicate.CODEC
					.fieldOf("condition")
					.forGetter(InvertedPredicate::condition)
	).apply(instance, InvertedPredicate::new));

	@Override
	public boolean test(GameContext context) {
		return !condition.value().test(context);
	}

	@Override
	public MapCodec<InvertedPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public PredicateType<InvertedPredicate> getType() {
		return PredicateTypes.INVERTED;
	}
}
