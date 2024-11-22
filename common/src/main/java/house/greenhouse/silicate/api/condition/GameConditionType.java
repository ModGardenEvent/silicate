package house.greenhouse.silicate.api.condition;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.ApiStatus;

/**
 * A utility class used in codec definition for creating {@link GameCondition}s (subclasses).
 * @param codec The {@link MapCodec} of the {@link GameCondition}, returned by {@link GameCondition#getCodec()}.
 * @param <T> The type of the {@link GameCondition} subclass.
 */
@ApiStatus.Experimental
public record GameConditionType<T extends GameCondition<T>>(MapCodec<T> codec) {
}
