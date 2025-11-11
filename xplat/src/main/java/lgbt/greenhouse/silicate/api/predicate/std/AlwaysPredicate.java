package lgbt.greenhouse.silicate.api.predicate.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.type.SilicatePrimitives;

/**
 * A predicate that always returns a specific value when tested.
 * @param value The value to return in a test.
 */
public record AlwaysPredicate(Deferred<Boolean> value) implements GamePredicate<AlwaysPredicate> {
	public AlwaysPredicate(boolean value) {
		this(new Deferred<>(value));
	}

	@Override
	public boolean test(GameContext ctx) {
		return value.get(ctx);
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
