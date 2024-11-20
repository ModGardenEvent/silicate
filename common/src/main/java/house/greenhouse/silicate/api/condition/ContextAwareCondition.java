package house.greenhouse.silicate.api.condition;

import house.greenhouse.silicate.api.context.GameContext;

/**
 * An extension of {@link GameCondition} that additionally has access to the {@link GameContext}.
 */
public interface ContextAwareCondition<T extends ContextAwareCondition<T, C>, C extends GameContext> extends GameCondition<T> {
	/**
	 * @return the {@link GameContext}.
	 */
	C getContext();
}