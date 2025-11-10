package lgbt.greenhouse.silicate.api.condition.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.meta.DynamicValue;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.LocalParameterKey;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;

/**
 * While technically not a predicate, this sets a parameter to a specific value.
 */
public record DefinePredicate(
		ParameterKey<?> parameterKey,
		DynamicValue dynamicValue
) implements GamePredicate<DefinePredicate> {
	@Override
	public boolean test(GameContext context) {
		// check if parameter key already exists
		ParameterKey<?> parameterKey =
				context.getParams().resolve((ParameterKey.Reference<?>) this.parameterKey);
		if (parameterKey == null) {
			parameterKey = new LocalParameterKey<>(this.parameterKey.getId(), this.dynamicValue.type());
		}

		//noinspection unchecked // it's probably fine. Object? wildcard? same thing, totally
		context.getParams().set((ParameterKey<? super Object>) parameterKey, this.dynamicValue.value());
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
					.withValue(
							"parameter",
							SilicateValueTypes.PARAMETER_KEY,
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
