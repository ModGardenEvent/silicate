package lgbt.greenhouse.silicate.api.predicate.std;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.predicate.SilicatePredicateTypes;
import lgbt.greenhouse.silicate.api.predicate.meta.Deferred;
import lgbt.greenhouse.silicate.api.predicate.meta.PredicateCodecBuilder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.type.SilicateValueTypes;
import lgbt.greenhouse.silicate.api.type.ValueType;

public record IsTypePredicate(
		ParameterKey.Reference<?> parameter,
		Deferred<ValueType<?>> valueType
) implements GamePredicate<IsTypePredicate> {
	@Override
	public boolean test(GameContext ctx) {
		return valueType.get(ctx)
				.isAssignableFrom(ctx
						.getParameter(this.parameter)
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
					.withReference(
							"parameter",
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
