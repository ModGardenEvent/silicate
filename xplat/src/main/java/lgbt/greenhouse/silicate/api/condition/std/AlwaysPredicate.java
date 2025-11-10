package lgbt.greenhouse.silicate.api.condition.std;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;

/**
 * A predicate that always returns a specific value when tested.
 * @param value The value to return in a test.
 */
public record AlwaysPredicate(boolean value) implements GamePredicate<AlwaysPredicate> {
	public static final MapCodec<AlwaysPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.BOOL
					.fieldOf("value")
					.forGetter(AlwaysPredicate::value)
	).apply(instance, AlwaysPredicate::new));

	@Override
	public boolean test(GameContext context) {
		return value;
	}

	@Override
	public MapCodec<AlwaysPredicate> getCodec() {
		return CODEC;
	}

	@Override
	public GamePredicate.Type<AlwaysPredicate> getType() {
		return SilicatePredicateTypes.ALWAYS;
	}

	public static class Type extends GamePredicate.Type<AlwaysPredicate> {
		@Override
		protected MapCodec<AlwaysPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(AlwaysPredicate.class))
					.withValue(
							"value",
							SilicatePrimitives.BOOLEAN,
							AlwaysPredicate::value
					)
					.build();
		}
	}
}
