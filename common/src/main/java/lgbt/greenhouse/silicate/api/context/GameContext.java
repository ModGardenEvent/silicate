package lgbt.greenhouse.silicate.api.context;

import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.condition.GamePredicate;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;
import org.jetbrains.annotations.Nullable;

/**
 * Context useful to {@link GamePredicate}.
 */
public class GameContext {
	@Nullable
	private final Level level;
	private final ParameterMap params;

	protected GameContext(@Nullable Level level, ParameterMap params) {
		this.level = level;
		this.params = params;
	}

	public static GameContext of(@Nullable Level level, ParameterMap params) {
		return new GameContext(level, params);
	}

	public @Nullable Level getLevel() {
		return level;
	}

	public ParameterMap getParams() {
		return params;
	}

	public <T> T getParam(ParameterKey<T> paramType) {
		return getParams().get(paramType).value();
	}
}
