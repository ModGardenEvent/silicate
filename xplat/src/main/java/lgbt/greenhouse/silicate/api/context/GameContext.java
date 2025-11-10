package lgbt.greenhouse.silicate.api.context;

import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;

/**
 * Context useful to {@link GamePredicate}.
 */
public class GameContext {
	private final Level level;
	private final ParameterMap params;

	protected GameContext(Level level, ParameterMap params) {
		this.level = level;
		this.params = params;
	}

	public static GameContext of(Level level, ParameterMap params) {
		return new GameContext(level, params);
	}

	public Level getLevel() {
		return level;
	}

	public ParameterMap getParameterMap() {
		return params;
	}

	public <T> T getParameter(ParameterKey<T> paramType) {
		return getParameterMap().getOrThrow(paramType).value();
	}
}
