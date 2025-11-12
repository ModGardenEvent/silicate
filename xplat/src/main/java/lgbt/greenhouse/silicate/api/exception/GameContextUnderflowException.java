package lgbt.greenhouse.silicate.api.exception;

import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;

/**
 * Thrown when the {@link ParameterMap} stack in {@link GameContext} is popped with no {@link ParameterMap}s left.
 */
public class GameContextUnderflowException extends RuntimeException {
	public GameContextUnderflowException() {
		super("Attempted to pop from GameContext with no ParameterMaps present");
	}
}
