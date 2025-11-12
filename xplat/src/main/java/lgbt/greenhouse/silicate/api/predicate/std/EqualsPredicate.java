package lgbt.greenhouse.silicate.api.predicate.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.predicate.meta.DynamicValue;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;

import java.util.Objects;

public record EqualsPredicate(
		DynamicValue left,
		DynamicValue right
) implements GamePredicate<EqualsPredicate> {
	@Override
	public boolean test(GameContext ctx) {
		return Objects.equals(left.value().get(ctx), right.value().get(ctx));
	}

	@Override
	public GamePredicate.Type<EqualsPredicate> getType() {
		return SilicatePredicateTypes.EQUALS;
	}

	public static final class Type extends GamePredicate.Type<EqualsPredicate> {
		@Override
		protected MapCodec<EqualsPredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(EqualsPredicate.class))
					.withDynamicValue(
							"value_type",
							"left",
							EqualsPredicate::left
					)
					.withDynamicValue(
							"value_type",
							"right",
							EqualsPredicate::right
					)
					.build();
		}
	}
}
