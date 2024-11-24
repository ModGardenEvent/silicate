package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import house.greenhouse.silicate.api.context.GameContext;
import house.greenhouse.silicate.api.context.param.ContextParamType;

/**
 * A {@link GameCondition} that has a {@link ContextParamType}.
 * @see #getParamType()
 * @implSpec See the documentation on {@link #getParamType()}.
 */
public interface TypedGameCondition<T extends GameCondition<T>, P> extends GameCondition<T> {
	/**
	 * @see ContextParamType#getCodec(Class)
	 */
	@SuppressWarnings("unchecked") // Checked at runtime.
	private static <P> DataResult<TypedGameCondition<?, P>> validate(GameCondition<?> condition, Class<P> clazz) {
		// Extra Spooky!
		if (condition instanceof TypedGameCondition<?, ?> typedCondition && typedCondition.getParamType().clazz().equals(clazz)) {
			return DataResult.success((TypedGameCondition<?, P>) typedCondition);
		} else {
			return DataResult.error(() -> "GameCondition is not a TypedGameCondition");
		}
	}
	
	private static GameCondition<?> toGameCondition(TypedGameCondition<?, ?> typedCondition) {
		return typedCondition;
	}

	/**
	 * Return the codec for this {@link TypedGameCondition}
	 * @param clazz The class of the type in {@link P}.
	 * @return The typed codec.
	 * @param <P> The value type of the parameter type.
	 */
	static <P> Codec<TypedGameCondition<?, P>> getTypedCodec(Class<P> clazz) {
		return GameCondition.CODEC
				.comapFlatMap(
						condition -> validate(condition, clazz),
						TypedGameCondition::toGameCondition
				);
	}

	/**
	 * Creates an anonymous {@link TypedGameCondition} from an untyped {@link GameCondition}.
	 * @return The anonymous {@link TypedGameCondition} delegate.
	 * @param <T> The type to delegate.
	 * @param <P> The value type of the parameter type.
	 */
	static <T extends GameCondition<T>, P> TypedGameCondition<?, P> fromUntyped(
			ContextParamType<P> paramType,
			T untyped
	) {
		return new TypedGameCondition<T, P>() {
			@Override
			public ContextParamType<P> getParamType() {
				return paramType;
			}

			@Override
			public boolean test(GameContext context) {
				return untyped.test(context);
			}

			@Override
			public MapCodec<T> getCodec() {
				return untyped.getCodec();
			}

			@Override
			public GameConditionType<T> getType() {
				return untyped.getType();
			}
		};
	}
	
	/**
	 * For conditions with multiple parameter types, this would be the first or main parameter type. This is never the parameter type chained to another condition.
	 * @return The first or main parameter type.
	 */
	ContextParamType<P> getParamType();
}