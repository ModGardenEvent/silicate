package net.modgarden.silicate.api.context.param;

import net.modgarden.silicate.api.context.GameContext;

/**
 * A parameter to {@link GameContext}.
 * @param value the value of the parameter.
 * @param <T> type of the value.
 */
public record ContextParam<T>(T value) {
}
