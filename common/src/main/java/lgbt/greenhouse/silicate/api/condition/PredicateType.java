package lgbt.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import lgbt.greenhouse.silicate.api.SilicateBuiltInRegistries;

/**
 * A utility class used in codec definition for creating {@link GamePredicate}s (subclasses).
 * @param codec The {@link MapCodec} of the {@link GamePredicate}, returned by {@link GamePredicate#getCodec()}.
 * @param <T> The type of the {@link GamePredicate} subclass.
 * @see SilicateBuiltInRegistries#PREDICATE
 */
public record PredicateType<T extends GamePredicate<T>>(MapCodec<T> codec) {
}
