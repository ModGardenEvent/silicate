package lgbt.greenhouse.silicate.api.predicate.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.predicate.meta.DynamicValue;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.LocalParameterKey;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;

/**
 * While technically not a predicate, this sets a parameter to a specific value.
 */
public record DefinePredicate(
		ParameterKey.Reference<?> parameterKey,
		DynamicValue dynamicValue
) implements GamePredicate<DefinePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		// check if parameter key already exists
		ParameterKey<?> parameterKey =
				ctx.getParameterMap().resolve(this.parameterKey);
		if (parameterKey == null) {
			parameterKey = new LocalParameterKey<>(this.parameterKey.getId(), this.dynamicValue.type());
		}

		//noinspection unchecked // it's probably fine. Object? wildcard? same thing, totally
		ctx.getParameterMap().set((ParameterKey<? super Object>) parameterKey, this.dynamicValue.value());
		return true;
	}

	@Override
	public GamePredicate.Type<DefinePredicate> getType() {
		return SilicatePredicateTypes.DEFINE;
	}

	public static final class Type extends GamePredicate.Type<DefinePredicate> {
		@Override
		protected MapCodec<DefinePredicate> createCodec() {
			return createBaseCodec()
					.apply(PredicateCodecBuilder.of(DefinePredicate.class))
					.withReference(
							"parameter",
							DefinePredicate::parameterKey
					)
					.withDynamicValue(
							"value_type",
							"value",
							DefinePredicate::dynamicValue
					)
					.build();
		}
	}
}
