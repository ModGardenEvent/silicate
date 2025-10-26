package lgbt.greenhouse.silicate.api.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.param.GlobalParameterType;

import java.util.function.Predicate;

/**
 * A predicate that may be either {@link TypedGamePredicate} or {@link GamePredicate}.
 * @param <P> The type of parameter.
 */
@SuppressWarnings("rawtypes") // This class does not care about the type and does not require it.
public record MaybeTypedPredicate<P>(
		Either<Holder<TypedGamePredicate<?, P>>, Holder<GamePredicate<?>>> either
) implements GamePredicate, Predicate {
	public static <P> MaybeTypedPredicate<P> of(TypedGamePredicate<?, P> condition) {
		return new MaybeTypedPredicate<>(Either.left(Holder.direct(condition)));
	}

	public static <P> MaybeTypedPredicate<P> of(GamePredicate<?> condition) {
		return new MaybeTypedPredicate<>(Either.right(Holder.direct(condition)));
	}

	@Override
	public boolean test(Object o) {
		return test((GameContext) o);
	}

	@Override
	public boolean test(GameContext context) {
		return either.map(typed -> typed.value().test(context), untyped -> untyped.value().test(context));
	}

	@Override
	public MapCodec<?> getCodec() {
		return either.map(holder -> holder.value().getCodec(), holder -> holder.value().getCodec());
	}

	@Override
	public PredicateType<?> getType() {
		return either.map(holder -> holder.value().getType(), holder -> holder.value().getType());
	}

	/**
	 * Return either the parameter type of the {@link TypedGamePredicate} or the default parameter type.
	 * @param defaultParamType The default {@link GlobalParameterType}. This is typically the caller's parameter type.
	 * @return The condition's parameter type or the default parameter type.
	 */
	public GlobalParameterType<P> getParamTypeOrDefault(GlobalParameterType<P> defaultParamType) {
		return either.map(holder -> holder.value().getParamType(), untyped -> defaultParamType);
	}
}
