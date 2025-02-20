package net.modgarden.silicate.api.condition;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.modgarden.silicate.api.context.GameContext;
import net.modgarden.silicate.api.context.param.ContextParamType;

import java.util.function.Predicate;

/**
 * A condition that may be either {@link TypedGameCondition} or {@link GameCondition}.
 * @param <P> The type of parameter.
 */
@SuppressWarnings("rawtypes") // This class does not care about the type and does not require it.
public record MaybeTypedCondition<P>(
		Either<Holder<TypedGameCondition<?, P>>, Holder<GameCondition<?>>> either
) implements GameCondition, Predicate {
	public static <P> MaybeTypedCondition<P> of(TypedGameCondition<?, P> condition) {
		return new MaybeTypedCondition<>(Either.left(Holder.direct(condition)));
	}

	public static <P> MaybeTypedCondition<P> of(GameCondition<?> condition) {
		return new MaybeTypedCondition<>(Either.right(Holder.direct(condition)));
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
	public GameConditionType<?> getType() {
		return either.map(holder -> holder.value().getType(), holder -> holder.value().getType());
	}

	/**
	 * Return either the parameter type of the {@link TypedGameCondition} or the default parameter type.
	 * @param defaultParamType The default {@link ContextParamType}. This is typically the caller's parameter type.
	 * @return The condition's parameter type or the default parameter type.
	 */
	public ContextParamType<P> getParamTypeOrDefault(ContextParamType<P> defaultParamType) {
		return either.map(holder -> holder.value().getParamType(), untyped -> defaultParamType);
	}
}
