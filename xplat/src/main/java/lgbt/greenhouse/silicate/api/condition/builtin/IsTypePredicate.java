package lgbt.greenhouse.silicate.api.condition.builtin;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.condition.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.condition.meta.Deferred;
import lgbt.greenhouse.silicate.api.condition.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import lgbt.greenhouse.silicate.api.type.ValueType;

public record IsTypePredicate(
		Deferred<ParameterKey<?>> parameter,
		Deferred<ValueType<?>> valueType
) implements GamePredicate<IsTypePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		return valueType.get(ctx)
				.isAssignableFrom(ctx
						.getParameter(parameter.get(ctx))
						.getClass());
	}

	@Override
	public GamePredicate.Type<IsTypePredicate> getType() {
		return SilicatePredicateTypes.IS_TYPE;
	}

	public static final class Type extends GamePredicate.Type<IsTypePredicate> {
		@Override
		protected MapCodec<IsTypePredicate> createCodec() {
			return this.createBaseCodec()
					.apply(PredicateCodecBuilder.of(IsTypePredicate.class))
					.withValue(
							"parameter",
							SilicateValueTypes.PARAMETER_KEY,
							IsTypePredicate::parameter
					)
					.withValue(
							"value_type",
							SilicateValueTypes.VALUE_TYPE,
							IsTypePredicate::valueType
					)
					.build();
		}
	}
}
