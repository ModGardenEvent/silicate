package lgbt.greenhouse.silicate.api.predicate.meta;

import lgbt.greenhouse.silicate.api.context.GameContext;
import lgbt.greenhouse.silicate.api.predicate.GamePredicate;

import java.util.function.Consumer;

/**
 * A special {@link GamePredicate} that wraps other {@link GamePredicate}s in order
 * to dynamically resolve information at test-time. This is primarily used for
 * the {@code definitions} field in predicates.
 */
public final class DynamicMetaPredicate<T extends GamePredicate<T>> implements GamePredicate<T> {
	private final GamePredicate<T> predicate;
	private final Consumer<GameContext> action;

	/**
	 * Construct a new {@link DynamicMetaPredicate} for wrapping a {@link GamePredicate}.
	 * <br>
	 * This class uses a {@link Consumer} to run additional logic before testing the
	 * {@link GamePredicate}.
	 * @param predicate the predicate to wrap
	 * @param action additional logic to run before testing the {@link GamePredicate}
	 */
	public DynamicMetaPredicate(GamePredicate<T> predicate, Consumer<GameContext> action) {
		this.predicate = predicate;
		this.action = action;
	}

	@Override
	public boolean test(GameContext ctx) {
		this.action.accept(ctx);
		return this.predicate.test(ctx);
	}

	@Override
	public GamePredicate.Type<T> getType() {
		return this.predicate.getType();
	}
}
