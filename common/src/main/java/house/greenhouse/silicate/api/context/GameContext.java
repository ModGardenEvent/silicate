package house.greenhouse.silicate.api.context;

import house.greenhouse.silicate.api.condition.GameCondition;
import house.greenhouse.silicate.api.context.param.ContextParamType;
import house.greenhouse.silicate.api.context.param.ContextParamMap;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * Context useful to {@link GameCondition}.
 */
public class GameContext {
	@Nullable
	private final Level level;
	private final ContextParamMap params;
	
	protected GameContext(@Nullable Level level, ContextParamMap params) {
		this.level = level;
		this.params = params;
	}
	
	public static GameContext of(@Nullable Level level, ContextParamMap params) {
		return new GameContext(level, params);
	}
	
	public @Nullable Level getLevel() {
		return level;
	}
	
	public ContextParamMap getParams() {
		return params;
	}
	
	public <T> T getParam(ContextParamType<T> paramType) {
		return getParams().get(paramType).value();
	}
}