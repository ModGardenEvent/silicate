package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.type.ValueType;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;

/**
 * A {@link GamePredicate} that has a {@link GlobalParameterKey}.
 * @implSpec See the documentation on {@link #getParamType()}.
 * @see #getParamType()
 * @see MaybeTypedPredicate
 */
public interface TypedGamePredicate<T extends GamePredicate<T>, P> extends GamePredicate<T> {
	/**
	 * @see GlobalParameterKey#getCodec(ValueType)
	 */
	@SuppressWarnings({"unchecked", "rawtypes"}) // Checked at runtime.
	private static <P> DataResult<Holder<TypedGamePredicate<?, P>>> validate(Holder<GamePredicate<?>> condition, ValueType<P> type) {
		// Extra Spooky!
		if (condition.isBound() && condition.value() instanceof TypedGamePredicate<?, ?> typedCondition && typedCondition.getParamType().type().equals(type)) {
			return DataResult.success((Holder) condition);
		} else {
			return DataResult.error(() -> "GamePredicate is not a TypedGamePredicate");
		}
	}

	@SuppressWarnings("unchecked") // The underlying type can be cast.
	private static <P> Holder<GamePredicate<?>> toGameCondition(Holder<TypedGamePredicate<?, P>> typedCondition) {
		return (Holder<GamePredicate<?>>) (Object) typedCondition;
	}

	/**
	 * Return the codec for this {@link TypedGamePredicate}.
	 *
	 * @param <P> The value type of the parameter type.
	 * @param type The class of the type in {@link P}.
	 * @return The typed codec.
	 */
	static <P> Codec<Holder<TypedGamePredicate<?, P>>> getTypedCodec(ValueType<P> type) {
		return GamePredicate.CODEC
				.comapFlatMap(
						condition -> validate(condition, type),
						TypedGamePredicate::toGameCondition
				);
	}

	/**
	 * Return the codec for this {@link TypedGamePredicate} that also accepts a {@link GamePredicate}.
	 *
	 * @param <P> The value type of the parameter type.
	 * @param type The class of the type in {@link P}.
	 * @return The typed or untyped codec.
	 */
	static <P> Codec<MaybeTypedPredicate<P>> getMaybeTypedCodec(ValueType<P> type) {
		return Codec.either(
				getTypedCodec(type),
				GamePredicate.CODEC
		).xmap(MaybeTypedPredicate::new, MaybeTypedPredicate::either);
	}

	/**
	 * Creates an anonymous {@link TypedGamePredicate} from an untyped {@link GamePredicate}.
	 * @return The anonymous {@link TypedGamePredicate} delegate.
	 * @param <P> The value type of the parameter type.
	 * @implNote We have to give up type checking somewhere, so we do it here. The parameter type is checked later on, so this is fine. It also doesn't really matter what type the condition is since the caller provides us that condition's type.
	 */
	static <T extends GamePredicate<T>, P> TypedGamePredicate<T, P> retype(
			GlobalParameterKey<P> paramType,
			GamePredicate<?> untyped
	) {
		return new TypedGamePredicate<>() {
			@Override
			public GlobalParameterKey<P> getParamType() {
				return paramType;
			}

			@Override
			public boolean test(GameContext context) {
				return untyped.test(context);
			}

			@SuppressWarnings("unchecked")
			@Override
			public MapCodec<T> getCodec() {
				return (MapCodec<T>) untyped.getCodec();
			}

			@SuppressWarnings("unchecked")
			@Override
			public PredicateType<T> getType() {
				return (PredicateType<T>) untyped.getType();
			}
		};
	}

	/**
	 * For conditions with multiple parameter types, this would be the first or main parameter type. This is never the parameter type used in series with another condition.
	 * @return The first or main parameter type.
	 */
	GlobalParameterKey<P> getParamType();
}
