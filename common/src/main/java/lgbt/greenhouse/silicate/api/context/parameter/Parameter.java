package lgbt.greenhouse.silicate.api.context.parameter;

import lgbt.greenhouse.silicate.api.context.GameContext;

/**
 * A parameter to {@link GameContext}.
 * @param value the value of the parameter.
 * @param <T> type of the value.
 */
public record Parameter<T>(T value) {
}
