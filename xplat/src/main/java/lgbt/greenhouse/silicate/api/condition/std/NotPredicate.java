package lgbt.greenhouse.silicate.api.condition.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.meta.Deferred;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;

/**
 * A predicate that inverts another condition's test.
 * @param condition The condition to invert.
 */
public record NotPredicate(
		Deferred<Holder<GamePredicate<?>>> condition
) implements GamePredicate<NotPredicate> {
	@Override
	public boolean test(GameContext ctx) {
		return !condition.get(ctx).value().test(ctx);
	}

	@Override
	public GamePredicate.Type<NotPredicate> getType() {
		return SilicatePredicateTypes.NOT;
	}

	public static final class Type extends GamePredicate.Type<NotPredicate> {
		@Override
		public MapCodec<NotPredicate> createCodec() {
			return createBaseCodec()
					.apply(PredicateCodecBuilder.of(NotPredicate.class))
					.withValue(
							"condition",
							SilicateValueTypes.CONDITION,
							NotPredicate::condition
					)
					.build();
		}
	}
}
