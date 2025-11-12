package lgbt.greenhouse.silicate.api.context;

import lgbt.greenhouse.silicate.api.context.parameter.ParameterKey;
import lgbt.greenhouse.silicate.api.exception.GameContextUnderflowException;
import net.minecraft.world.level.Level;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;
import lgbt.greenhouse.silicate.api.context.parameter.ParameterMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Context useful to {@link GamePredicate}.
 */
public class GameContext {
	private final Level level;
	private final List<ParameterMap> parameterMaps;

	protected GameContext(Level level, ParameterMap initialParameterMap) {
		this.level = level;
		this.parameterMaps = new ArrayList<>(1);
		this.parameterMaps.add(initialParameterMap);
	}

	public static GameContext of(Level level, ParameterMap initialParameterMap) {
		return new GameContext(level, initialParameterMap);
	}

	public Level getLevel() {
		return level;
	}

	/**
	 * Push a copy of the {@link ParameterMap} onto the {@link GameContext}.
	 * <br>
	 * Predicates are required to call this method before testing nested conditions,
	 * or else the scope will leak.
	 * @see GamePredicate#pushTestPop(GameContext)
	 */
	public void pushParameterMap() {
		this.parameterMaps.add(this.getParameterMap().copy());
	}

	/**
	 * Pop the last {@link ParameterMap} from the {@link GameContext}.
	 * <br>
	 * Predicates are required to call this method after testing nested conditions,
	 * or else the scope will leak.
	 * @see GamePredicate#pushTestPop(GameContext)
	 */
	public void popParameterMap() {
		if (this.parameterMaps.size() <= 1) {
			throw new GameContextUnderflowException();
		}

		this.parameterMaps.removeLast();
	}

	public ParameterMap getParameterMap() {
		return parameterMaps.getLast();
	}

	public <T> T getParameter(ParameterKey<T> paramType) {
		return getParameterMap().getOrThrow(paramType).value();
	}
}
