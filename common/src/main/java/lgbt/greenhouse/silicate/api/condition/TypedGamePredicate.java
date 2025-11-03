package lgbt.greenhouse.silicate.api.condition;

import lgbt.greenhouse.silicate.api.context.param.GlobalParameterKey;

/**
 * A {@link GamePredicate} that has a {@link GlobalParameterKey}.
 */
public interface TypedGamePredicate<T extends GamePredicate<T>, P> extends GamePredicate<T> {
}
